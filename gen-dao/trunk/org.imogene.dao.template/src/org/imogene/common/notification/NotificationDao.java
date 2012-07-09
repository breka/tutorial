package org.imogene.common.notification;

import java.util.List;

import org.imogene.common.criteria.ImogCriterion;

public interface NotificationDao {

	/**
	 * Load all the notifications
	 * 
	 * @return list of all the notifications in the database
	 */
	public Notification load(String id);

	public List<Notification> load(String sourceCard, String operation);

	/**
	 * List notifications
	 * 
	 * @param first first index to retrieve
	 * @param max nb of items to retrieve
	 * @param property the property used to sort the collection
	 * @param sortOrder true for an ascendant sort
	 * @param criterion request criteria
	 * @return list of notifications
	 */
	public List<Notification> load(int first, int max, String property, boolean sortOrder, ImogCriterion criterion);

	/**
	 * Count number of notifications in the database
	 * 
	 * @return the count
	 */
	public long count();

	/**
	 * Count number of notifications in the database, that matches the criteria
	 * 
	 * @return the count
	 */
	public long count(ImogCriterion criterion);

	/**
	 * Save or update the notification
	 * 
	 * @param notification the notification to save or update
	 */
	public void saveOrUpdate(Notification notification);
}
