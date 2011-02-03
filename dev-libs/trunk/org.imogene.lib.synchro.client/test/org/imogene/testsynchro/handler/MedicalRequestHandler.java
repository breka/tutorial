package org.imogene.testsynchro.handler;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.testsynchro.entity.MedicalRequest;


public class MedicalRequestHandler implements EntityHandler {

	private EntityDao dao;
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#countAll()
	 */
	public int countAll() {		
		return dao.countAll();
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#createNewEntity(java.lang.String)
	 */
	public Synchronizable createNewEntity(String id) {	
		MedicalRequest d = new MedicalRequest();		
		d.setId(id);
		d.setModified(new Date());
		d.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		d.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return d;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#delete(org.imogene.data.Synchronizable, org.imogene.data.SynchronizableUser)
	 */
	public void delete(Synchronizable entity, SynchronizableUser user) {
		dao.delete(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#deleteEntities()
	 */
	public void deleteEntities() {
		dao.deleteEntities();
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#getDao()
	 */
	public EntityDao getDao() {		
		return dao;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#loadEntities(org.imogene.data.SynchronizableUser)
	 */
	public List<Synchronizable> loadEntities(SynchronizableUser user) {		
		return dao.loadEntities();
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#loadEntity(java.lang.String, org.imogene.data.SynchronizableUser)
	 */
	public Synchronizable loadEntity(String entityId, SynchronizableUser user) {		
		return dao.loadEntity(entityId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#loadModified(java.util.Date, org.imogene.data.SynchronizableUser)
	 */
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user) {		
		return dao.loadModified(date, (ImogJunction)null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#loadModified(java.lang.String, java.util.Date, org.imogene.data.SynchronizableUser)
	 */
	public Synchronizable loadModified(String entityId, Date date,
			SynchronizableUser user) {		
		return dao.loadModified(date, entityId);
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

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#merge(org.imogene.data.Synchronizable, org.imogene.data.SynchronizableUser)
	 */
	public void merge(Synchronizable entity, SynchronizableUser user) {
		dao.merge(entity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#saveOrUpdate(org.imogene.data.Synchronizable, org.imogene.data.SynchronizableUser)
	 */
	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user) {
		dao.saveOrUpdate(entity);

	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.data.handler.EntityHandler#toString(org.imogene.data.Synchronizable)
	 */
	public String toString(Synchronizable entity) {		
		return dao.toString();
	}
	
	/**
	 * 
	 * @param dao
	 */
	public void setDao(EntityDao dao){
		this.dao = dao;
	}

}
