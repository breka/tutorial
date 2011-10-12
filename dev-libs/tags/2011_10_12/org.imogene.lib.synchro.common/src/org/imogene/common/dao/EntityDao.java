package org.imogene.common.dao;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.criteria.ImogJunction;
import org.imogene.common.data.Synchronizable;


/**
 * Interface that describes an Entity DAO 
 * that enables to load and store an entity.
 * @author MEDES-IMPS
 */
public interface EntityDao {

	/**
	 * Load a Entity from the database
	 * @param id the Entity id
	 * @return Entity or null if it is not in the database
	 */
	public Synchronizable loadEntity(String entityId);
	
	/**
	 * Store or update an entity in the database
	 * @param object the entity to save
	 */
	public void saveOrUpdate(Synchronizable entity);
	
	/**
	 * Merges an entity in the database
	 * @param object the entity to merge
	 */
	public void merge(Synchronizable entity);
	
	/**
	 * Load the Entities that meet search criterions	
	 * @param criterions search criterions
	 * @return the Entities that meet search criterions
	 */
	public List<Synchronizable> loadEntities(ImogJunction criterions);
	
	/**
	 * Load all the Entities that are present in the database	
	 * @return List of all the Entity
	 */
	public List<Synchronizable> loadEntities();
	
	
	/**
	 * 
	 * @param startRow page start row
	 * @param maxRows page max number of rows
	 * @param sortProperty property used for entity list sorting
	 * @param sortOrder true if ascending
	 * @return the entities between startRow and startRow + maxRows
	 */
	public List<Synchronizable> loadEntities(int startRow, int maxRows, String sortProperty, boolean sortOrder);
	
	
	/**
	 * Load the Entities modified since the specified date that meet search criterions
	 * @param date the modification date
	 * @param criterions search criterions
	 * @return the Entities modified since the specified date that meet search criterions
	 */
	public List<Synchronizable> loadModified(Date date, ImogJunction criterions);
	
	/**
	 * Load the Entities modified since the specified date.
	 * @param date the modification date
	 * @return the Entities modified since the specified date
	 */
	public List<Synchronizable> loadModified(Date date);
	
	/**
	 * Load an Entity modified since the specified date.
	 * @param date the modification date
	 * @param entityId the id of the entity whose modification is looked for
	 * @result the Entity modified since the specified date, if not 
	 * modified, returns null
	 */
	public Synchronizable loadModified(Date date, String entityId);
	
	/**
	 * Load an Entity modified since the specified date meeting search criterions
	 * @param date the modification date
	 * @param criterions search criterions
	 * @param entityId the id of the entity whose modification is looked for
	 * @result The Entity modified since the specified date, if not 
	 * modified, returns null
	 */
	public Synchronizable loadModified(Date date, ImogJunction criterions, String entityId);
			
	/**
	 * Deletes one entity
	 * @param entity the entity to be deleted
	 */
	public void delete(Synchronizable entity);	
	
	/**
	 * Counts the number of entities
	 * @return the total number of entities
	 */
	public int countAll();
	
	/**
	 * Clear objects in DAO cache
	 */
	public void clear();
	
	/**
	 * Flush objects in DAO cache
	 */
	public void flush();
	
	/**
	 * For tests: Delete all stored entities
	 */
	public void deleteEntities();
	
}
