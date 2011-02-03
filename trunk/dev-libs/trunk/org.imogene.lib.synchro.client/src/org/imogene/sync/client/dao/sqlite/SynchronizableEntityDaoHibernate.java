package org.imogene.sync.client.dao.sqlite;

import java.util.List;

import org.imogene.common.data.Synchronizable;
import org.imogene.sync.client.dao.SynchronizableEntityDao;
import org.imogene.uao.synchronizable.SynchronizableEntity;


public class SynchronizableEntityDaoHibernate implements SynchronizableEntityDao {

	/* (non-Javadoc)
	 * @see org.imogene.sync.client.dao.SynchronizableEntityDao#delete(org.imogene.uao.synchronizable.SynchronizableEntity)
	 */
	public void delete(SynchronizableEntity entity) {	
				
		HibernateGenericDao.delete(entity);			
	}

	/* (non-Javadoc)
	 * @see org.imogene.sync.client.dao.SynchronizableEntityDao#load(java.lang.String)
	 */
	public SynchronizableEntity load(String id) {		
		SynchronizableEntity result = (SynchronizableEntity)HibernateGenericDao.loadEntity(SynchronizableEntity.class, id);				
		return result;
	}

	/* (non-Javadoc)
	 * @see org.imogene.sync.client.dao.SynchronizableEntityDao#loadAll()
	 */
	public List<Synchronizable> loadAll() {				
		List<Synchronizable> result = HibernateGenericDao.loadEntities(SynchronizableEntity.class);				
		return result;
	}

	/* (non-Javadoc)
	 * @see org.imogene.sync.client.dao.SynchronizableEntityDao#store(org.imogene.uao.synchronizable.SynchronizableEntity)
	 */
	public void store(SynchronizableEntity entity) {		
		HibernateGenericDao.saveOrUpdate(entity);					
	}
	
	
}
