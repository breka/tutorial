package org.imogene.web.server.dao;

import java.util.List;

/**
 * Generic dao 
 * @author Medes-IMPS 
 */
public interface GenericDao {
	
	/**
	 * Saves or updates the specified entity
	 * @param entity the entity to be saved or to updated
	 */
	public void saveOrUpdate(Object entity);
	
	/**
	 * Loads an entity
	 * @param entityClass the entity class
	 * @param id the entity id
	 * @return the entity if it exists
	 */
	public Object loadEntity(Class<?> entityClass, String id);
	
	
	/**
	 * Gets the list of entities for the specified class
	 * @param entityClass the entity class
	 * @param count max number of results
	 * @return the list of entities
	 */
	public List<?> listBeans(Class<?> entityClass);
	
	
	/**
	 * Evicts an entity from hibernate session
	 * @param entity the entity to be evicted from hibernate session
	 */
	public void evictEntity(Object entity);

}
