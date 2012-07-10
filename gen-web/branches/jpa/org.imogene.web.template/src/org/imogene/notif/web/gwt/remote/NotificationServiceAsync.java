package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.common.criteria.ImogJunction;
import org.imogene.notif.web.gwt.client.NotificationTemplate;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NotificationServiceAsync {

	/* biology services */

	/**
	 * Load the entity with the specified id
	 * 
	 * @param entityId
	 *            the entity id
	 * @param callback
	 *            the result call back
	 */
	public void getNotification(String entityId,
			AsyncCallback<NotificationTemplate> callback);

	/**
	 * List entities of type notification template
	 * 
	 * @param i
	 *            first index to retrieve
	 * @param j
	 *            nb of items to retrieve
	 * @param sortProperty
	 *            the property used to sort the collection
	 * @param sortOrder
	 *            true for an ascendant sort
	 * @param callback
	 *            the result call back
	 */
	public void listNotification(int i, int j, String sortProperty,
			boolean sortOrder,
			AsyncCallback<List<NotificationTemplate>> callback);

	/**
	 * List entities of type notification template
	 * 
	 * @param i
	 *            first index to retrieve
	 * @param j
	 *            nb of items to retrieve
	 * @param criterion
	 *            request criteria
	 * @param sortProperty
	 *            the property used to sort the collection
	 * @param sortOrder
	 *            true for an ascendant sort
	 * @param callback
	 *            the result call back
	 */
	public void listNotification(int i, int j, ImogJunction criterion,
			String property, boolean sortOrder,
			AsyncCallback<List<NotificationTemplate>> callback);

	/**
	 * Count number of notification template in the database
	 * 
	 * @param callback
	 *            the result call back
	 */
	public void countNotification(AsyncCallback<Integer> callback);

	/**
	 * Count number of notification template in the database, that matches the
	 * criteria
	 * 
	 * @param criterion
	 *            the criteria parameters
	 * @param callback
	 *            the result call back
	 */
	public void countNotification(ImogJunction criterion,
			AsyncCallback<Integer> callback);

	/**
	 * Save or update the notification template
	 * 
	 * @param entity
	 *            the notification to save or update
	 * @param isNew
	 *            true if it is a new entity added for the first time.
	 * @param callback
	 *            the result call back
	 */
	public void saveOrUpdate(NotificationTemplate entity, boolean isNew,
			AsyncCallback<Void> callback);

}
