package org.imogene.sync.client.localizedtext;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.sync.client.EntityListener;
import org.imogene.sync.localizedtext.LocalizedText;

/**
 * Implements a data handler for the LocalizedText 
 * @author Medes-IMPS
 */
public class LocalizedTextHandler implements EntityHandler {

	private static Set<EntityListener> listeners = new HashSet<EntityListener>();

	private EntityDao dao;

	public int countAll() {
		return dao.countAll();
	}

	public Synchronizable createNewEntity(String id) {
		//TODO handle  with not null constraint values
		LocalizedText entity = new LocalizedText();
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

	/**
	 * Used to get LocalizedTextForList entities by pages
	 * @param startRow page start row
	 * @param maxRows page max number of rows
	 * @param sortProperty property used for entity list sorting
	 * @param sortOrder true if ascending
	 * @param user
	 * @return LocalizedTextForList entities between startRow and (startRow + maxRows)
	 */
	public List<Synchronizable> loadEntities(int startRow, int maxRows,
			String sortProperty, boolean sortOrder, SynchronizableUser user) {
		return dao.loadEntities(startRow, maxRows, sortProperty, sortOrder);
	}

	public Synchronizable loadEntity(String entityId, SynchronizableUser user) {
		return dao.loadEntity(entityId);
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

	@Override
	public List<Synchronizable> loadEntities(SynchronizableUser user,
			String terminalId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Synchronizable> loadModified(Date date,
			SynchronizableUser user, String terminalId) {
		// TODO Auto-generated method stub
		return null;
	}

}
