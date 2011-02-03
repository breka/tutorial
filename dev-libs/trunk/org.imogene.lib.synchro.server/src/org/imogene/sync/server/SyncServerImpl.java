package org.imogene.sync.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.imogene.sync.server.session.SyncSession;
import org.imogene.sync.server.session.SyncSessionDao;
import org.imogene.uao.defaultuser.server.DefaultUserHandler;
import org.imogene.uao.synchronizable.SynchronizableEntity;

/**
 * Implementation of a synchronization server.
 * @author MEDES-IMPS
 */
public class SyncServerImpl implements SyncServer {
		
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger("org.imogene.sync.server.SyncServerImpl");
	
	private SyncHistoryDao historyDao;
	
	private SyncSessionDao sessionDao;
	
	private DataHandlerManager dataHandlerManager;
	
	private SerializerManager serializerManager;
	
	private DefaultUserHandler defaultUserHandler;
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.SyncServer#applyClientModifications(java.lang.String, java.io.InputStream)
	 */
	public int applyClientModifications(String sessionId, InputStream data) throws ImogSerializationException {
		int i=0;
		if(checkSession(sessionId)){
			SyncSession session =sessionDao.load(sessionId);
			String type = session.getType();
			String userId = session.getUserId();
			SynchronizableUser currentUser = getCurrentUser(userId);
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
	 * @see org.imogene.sync.server.SyncServer#checkSession(java.lang.String)
	 */
	public boolean checkSession(String sessionId) {
		return sessionDao.isValid(sessionId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.SyncServer#closeSession(java.lang.String, int)
	 */
	public int closeSession(String sessionId, int status) {
		/* store the history of the session */
		SyncHistory history = new SyncHistory();
		history.setId(BeanKeyGenerator.getNewId("HIS"));
		SyncSession session = sessionDao.load(sessionId.toString());
		history.setTerminalId(session.getTerminalId());
		history.setTime(session.getSendDate());
		history.setStatus(status);		
		historyDao.saveOrUpdate(history);
		historyDao.deleteOldHistories(session.getTerminalId());		
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.SyncServer#getServerModifications(java.lang.String, java.io.OutputStream)
	 */
	public void getServerModifications(String sessionId, OutputStream out) throws ImogSerializationException {
		if(checkSession(sessionId)){			
			SyncSession session = sessionDao.load(sessionId);
			String userId = session.getUserId();
			SynchronizableUser currentUser = getCurrentUser(userId);
			String type = session.getType();
			List<Synchronizable> allEntities = new Vector<Synchronizable>();			
			List<String> synchronizables = getSynchronizables(currentUser.getSynchronizables());	
			Date lastDate = computeLastDate(session.getTerminalId());
			
			// serialize entities
			for(String entityId: synchronizables){	
				EntityHandler handler = dataHandlerManager.getHandler(entityId);
				if(handler!=null){
					List<Synchronizable> modified = null;				
					if (lastDate!=null)
						modified = handler.loadModified(lastDate, currentUser);
					else
						modified = handler.loadEntities(currentUser);
					
					if (modified!=null && modified.size()>0)
						allEntities.addAll(modified);
				}
			}
			
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
			serializerManager.getSerializer(type).serialize(allEntities, out);		
		}		
	}		

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.server.SyncServer#initSession(java.lang.String, java.lang.String, org.imogene.common.data.SynchronizableUser)
	 */
	public String initSession(String termId, String type, SynchronizableUser user) {
		SyncSession session = new SyncSession();
		session.setId(UUID.randomUUID().toString());
		session.setTerminalId(termId);
		session.setType(type);
		session.setUserId(user.getId());
		session.setInitDate(new Date(System.currentTimeMillis()));
		session.setSendDate(new Date(System.currentTimeMillis()));
		sessionDao.saveOrUpdate(session);
		return session.getId();
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
	 private SynchronizableUser getCurrentUser(String userId) {
		 
		EntityHandler handler = null;

		if (userId != null) {
			
			
			String[] idComp = userId.split("_");
			if (idComp.length>1) {
				/* gets user entity type form id */
				String shortName = idComp[0];
				String entityClassName = SynchronizableUtil.getInstance().getEntityPath(shortName);
				if (entityClassName != null) {
					handler = dataHandlerManager.getHandler(entityClassName);
				}
			}
			else {
				handler = defaultUserHandler;		
			}
			
			if (handler != null) {
				SynchronizableUser user = (SynchronizableUser) handler
						.getDao().loadEntity(userId);
				if (user != null)
					return user;
			}
				
		}
		return null;
	}
	
	/**
	 * Setter for bean injection
	 */
	public void setHistoryDao(SyncHistoryDao pHistoryDao) {
		this.historyDao = pHistoryDao;
	}

	/**
	 * Setter for bean injection	
	 */
	public void setSessionDao(SyncSessionDao pSessionDao) {
		sessionDao = pSessionDao;
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
	
	

	
	
	
	
}
