package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.notif.web.gwt.client.NotificationTemplate;
import org.imogene.web.gwt.common.criteria.ImogJunction;

import com.google.gwt.user.client.rpc.RemoteService;


/**
 *
 */
public interface NotificationService extends RemoteService {

	/* biology services */

	/**
	 * Load the entity with the specified id
	 *@param entityId the entity id
	 *@return the entity or null
	 */
	public NotificationTemplate getNotification(String id);

	/**
	 * List entities of type Biology
	 *@param i first index to retrieve
	 *@param j nb of items to retrieve
	 *@param sortProperty the property used to sort the collection
	 *@param sortOrder true for an ascendant sort
	 *@return list of biology
	 */
	public List<NotificationTemplate> listNotification(int i, int j, String sortProperty,
			boolean sortOrder);

	/**
	 * List entities of type Biology
	 *@param i first index to retrieve
	 *@param j nb of items to retrieve
	 *@param criterion request criteria
	 *@param sortProperty the property used to sort the collection
	 *@param sortOrder true for an ascendant sort
	 *@return list of biology
	 */
	public List<NotificationTemplate> listNotification(int i, int j, ImogJunction criterion,
			String property, boolean sortOrder);

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
	 *Save or update the entity
	 *@param entity the entity to save or update
	 *@param isNew true if it is a new entity added for the first time.
	 */
	public void saveOrUpdate(NotificationTemplate entity, boolean isNew);
	

}
