package org.imogene.common.data.handler;

import java.util.Date;
import java.util.List;

import org.imogene.common.dao.EntityDao;
import org.imogene.common.dao.LocalizedTextDao;
import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;


/**
 * Interface that describes an Entity manager 
 * that enables to load and store an entity
 * after data access permission checking.
 * @author MEDES-IMPS
 */
public interface EntityHandler {

	/**
	 * Load a Entity from the database
	 * @param entityId the Entity id
	 * @param user the user which is performing the data access
	 * @return Entity or null if it is not in the database
	 */
	public Synchronizable loadEntity(String entityId, SynchronizableUser user);
	
	/**
	 * Store or update an entity in the database
	 * @param entity the entity to save
	 * @param user the user which is performing the data access
	 */
	public void saveOrUpdate(Synchronizable entity, SynchronizableUser user);
	
	/**
	 * Merge an entity in the database
	 * @param entity the entity to merge
	 * @param user the user which is performing the data access
	 */
	public void merge(Synchronizable entity, SynchronizableUser user);
	
	/**
	 * Load the Entities that are present in the database	
	 * @param user the user which is performing the data access
	 * @return List of Entities
	 */
	public List<Synchronizable> loadEntities(SynchronizableUser user);
	
	/**
	 * Gets a list of entities whose fields are filtered depending on
	 * the user privileges and user defined clients filters
	 * @param user current user whose access has to be filtered (if null, no filtering)
	 * @param terminalId Id of the terminal the current user is using
	 * @return list of entities
	 */
	public List<Synchronizable> loadEntities(SynchronizableUser user, String terminalId);	
	
	/**
	 * Load the Entities modified since the specified date depending on
	 * the user privileges 
	 * @param date the modification date
	 * @param user the user which is performing the data access
	 * @return List of Entities modified since the specified date
	 */
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user);
	
	/**
	 * Load the Entities modified since the specified date depending on
	 * the user privileges and user defined clients filters
	 * @param date the modification date
	 * @param user the user which is performing the data access
	 * @param terminalId Id of the terminal the current user is using
	 * @return List of Entities modified since the specified date
	 */
	public List<Synchronizable> loadModified(Date date, SynchronizableUser user, String terminalId);
	
	/**
	 * Load the Entity modified since the specified date.
	 * @param entityId the id of the entity whose modification is looked for
	 * @param date the modification date
	 * @param user the user which is performing the data access
	 * @return the Entity modified since the specified date
	 */
	public Synchronizable loadModified(String entityId, Date date, SynchronizableUser user);
	
	/**
	 * Used to get the entities by pages
	 * @param startRow page start row
	 * @param maxRows page max number of rows
	 * @param sortProperty property used for entity list sorting
	 * @param sortOrder true if ascending
	 * @param user
	 * @return entities between startRow and (startRow + maxRows)
	 */
	public List<Synchronizable> loadEntities(int startRow, int maxRows,	String sortProperty, boolean sortOrder, SynchronizableUser user);	
	
	/**
	 * Create an empty entity with this id,
	 * waiting for the update incoming in this synchronization session.
	 * @param id the Entity id
	 * @return the new entity created
	 */
	public Synchronizable createNewEntity(String id);
	
	/**
	 * Get the DAO used to access data
	 * To be used only if no data access control
	 * is needed
	 * @return the DAO used to access data
	 */
	public EntityDao getDao();
	
	/**
	 * Setter for bean injection
	 * @param dao the dao for the entity access
	 */
	public void setDao(EntityDao dao);
	

	/**
	 * Setter for bean injection
	 * @param i18nDao the dao for LocalizedText access
	 */
	public void setI18nDao(LocalizedTextDao i18nDao);
	
	/**
	 * Deletes one entity
	 * @param user the user which is performing the action
	 * @param entity the entity to be deleted
	 */
	public void delete(Synchronizable entity, SynchronizableUser user);		
	
	/**
	 * Counts the number of entities
	 * @return the total number of entities
	 */
	public int countAll();
	
	/**
	 * For tests: Delete all entities
	 */
	public void deleteEntities();
	
}
