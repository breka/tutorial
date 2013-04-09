package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.notif.web.gwt.client.NotificationTemplate;
import org.imogene.web.gwt.client.Constants;
import org.imogene.web.gwt.common.criteria.ImogJunction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class NotificationServiceFacade {

	private static NotificationServiceFacade instance;

	private NotificationServiceAsync proxy;

	/**
	 * Private constructor
	 */
	private NotificationServiceFacade() {
		proxy = (NotificationServiceAsync) GWT.create(NotificationService.class);

		ServiceDefTarget target = (ServiceDefTarget) proxy;
		target.setServiceEntryPoint(GWT.getHostPageBaseURL() + "/notif.serv");
	}

	/**
	 * Instance accessor
	 */
	public static NotificationServiceFacade getInstance() {
		if (instance == null) {
			instance = new NotificationServiceFacade();
		}
		return instance;
	}

	/* biology services */

	/**
	 * Load the entity with the specified id
	 *@param entityId the entity id
	 *@param callback the result call back
	 */
	public void getNotification(String entityId, AsyncCallback<NotificationTemplate> callback) {
		proxy.getNotification(entityId, callback);
	}

	/**
	 * List entities of type notification template
	 *@param i first index to retrieve
	 *@param j nb of items to retrieve
	 *@param sortProperty the property used to sort the collection
	 *@param sortOrder true for an ascendant sort
	 *@param callback the result call back
	 */
	public void listNotification(int i, int j, String sortProperty,
			boolean sortOrder, AsyncCallback<List<NotificationTemplate>> callback) {
		proxy.listNotification(i, j, sortProperty, sortOrder, callback);
	}

	/**
	 * List entities of type notification template
	 *@param i first index to retrieve
	 *@param j nb of items to retrieve
	 *@prama criterion request criteria
	 *@param sortProperty the property used to sort the collection
	 *@param sortOrder true for an ascendant sort
	 *@param callback the result call back
	 */
	public void listNotification(int i, int j, ImogJunction criterion,
			String property, boolean sortOrder,
			AsyncCallback<List<NotificationTemplate>> callback) {
		proxy.listNotification(i, j, criterion, property, sortOrder, callback);
	}

	/**
	 *Count number of biology in the database
	 *@param callback the result call back
	 */
	public void countNotification(AsyncCallback<Integer> callback) {
		proxy.countNotification(callback);
	}

	/**
	 *Count number of biology in the database, 
	 *that matches the criteria
	 *@param criterion the criteria parameters
	 *@param callback the result call back
	 */
	public void countNotification(ImogJunction criterion,
			AsyncCallback<Integer> callback) {
		proxy.countNotification(criterion, callback);
	}

	/**
	 *Save or update the entity
	 *@param entity the entity to save or update
	 *@param isNew true if it is a new entity added for the first time.
	 *@param callback the result call back
	 */
	public void saveOrUpdate(NotificationTemplate entity, boolean isNew,
			AsyncCallback<Void> callback) {
		proxy.saveOrUpdate(entity, isNew, callback);
	}
	

}
