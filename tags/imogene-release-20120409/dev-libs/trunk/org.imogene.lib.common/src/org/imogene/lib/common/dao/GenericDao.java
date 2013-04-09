package org.imogene.lib.common.dao;

import java.util.List;

public interface GenericDao {
	
	public <T> List<T> load(Class<T> clazz);

	public <T> T load(Class<T> clazz, String id);

	public Object load(String className, String id);
	
	public void evict(Object o);
	
	public <T> void saveOrUpdate(T object);
	
	public <T> long count(Class<T> clazz);

}
