package org.imogene.testsynchro.handler;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.common.data.handler.EntityHandler;
import org.imogene.sync.SyncConstants;
import org.imogene.testsynchro.entity.Patient;



public class PatientHandler implements EntityHandler {

	private EntityDao dao;
	
	public int countAll() {		
		return dao.countAll();
	}

	public Synchronizable createNewEntity(String id) {
		Patient d = new Patient();		
		d.setId(id);
		d.setModified(new Date());
		d.setModifiedBy(SyncConstants.SYNC_ID_SYS);
		d.setModifiedFrom(SyncConstants.SYNC_ID_SYS);
		return d;		
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

	public String toString(Synchronizable entity) {
		return dao.toString();
	}
	
	public void setDao(EntityDao dao){
		this.dao = dao;
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

}
