package org.imogene.sync.client.dao;

import java.util.List;

import org.imogene.common.data.Synchronizable;
import org.imogene.uao.synchronizable.SynchronizableEntity;


public interface SynchronizableEntityDao {

	
	public SynchronizableEntity load(String id);
	
	public List<Synchronizable> loadAll();
	
	public void store(SynchronizableEntity entity);
	
	public void delete(SynchronizableEntity entity);
	
}
