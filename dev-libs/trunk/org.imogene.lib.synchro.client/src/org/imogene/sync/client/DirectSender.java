package org.imogene.sync.client;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.client.dao.SyncParametersDao;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.SerializerManager;
import org.imogene.uao.synchronizable.SynchronizableEntity;


/**
 * The direct sender enables to send an entity directly after its creation
 * without having to wait until the synchronization process.
 * @author MEDES-IMPS
 */
public class DirectSender extends Thread {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.client.DirectSender");

	private boolean run = true;

	private boolean isRunning = false;

	private SyncClient syncClient;

	private SyncParametersDao parametersDao;

	private DataHandlerManager dataManager;

	private SerializerManager serializerManager;

	/**
	 * Set the client implementation to use
	 * 
	 * @param client
	 *            The synchronization client implementation
	 */
	public void setSyncClient(SyncClient client) {
		syncClient = client;
	}

	/**
	 * Set the parameter DAO
	 * 
	 * @param dao
	 *            the syncParameters DAO
	 */
	public void setParametersDao(SyncParametersDao dao) {
		parametersDao = dao;
	}
	
	/**
	 * 
	 * @param manager
	 */
	public void setDataManager(DataHandlerManager manager){
		dataManager = manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		SyncParameters parameters = parametersDao.load();
		if (parameters == null)
			throw new RuntimeException(
					"Parameters not initialized, thread not started.");

		if (syncClient == null)
			throw new RuntimeException(
					"SyncClient not initialized, thread not started.");

		if (parameters != null && syncClient != null) {

			isRunning = true;
			while (run) {

				Set<SynchronizableEntity> toSend = parameters.getDirectSend();
				for(SynchronizableEntity classEntity: toSend){
				//for (int i = 0; i < toSend.length; i++) {
					EntityHandler dao = dataManager.getHandler(classEntity.getName());
					if (dao != null && dao.countAll() > 0) {
						/* init session */
						logger.debug("Initialize a session");
						String sessionId = syncClient.initSession(parameters
								.getLogin(), parameters.getPassword(),
								parameters.getModifiedFrom(), parameters
										.getType());
						/* send data */
						if (sessionId != null
								&& !sessionId
										.startsWith(SyncClient.ERROR_PREFIX)) {
							logger.debug("Send the data");
							List<Synchronizable> entities = dao
									.loadEntities(IdentityManager.getInstance()
											.loggedUser());
							try {
								File tempFile = File.createTempFile("medoo",
										".dsend");
								FileOutputStream fos = new FileOutputStream(
										tempFile);
								Synchronizable entity = entities.get(0);
								int res = -1;
								/* bin stream serialization */
								if (parameters.getType().equals("bin")) {
									DataOutputStream dos = new DataOutputStream(
											fos);
									dos.writeUTF(classEntity.getName());
									serializerManager.getSerializer(classEntity.getName())
											.serialize(entity, fos);
									dos.close();
									fos.close();
									InputStream fis = new FileInputStream(
											tempFile);
									res = syncClient.directSend(sessionId, fis);
									fis.close();
									tempFile.delete();
								}
								/* xml serialization */

								if (res != -1)
									deleteData(entity);
							} catch (IOException ioe) {
								logger.error(ioe.getMessage());
							} catch(ImogSerializationException mse){
								logger.error(mse.getMessage());
							}
						}
					}
				}
			}
			isRunning = false;
		}
	}

	/**
	 * Send client modifications to the server
	 * 
	 * @param session
	 *            the session id
	 * @param data
	 *            the data to send
	 */
	private void deleteData(Synchronizable entity) {
		dataManager.getHandler(entity.getClass().getName()).delete(
				entity, null);
	}

	/**
	 * Get the status of the direct sender
	 * 
	 * @return true if the thread is running, false otherwise
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Ask the thread to stop
	 */
	public void stopSending() {
		run = false;
	}

}
