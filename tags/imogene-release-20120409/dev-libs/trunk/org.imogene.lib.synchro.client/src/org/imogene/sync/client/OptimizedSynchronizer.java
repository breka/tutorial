package org.imogene.sync.client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.client.dao.SyncParametersDao;
import org.imogene.sync.client.history.HistoryDao;
import org.imogene.sync.client.history.SyncHistory;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.ImogSerializer;
import org.imogene.sync.serializer.SerializerManager;
import org.imogene.uao.synchronizable.SynchronizableEntity;



public class OptimizedSynchronizer extends Thread {

	private boolean run = true;
	
	private boolean loopMode = false;
	
	private int period = 10000;	
	
	private boolean isRunning = false;
	
	private OptimizedSyncClient syncClient;
		
	private SyncParameters parameters;
	
	private HistoryDao historyDao;
	
	private SyncParametersDao parametersDao;
	
	private Logger logger = Logger.getLogger("org.imogene.sync.client.Synchronizer");
	
	private DataHandlerManager dataManager;
	
	private SerializerManager serializerManager;
	
	private File directory;
	
	private Set<SyncListener> listeners = new HashSet<SyncListener>();
	
	public OptimizedSynchronizer(String directoryPath){
		directory = new File(directoryPath);
	}
	
	public OptimizedSynchronizer(String directoryPath, int pPeriod){		
		directory = new File(directoryPath);
		loopMode = true;
		period = pPeriod;
	}

	/**
	 * Set the SyncClient implementation to use 
	 * @param client the SyncClient implementation
	 */
	public void setSyncClient(OptimizedSyncClient client){
		syncClient = client;
	}
	
	/**
	 * Set the DAO to access to the synchronization parameters
	 * @param dao the DAO to access to the synchronization parameters
	 */
	public void setSyncParametersDao(SyncParametersDao dao){
		parametersDao = dao;
	}
	
	/**
	 * Set the DAO to use to access to the histories
	 * @param dao the DAO to access to the synchronization histories
	 */
	public void setHistoryDao(HistoryDao dao){
		historyDao = dao;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {	
		
		parameters = parametersDao.load();
		if (parameters == null)
			throw new RuntimeException(
					"Synchronization parameters not initialized, thread not started");
		else {
			logger.debug("Starting the server, run="+run);
			isRunning=true;		
			run = true;
			while (run) {				
				try{
					try{
						oneProcess();					
					}catch(SynchronizationException ex){
						for(SyncListener listener : listeners)
							listener.syncError(ex.getCode());
						logger.error(ex);
						ex.printStackTrace();						
					}
					run = loopMode;
					if(run)
						sleep(period);					
				}catch(InterruptedException ex){					
					logger.error(ex.getMessage());
				}
			}
			isRunning = false;
		}
	}
	
	private void oneProcess() throws SynchronizationException {

		try {
			// TODO look for synchronization ERROR.
			SyncHistory error = historyDao.loadLastError();

			/*
			 * We resume the synchronization process
			 */
			if (error != null) {
				resumeOnError(error);
			}

			/*
			 * new synchronization process even we proceed to a resume on error
			 * before
			 */
			/* 1 - initialize the session */
			logger.debug("starting a synchronization session");			
			String sessionId = syncClient.initSession(parameters.getLogin(), parameters.getPassword(), parameters.getModifiedFrom(),
					parameters.getType());
			for(SyncListener listener : listeners)
				listener.initSession(sessionId);
			if (sessionId != null && !sessionId.startsWith(SyncClient.ERROR_PREFIX)) {
				logger.debug("Session id " + sessionId);

				/* 2 - send client modification */				
				logger.debug("Starting sending local modification.");
				File outFile = new File(directory, sessionId + ".lmodif");
				FileOutputStream fos = new FileOutputStream(outFile);
				/* we take the date just before to access the database and to serialize */
				Date tempDate = new Date(System.currentTimeMillis());
				getDataToSynchronize(fos);
				fos.close();

				SyncHistory history = new SyncHistory();
				history.setId(sessionId);
				history.setDate(tempDate);
				history.setStatus(SyncHistory.STATUS_ERROR);
				history.setLevel(SyncHistory.LEVEL_SEND);
				historyDao.store(history);
				for(SyncListener listener : listeners)
					listener.sending(0);
				/* send data */
				FileInputStream fis = new FileInputStream(outFile);
				int res = syncClient.sendClientModification(sessionId, fis);
				fis.close();
				logger.debug("number of server modifications applied: " + res);
				if (res > -1) {
					history.setLevel(SyncHistory.LEVEL_RECEIVE);
					historyDao.store(history);
				} else {
					throw new SynchronizationException("Error sending data to the server.", SynchronizationException.ERROR_SEND);
				}

				/* 3 - get server modifications */
				logger.debug("Starting receiving server modifications");
				File inFile = new File(directory, sessionId + ".smodif");
				FileOutputStream sFos = new FileOutputStream(inFile);
				for(SyncListener listener : listeners)
					listener.receiving(0);				
				syncClient.requestServerModifications(sessionId, sFos);
				sFos.close();
				FileInputStream sFis = new FileInputStream(inFile);
				applyIncomingModifications(sFis);
				sFis.close();

				/* Update the sync history */
				history.setLevel(SyncHistory.LEVEL_RECEIVE);
				history.setStatus(SyncHistory.STATUS_OK);
				historyDao.store(history);

				/* 4 - close the session */
				logger.debug("Closing the session");
				syncClient.closeSession(sessionId);
				for(SyncListener listener : listeners)
					listener.finish();
				/* now we are sure that we never need this temp file */
				inFile.delete();
				outFile.delete();
			}
		}catch (Exception ioe) {
			ioe.printStackTrace();
			SynchronizationException syx = new SynchronizationException("Error during the synchronization caused by : ", ioe, SynchronizationException.DEFAULT_ERROR);
			if(ioe instanceof SynchronizationException)
				syx.setCode(((SynchronizationException)ioe).getCode());
			throw syx;
		}
	}
	
	/**
	 * Resume a synchronization process 
	 * that terminated with error.
	 * @param error the synchronization history
	 * @throws SynchronizationException if an error occurred
	 */
	private void resumeOnError(SyncHistory error) throws SynchronizationException {
		/* we resume a sent, by re-sending local data
		 * an retrieving all the data from the server */
		if(error.getLevel()==SyncHistory.LEVEL_SEND){
			logger.debug("Resuming the sent for the session "+error.getId());
			try{
				for(SyncListener listener:listeners)
					listener.resumeSend(0, 0);
				/* 1 - initialize the resumed session */
				String result = syncClient.resumeSend(parameters.getLogin(),
				parameters.getPassword(), parameters.getModifiedFrom(),
				parameters.getType(),error.getId());
				/* 2 - sending local modifications */
				if(result.equals("error")){
					throw new SynchronizationException("Error resuming the session, the server return an error code", SynchronizationException.ERROR_SEND);
				}
				else{					
					int bytesReceived = Integer.parseInt(result);
					File outFile = new File(directory, error.getId()+".lmodif");
					FileInputStream fis = new FileInputStream(outFile);
					fis.skip(bytesReceived);		
					logger.debug("Re-sending data from the file "+outFile.getAbsolutePath()+" skipping "+ bytesReceived + " bytes");
					syncClient.resumeSendModification(error.getId(), fis);
					fis.close();
					outFile.delete();
				}	
				error.setLevel(SyncHistory.LEVEL_RECEIVE);
				historyDao.store(error);
				/* 3 - receiving the server modifications */
				requestServerModification(error.getId());
				error.setStatus(SyncHistory.STATUS_OK);
				historyDao.store(error);
				/* 4 - closing the session */
				logger.debug("Closing the session");
				syncClient.closeSession(error.getId());
				for(SyncListener listener : listeners)
					listener.finish();			
				
			}catch(Exception ex){
				SynchronizationException syx =  new SynchronizationException("Error resuming a sent", ex, SynchronizationException.DEFAULT_ERROR);
				if(ex instanceof SynchronizationException)
					syx.setCode(((SynchronizationException)ex).getCode());
				throw syx;
			}			
		}
		/* we resume a reception, 
		 * by re-receiving the server data */
		if(error.getLevel()==SyncHistory.LEVEL_RECEIVE){			
			logger.debug("Resuming the receive operation for the session "+error.getId());
			try{
				/* clear the sent file */
				File tmp = new File(directory, error.getId()+".lmodif");
				if(tmp.exists())
					tmp.delete();
				/* 1 - initialize the resumed session */
				File inFile = new File(directory, error.getId()+".smodif");
				String result = syncClient.resumeReceive(parameters.getLogin(),
						parameters.getPassword(), parameters.getModifiedFrom(),
						parameters.getType(),error.getId(), inFile.length());
				/* 2 - receiving data */
				if(!result.equals("error")){
					FileOutputStream fos = new FileOutputStream(inFile, true);
					for(SyncListener listener : listeners)
						listener.resumeReceive(0, 0);
					syncClient.resumeRequestModification(error.getId(), fos, inFile.length());
					FileInputStream sFis = new FileInputStream(inFile);
					applyIncomingModifications(sFis);
					error.setStatus(SyncHistory.STATUS_OK);
					historyDao.store(error);
					/* 3 - closing the session */
					logger.debug("Closing the session");
					syncClient.closeSession(error.getId());
					inFile.delete();
					for(SyncListener listener : listeners)
						listener.finish();
				}else{
					throw new SynchronizationException("The server return an error code", SynchronizationException.ERROR_RECEIVE);
				}
			}catch(Exception ex){
				SynchronizationException syx = new SynchronizationException("Error resuming a receive operation", ex, SynchronizationException.ERROR_RECEIVE);
				if(ex instanceof SynchronizationException)
					syx.setCode(((SynchronizationException)ex).getCode());
				throw syx;
			}
		}
	}
	
	/**
	 * Request the server modification in normal mode
	 * @param sessionId the session Id
	 * @throws Exception if an error occurred.
	 */
	private void requestServerModification(String sessionId) throws Exception {
		logger.debug("Starting receiving server modifications");
		File inFile = new File(directory, sessionId+".smodif");
		FileOutputStream sFos = new FileOutputStream(inFile);
		for(SyncListener listener:listeners)
			listener.receiving(0);
		syncClient.requestServerModifications(sessionId, sFos);
		sFos.close();
		FileInputStream sFis = new FileInputStream(inFile);
		applyIncomingModifications(sFis);
		sFis.close();		
		//inFile.delete();
	}
	
	/**
	 * Get the entities to be sent to the server for synchronization
	 * @param os output stream where to write data
	 * @return number of entities to be synchronized
	 */
	private Integer getDataToSynchronize(OutputStream os) throws SynchronizationException {		
		
		Set<SynchronizableEntity> types = parameters.getSynchronizable();
		Date lastSynchro = computeLastDate();
		List<Synchronizable> entities = new Vector<Synchronizable>();
		
		for(SynchronizableEntity classEntity : types){
			EntityHandler dao = dataManager.getHandler(classEntity.getName());
			entities.addAll(dao.loadModified(lastSynchro, null));
		}
		logger.debug("Number of entities to send : " + entities.size());
		try {
			if (parameters.getType().equals("bin")) {
				ImogSerializer serializer = serializerManager.getSerializer("bin");
				serializer.serialize(entities, os);
			}
			if (parameters.getType().equals("xml")) {
				ImogSerializer serializer = serializerManager.getSerializer("xml");
				serializer.serialize(entities, os);
			}
			os.close();				
		} catch (ImogSerializationException se) {			
			throw new SynchronizationException("Error preparing the data to synchronize.", se);
		} catch (IOException ioe) {			
			throw new SynchronizationException("Error preparing the data to synchronize.", ioe);
		}			
		return entities.size();
	}
	
	/**
	 * Compute the date of the last synchronization
	 * @return the date of the last synchronization
	 */
	private Date computeLastDate(){		
		SyncHistory history = historyDao.loadLastOk();
		if(history != null)
			return history.getDate();
		return new Date(-1);
	}
	
	/**
	 * Apply the incoming server modification locally.
	 * @param data incoming bytes
	 */
	private void applyIncomingModifications(InputStream is) throws SynchronizationException {		
			
		try{
			if (parameters.getType().equals("bin")) {
				DataInputStream dis = new DataInputStream(is);
				while (dis.available() > 0) {
					String type = dis.readUTF();
					logger.debug("handle class :" + type);
					try {
						Synchronizable entity = serializerManager
								.getSerializer(type).deSerialize(dis);
						if (entity != null) {
							dataManager.getHandler(type).saveOrUpdate(entity,
									null);
						}
					} catch (ImogSerializationException se) {
						logger.error(se.getMessage());
					}
				}
			}
			if(parameters.getType().equals("xml")){
				ImogSerializer serializer = serializerManager.getSerializer("xml");				
				if(serializer!=null){
					try{				
					int i =  serializer.processMulti(is, null);
					logger.debug(i+" entitie(s) added or updated to the database");
					}catch(ImogSerializationException ex){
						logger.error(ex.getMessage());
					}
				} else {
					logger.error("No serializer registred for the type: xml");			
				}
			}
		}catch(IOException ioe){
			throw new SynchronizationException("Error applying the modifications received from the server", ioe);
		}
	}
	
	/**
	 * Stop the synchronizer
	 */
	public void stopSynchronizer(){
		run = false;
		loopMode = false;
	}
	
	/**
	 * Set the loop mode
	 * @param loop true if the server loops
	 */
	public void setLoop(boolean loop){
		loopMode = loop;
		
	}
	
	public boolean getLoop(){
		return loopMode;
	}
	
	/**
	 * Set the loop period that have to be < 10000ms;
	 * @param pPeriod period in ms
	 */
	public void setPeriod(int pPeriod){
		if(pPeriod<10000)
			pPeriod = 10000;
		period = pPeriod;
	}
	
	public int getPeriod(){
		return period;
	}
	
	/**
	 * Get the status of the synchronizer
	 * @return true if the synchronizer id running, false otherwise.
	 */
	public boolean isRunning(){
		return isRunning;
	}

	public void setDataManager(DataHandlerManager manager){
		dataManager = manager;
	}
	
	public void setSerializerManager(SerializerManager manager){
		serializerManager = manager;
	}
	
	public void addSyncListener(SyncListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(SyncListener listener){
		listeners.remove(listener);
	}
	
	
}
