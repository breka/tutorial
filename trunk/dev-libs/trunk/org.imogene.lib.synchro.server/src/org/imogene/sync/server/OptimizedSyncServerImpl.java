package org.imogene.sync.server;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.SynchronizableUtil;
import org.imogene.common.data.handler.BeanKeyGenerator;
import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.serializer.ImogSerializationException;
import org.imogene.sync.serializer.ImogSerializer;
import org.imogene.sync.serializer.SerializerManager;
import org.imogene.sync.server.history.SyncHistory;
import org.imogene.sync.server.history.SyncHistoryDao;
import org.imogene.sync.server.metadata.ImogMetadataDao;
import org.imogene.sync.server.metadata.ImogMetadataSerializer;
import org.imogene.sync.server.metadata.MetadataSerializationException;
import org.imogene.sync.server.session.SyncSession;
import org.imogene.sync.server.session.SyncSessionDao;
import org.imogene.uao.defaultuser.DefaultUser;
import org.imogene.uao.defaultuser.server.DefaultUserHandler;
import org.imogene.uao.synchronizable.SynchronizableEntity;

/**
 * Implementation of a synchronization server.
 * @author MEDES-IMPS
 */
public class OptimizedSyncServerImpl implements OptimizedSyncServer {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.server");	
	
	private SyncHistoryDao historyDao;
	
	private SyncSessionDao sessionDao;
	
	private ImogMetadataDao metadataDao;
	
	private DataHandlerManager dataHandlerManager;
	
	private SerializerManager serializerManager;
	
	private DefaultUserHandler defaultUserHandler;	
	
	private EntityHelper entityHelper;
		
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#applyClientModifications(java.lang.String, java.io.InputStream)
	 */
	public int applyClientModifications(String sessionId, InputStream data) throws ImogSerializationException {
		int i=0;
		if(checkSession(sessionId)){
			SyncSession session = sessionDao.load(sessionId);
			String type = session.getType();
			String userId = session.getUserId();
			String userType = session.getUserType();
			SynchronizableUser currentUser = getCurrentUser(userId, userType);
			ImogSerializer serializer = serializerManager.getSerializer(type);
			if(serializer!=null){
				i =  serializer.processMulti(data, currentUser);
			} else {
				throw new ImogSerializationException("No serializer registred for the type: " + type);			
			}					
			return i;					
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#applyMetadata(java.io.InputStream)
	 */
	public void applyMetadata(InputStream data)
			throws MetadataSerializationException {		
		new ImogMetadataSerializer(metadataDao).unserialize(data);		
	}



	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#checkSession(java.lang.String)
	 */
	public boolean checkSession(String sessionId) {
		try{
			Thread.sleep(1000);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return sessionDao.isValid(sessionId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#closeSession(java.lang.String, int)
	 */
	public int closeSession(String sessionId, int status) {
		/* store the history of the session */
		SyncHistory history = new SyncHistory();
		history.setId(BeanKeyGenerator.getNewId("HIS"));
		SyncSession session = sessionDao.load(sessionId);		
		history.setTerminalId(session.getTerminalId());
		history.setTime(session.getSendDate());
		history.setStatus(status);		
		historyDao.saveOrUpdate(history);
		historyDao.deleteOldHistories(session.getTerminalId());		
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#searchEntity(java.lang.String)
	 */
	public void searchEntity(SynchronizableUser currentUser, String entityId, OutputStream out) throws ImogSerializationException {	

		Map<String, String> classnames = SynchronizableUtil.getInstance().getEntityClassReferences();
		for(String classname : classnames.values()) {
			
			EntityHandler handler = dataHandlerManager.getHandler(classname);
			if (handler!=null) {
				Synchronizable bean = handler.loadEntity(entityId, currentUser);
				if (bean!=null) {
					List<Synchronizable> entities = entityHelper.getAssociatedEntitiesIds(bean);
					entities.add(bean);
					serializerManager.getSerializer("xml").serialize(entities, out);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#getServerModifications(java.lang.String, java.io.OutputStream)
	 */
	public void getServerModifications(String sessionId, OutputStream out) throws ImogSerializationException {
		if(checkSession(sessionId)){			
			SyncSession session = sessionDao.load(sessionId);
			String userId = session.getUserId();
			String userType = session.getUserType();
			SynchronizableUser currentUser = getCurrentUser(userId, userType);
			String type = session.getType();
			List<Synchronizable> allEntities = new Vector<Synchronizable>();			
			List<String> synchronizables = getSynchronizables(currentUser.getSynchronizables());	
			Date lastDate = computeLastDate(session.getTerminalId());
			
			// serialize entities
			for(String entityId: synchronizables){	
				EntityHandler handler = dataHandlerManager.getHandler(entityId);
				if(handler!=null){
					List<Synchronizable> modified = null;		
					if (lastDate==null)
						modified = handler.loadEntities(currentUser, session.getTerminalId());
					else
						modified = handler.loadModified(lastDate, currentUser, session.getTerminalId());

					
					if (modified!=null && modified.size()>0)
						allEntities.addAll(modified);
				}
			}
			logger.debug("SeMo: " + allEntities.size() + " entities loaded, before filtering with the terminal id");
			
			
			// removes entities that have just been sent by the client terminal
			List<Synchronizable> toBeRemoved = new Vector<Synchronizable>();
			for (Synchronizable entity:allEntities) {
				if (entity.getModifiedFrom().equals(SyncConstants.SYNC_ID_SYS) || entity.getModifiedFrom().equals(session.getTerminalId())) {
					toBeRemoved.add(entity);
				}				
			}
			allEntities.removeAll(toBeRemoved);
			
			// TODO serialize current user
/*			if (! (user instanceof DefaultUser)) {
				Synchronizable modifiedUser;
				EntityHandler handler = dataHandlerManager.getHandler(user.getClass().getSuperclass().getName());
				if (lastDate!=null) 		
					modifiedUser = handler.getDao().loadModified(lastDate, user.getId());
				else 
					modifiedUser = handler.getDao().loadEntity(user.getId());					
				
				if (modifiedUser!=null)
					allEntities.add(modifiedUser);				
			}*/
			logger.debug("SeMo: " + allEntities.size()+ " entities kept, before the serialization");
			serializerManager.getSerializer(type).serialize(allEntities, out);		
		}		
	}		

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#initSession(java.lang.String, java.lang.String, org.imogene.common.data.SynchronizableUser)
	 */
	public String initSession(String termId, String type, SynchronizableUser user) {
		SyncSession session = new SyncSession();
		session.setId(UUID.randomUUID().toString());
		session.setTerminalId(termId);
		session.setType(type);
		session.setUserId(user.getId());
		if (user instanceof DefaultUser)
			session.setUserType(null);
		else
			session.setUserType(SynchronizableUtil.getInstance().getEntityShortName(user.getClass().getName()));
		session.setInitDate(new Date(System.currentTimeMillis()));
		session.setSendDate(new Date(System.currentTimeMillis()));
		sessionDao.saveOrUpdate(session);
		return session.getId().toString();
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#initResumeSendSession(java.lang.String, java.lang.String, org.imogene.common.data.SynchronizableUser, java.lang.String)
	 */
	public String initResumeSendSession(String termiId, String type,
			SynchronizableUser user, String sessionId) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.OptimizedSyncServer#initResumeRequestSession(java.lang.String, java.lang.String, org.imogene.common.data.SynchronizableUser, java.lang.String, long)
	 */
	public String initResumeRequestSession(String termiId, String type,
			SynchronizableUser user, String sessionId, long bytesReceived) {		
		return "OK";
	}	

	public File getFileDirectory() {
		throw new RuntimeException("Not impelmented.");
	}

	public int resumeApplyClientModifications(String sessionId, InputStream data)
			throws ImogSerializationException {		
		return 0;
	}

	public void resumeGetServerModifications(String sessionId,
			OutputStream out, long bytesToSkip)
			throws ImogSerializationException {				
	}

	
	
	/**
	 * Compute the date of the last 
	 * synchronization of the specified terminal;
	 * @param terminalId the terminal id
	 * @return the date or null if it is the first synchronization.
	 */
	private Date computeLastDate(String terminalId){
		SyncHistory last = historyDao.loadLastHistory(terminalId);
		if(last != null)
			return last.getTime();
		return null;
		
	}
	
	/**
	 * Get a list of synchronizable entity names
	 * @param syncEntities a set of Synchronizable Entity instances
	 * @return a list of synchronizable entity names
	 */	
	private List<String> getSynchronizables(Set<SynchronizableEntity> syncEntities) {
		List<String> synchronizables = new ArrayList<String>();
		for(SynchronizableEntity current:syncEntities) {
			SynchronizableEntity entity = current;
			synchronizables.add(entity.getName());	
		}
		return synchronizables;
	}
	 
	/**
	 * Loads the current user from DB
	 * @param userId the user ID
	 * @return the user bean
	 */
	 private SynchronizableUser getCurrentUser(String userId, String UserType) {
		 
		EntityHandler handler = null;

		if (UserType!=null) {
			String entityClassName = SynchronizableUtil.getInstance().getEntityPath(UserType);
			if (entityClassName != null) {
				handler = dataHandlerManager.getHandler(entityClassName);
			}
		}
		else {
			handler = defaultUserHandler;		
		}
		
		if (handler != null) {
			SynchronizableUser user = (SynchronizableUser) handler.getDao().loadEntity(userId);
			if (user != null)
				return user;
		}
				
		return null;
	}
	
	/**
	 * Setter for bean injection
	 * @param historyHandler
	 */
	public void setHistoryDao(SyncHistoryDao pHistoryDao) {
		this.historyDao = pHistoryDao;
	}

	/**
	 * Setter for bean injection
	 * @param historyHandler
	 */
	public void setSessionDao(SyncSessionDao pSessionDao) {
		sessionDao = pSessionDao;
	}
	
	/**
	 * setter for bean injection
	 * @param dao the meta-data dao.
	 */
	public void setMetadataDao(ImogMetadataDao dao){
		metadataDao = dao;
	}
	
	/**
	 * Setter for bean injection
	 * @param dataHandlerManager
	 */
	public void setDataHandlerManager(DataHandlerManager dataHandlerManager) {
		this.dataHandlerManager = dataHandlerManager;
	}

	/**
	 * Setter for bean injection
	 * @param serializerManager
	 */
	public void setSerializerManager(SerializerManager serializerManager) {
		this.serializerManager = serializerManager;
	}

	/**
	 * Setter for bean injection
	 * @param defaultUserHandler
	 */	
	public void setDefaultUserHandler(DefaultUserHandler defaultUserHandler) {
		this.defaultUserHandler = defaultUserHandler;
	}	

	public void setEntityHelper(EntityHelper helper){
		entityHelper = helper;
	}
	
}
