package org.imogene.sync.client.clientfilter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.LocalizedTextDao;
import org.imogene.common.dao.criteria.BasicCriteria;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.common.dao.criteria.ImogConjunction;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.client.EntityListener;
import org.imogene.uao.clientfilter.ClientFilter;



/**
 * Implements a data handler for the ClientFilter 
 * @author Medes-IMPS
 */
public class ClientFilterHandler implements EntityHandler {

	private static Set<EntityListener> listeners = new HashSet<EntityListener>();

	private EntityDao dao;

	public int countAll() {
		return dao.countAll();
	}

	public Synchronizable createNewEntity(String id) {
		//TODO handle  with not null constraint values
		ClientFilter entity = new ClientFilter();
		entity.setId(id);
		entity.setModified(new Date());
		entity.setCreatedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		entity.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return entity;
	}

	public void delete(Synchronizable entity, SynchronizableUser user) {
		dao.delete(entity);
	}

	public void deleteEntities() {
		dao.deleteEntities();
	}

	public List<Synchronizable> loadEntities(SynchronizableUser user) {
		return dao.loadEntities();
	}
	
	public List<Synchronizable> loadEntities(String cardEntity, String entityField) {
		
		ImogJunction junction = new ImogConjunction();
		
		BasicCriteria entityCriteria = new BasicCriteria();
		entityCriteria.setOperation(CriteriaConstants.STRING_OPERATOR_EQUAL);
		entityCriteria.setField("cardEntity");
		entityCriteria.setValue(cardEntity);
		junction.add(entityCriteria);
		
		BasicCriteria fieldCriteria = new BasicCriteria();
		fieldCriteria.setOperation(CriteriaConstants.STRING_OPERATOR_EQUAL);
		fieldCriteria.setField("entityField");
		fieldCriteria.setValue(entityField);
		junction.add(fieldCriteria);
		
		
		return dao.loadEntities(junction);
	}

	/**
	 * Used to get ClientFilterForList entities by pages
	 * @param startRow page start row
	 * @param maxRows page max number of rows
	 * @param sortProperty property used for entity list sorting
	 * @param sortOrder true if ascending
	 * @param user
	 * @return ClientFilterForList entities between startRow and (startRow + maxRows)
	 */
	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder, SynchronizableUser user) {
		return dao.loadEntities(startRow, maxRows, sortProperty, sortOrder);
	}

	public Synchronizable loadEntity(String entityId, SynchronizableUser user) {
		return dao.loadEntity(entityId);
	}
	
	public List<Synchronizable> loadEntities(SynchronizableUser user, String terminalId) {
		return dao.loadEntities();
	}

	public List<Synchronizable> loadModified(Date date,	SynchronizableUser user, String terminalId) {
		return dao.loadModified(date, (ImogJunction) null);
	}

	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {
		return dao.loadModified(date, (ImogJunction) null);
	}

	public Synchronizable loadModified(String entityId, Date date,
			SynchronizableUser user) {
		return dao.loadModified(date, entityId);
	}

	public void merge(Synchronizable entity, SynchronizableUser user) {
		dao.merge(entity);

		/* notify listeners */
		// TODO: should we notify when it is a fake entity ?
		synchronized (listeners) {
			for (EntityListener listener : listeners)
				listener.entitySavedOrUpdated(entity);
		}
	}

	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {
		dao.saveOrUpdate(entity);

		/* notify listeners */
		// TODO: should we notify when it is a fake entity ?
		synchronized (listeners) {
			for (EntityListener listener : listeners)
				listener.entitySavedOrUpdated(entity);
		}
	}

	/*
	 *
	 */
	public synchronized void addListener(EntityListener listener) {
		listeners.add(listener);
	}

	/*
	 *
	 */
	public synchronized void removeListener(EntityListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Setter for bean injection
	 * @param dao
	 */
	public void setDao(EntityDao dao) {
		this.dao = dao;
	}

	public EntityDao getDao() {
		return dao;
	}
	
	public void setI18nDao(LocalizedTextDao arg0) {
		// not relevant for ClientFilter
	}


	
	

}
