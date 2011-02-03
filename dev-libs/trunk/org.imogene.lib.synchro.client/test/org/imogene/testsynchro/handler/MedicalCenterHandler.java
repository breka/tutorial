package org.imogene.testsynchro.handler;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.testsynchro.dao.hibernate.MedicalCenterDao;
import org.imogene.testsynchro.entity.MedicalCenter;


public class MedicalCenterHandler implements EntityHandler {



	private MedicalCenterDao dao;
	
	public int countAll() {		
		return dao.countAll();
	}

	public Synchronizable createNewEntity(String id) {
		MedicalCenter entity = new MedicalCenter();
		entity.setId(id);
		entity.setModified(new Date());
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

	public EntityDao getDao() {
		return dao;
	}

	public List<Synchronizable> loadEntities(SynchronizableUser user) {
		
		return dao.loadEntities();
	}

	public Synchronizable loadEntity(String entityId, SynchronizableUser user) {
		
		return dao.loadEntity(entityId);
	}

	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {
		
		return dao.loadModified(date, (ImogJunction)null);
	}

	public Synchronizable loadModified(String entityId, Date date,
			SynchronizableUser user) {
		
		return dao.loadModified(date, entityId);
	}

	public void merge(Synchronizable entity, SynchronizableUser user) {
		dao.merge(entity);
		
	}

	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {
		dao.saveOrUpdate(entity);
		
	}
	
	public List<Synchronizable> loadEntities(SynchronizableUser arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Synchronizable> loadModified(Date arg0, SynchronizableUser arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Synchronizable> loadEntities(int arg0, int arg1, String arg2,
			boolean arg3, SynchronizableUser arg4) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 */
	public String toString(Synchronizable entity) {
		
		return dao.toString();
	}
	
	/**
	 * 
	 * @param dao
	 */
	public void setDao(MedicalCenterDao dao){
		this.dao = dao;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#setDao(org.imogene.dao.EntityDao)
	 */
	public void setDao(EntityDao dao){
		this.dao = (MedicalCenterDao)dao;
	}
}
