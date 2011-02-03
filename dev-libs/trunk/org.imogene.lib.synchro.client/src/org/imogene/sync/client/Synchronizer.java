package org.imogene.sync.client;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
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



public class Synchronizer extends Thread {

	private boolean run = true;
	
	private int period = 10000;
	
	private boolean loopMode=true;
	
	private boolean isRunning = false;
	
	private SyncClient syncClient;
		
	private SyncParameters parameters;
	
	private HistoryDao historyDao;
	
	private SyncParametersDao parametersDao;
	
	private Logger logger = Logger.getLogger("org.imogene.sync.client.Synchronizer");
	
	private DataHandlerManager dataManager;
	
	private SerializerManager serializerManager;
	

	/**
	 * Set the SyncClient implementation to use 
	 * @param client the SyncClient implementation
	 */
	public void setSyncClient(SyncClient client){
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
					"parameters not initialized, thread not started");
		else {
			isRunning=true;					
			while (run) {				
				try{
					oneProcess();
					run = loopMode;
					sleep(period);
				}catch(Exception ex){
					logger.error(ex.getMessage());
				}
			}
			isRunning = false;
		}
	}
	
	private void oneProcess() throws Exception {

		// 1 - initialize the session
		logger.debug("starting a synchronization session");
		String sessionId = syncClient.initSession(parameters.getLogin(),
				parameters.getPassword(), parameters.getModifiedFrom(),
				parameters.getType());
		if (sessionId != null && !sessionId.startsWith(SyncClient.ERROR_PREFIX)) {
			logger.debug("Session id " + sessionId);


			// 2 - send client modification
			logger.debug("Starting sending local modification.");
			File outFile = File.createTempFile("medoo", ".lmodif");
			FileOutputStream fos = new FileOutputStream(outFile);
			Integer entityNb = getDataToSynchronize(fos);
			fos.close();
			
			if (entityNb!=null && entityNb>0) {
				
				// create history
				SyncHistory history = new SyncHistory();
				history.setId(BeanKeyGenerator.generateKeyId());
				history.setDate(new Date(System.currentTimeMillis()));			
				// send data
				FileInputStream fis = new FileInputStream(outFile);
				int res = syncClient.sendClientModification(sessionId, fis);
				fis.close();	
				logger.debug("number of local modifications applied: " + res);
				if (res > -1) {
					history.setStatus(SyncHistory.STATUS_OK);
				} else {
					history.setStatus(SyncHistory.STATUS_ERROR);
				}			
				// save history
				historyDao.store(history);
				historyDao.deleteOldHistories();
				
			}		
			outFile.delete();

			
			// 3 - get server modifications
			logger.debug("Starting receiving server modifications");
			File inFile = File.createTempFile("medoo", ".smodif");
			FileOutputStream sFos = new FileOutputStream(inFile);
			syncClient.requestServerModifications(sessionId, sFos);
			sFos.close();
			FileInputStream sFis = new FileInputStream(inFile);
			applyIncomingModifications(sFis);
			sFis.close();
			inFile.delete();

			// 4 - close the session
			logger.debug("Closing the session");
			syncClient.closeSession(sessionId);
		}
	}
	
	/**
	 * Get the entities to be sent to the server for synchronization
	 * @param os outputstream where to write data
	 * @return nb of entities to be synchronized
	 */
	private Integer getDataToSynchronize(OutputStream os){		
				
		
		Set<SynchronizableEntity> types = parameters.getSynchronizable();
		Date lastSynchro = computeLastDate();
		List<Synchronizable> entities = new Vector<Synchronizable>();
		//for (int i = 0; i < types.length; i++) {
		for(SynchronizableEntity classEntity : types){
			EntityHandler dao = dataManager.getHandler(classEntity.getName());
			entities.addAll(dao.loadModified(lastSynchro, null));
		}
		
		if (entities.size()>0) {
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
				return entities.size();
			} catch (ImogSerializationException se) {
				logger.error(se.getMessage(), se);
				return null;
			} catch (IOException ioe) {
				logger.error(ioe.getMessage(), ioe);
				return null;
			}			
		}
		return null;
		
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
	private void applyIncomingModifications(InputStream is){		
			
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
			logger.error(ioe.getMessage());
		}
	}
	
	/**
	 * Stop the synchronizer
	 */
	public void stopSynchronizer(){
		run = false;
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
	
	
}
