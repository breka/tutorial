package org.imogene.sync.client.dao.sqlite;

import java.util.List;

import org.imogene.common.data.Synchronizable;
import org.imogene.sync.client.SyncParameters;
import org.imogene.sync.client.dao.SyncParametersDao;


public class SyncParameterDaoHibernate implements SyncParametersDao {
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.SyncParametersDao#load()
	 */
	public SyncParameters load() {		
		List<Synchronizable> result = HibernateGenericDao.loadEntities(SyncParameters.class);				
		if(result != null)
			return (SyncParameters)result.get(0);
		return null;		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.dao.SyncParametersDao#store(org.imogene.sync.SyncParameters)
	 */
	public void store(SyncParameters parameters) {						
		HibernateGenericDao.saveOrUpdate(parameters);		
	}				
	
}
