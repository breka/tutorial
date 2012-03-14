package org.imogene.notif.web.gwt.server.dao;

import java.util.List;

import org.imogene.notif.web.gwt.client.NotificationTemplate;
import org.imogene.web.gwt.common.criteria.ImogJunction;


public interface NotificationDao {
	
	/**
	 * Load the entity with the specified id
	 *@param entityId the entity id
	 *@return the entity or null
	 */
	public NotificationTemplate getNotification(String id);

	/**
	 * Load the entity with the specified id
	 *@param entityId the entity id
	 *@param criterions request criteria
	 *@return the entity or null
	 */
	public NotificationTemplate getNotification(String id, ImogJunction criterions);

	/**
	 * List the notification templates
	 *@param criterion request criteria
	 *@return list of biology
	 */
	public List<NotificationTemplate> listNotification(ImogJunction criterion);

	/**
	 * List the notification templates
	 *@param property the property used to sort
	 *@param asc the sort order
	 *@param criterion request criteria
	 *@return list of biology
	 */
	public List<NotificationTemplate> listNotification(String property, boolean asc,
			ImogJunction criterion);

	/**
	 * List the notification templates
	 *@param first first index to retrieve
	 *@param max nb of items to retrieve
	 *@param sortProperty the property used to sort the collection
	 *@param sortOrder true for an ascendant sort
	 *@return list of biology
	 */
	public List<NotificationTemplate> listNotification(int first, int max, String sortProperty,
			boolean sortOrder);

	/**
	 * List the notification templates
	 *@param first first index to retrieve
	 *@param max nb of items to retrieve	 
	 *@param property the property used to sort the collection
	 *@param sortOrder true for an ascendant sort
	 *@param criterion request criteria
	 *@return list of biology
	 */
	public List<NotificationTemplate> listNotification(int first, int max, String property,
			boolean sortOrder, ImogJunction criterion);

	/**
	 *Count number of biology in the database
	 *@return the count
	 */
	public int countNotification();

	/**
	 *Count number of biology in the database, 
	 *that matches the criteria
	 *@return the count
	 */
	public int countNotification(ImogJunction criterion);

	/**
	 * Delete the specified entity
	 *@param entity the entity to delete
	 */
	public void delete(NotificationTemplate entity);


	/**
	 *Save or update a notification template
	 *@param entity the entity to save or update
	 *@param isNew true if it is a new entity added for the first time.
	 */
	public void saveOrUpdate(NotificationTemplate entity, boolean isNew);
	
}
