package org.imogene.lib.common.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.imogene.lib.common.role.ImogRole;
import org.imogene.lib.common.sync.entity.SynchronizableEntity;

/**
 * This interface describes an Imogene actor, which is an Imogene bean that is
 * an application user too.
 * 
 * @author Medes-IMPS
 */
public abstract class ImogActor extends ImogBeanImpl {

	/** Get the assigned login for this user */
	public abstract String getLogin();

	/** Set the assigned login for this user */
	public abstract void setLogin(String login);

	/** Get the assigned password for this user */
	public abstract String getPassword();

	/** Set the assigned password for this user */
	public abstract void setPassword(String password);

	/** Get the actor last login date */
	public abstract Date getLastLoginDate();

	/** Set the actor last login date */
	public abstract void setLastLoginDate(Date lastLoginDate);

	/** Get the roles associated to this user */
	public abstract List<ImogRole> getRoles();

	/** Set the roles associated to this user */
	public abstract void setRoles(List<ImogRole> pRoles);

	/**
	 * Add a role to the user assigned list of roles
	 * 
	 * @param role a role
	 */
	public abstract void addRole(ImogRole role);

	/** Get the synchronizable entities associated to this user */
	public abstract List<SynchronizableEntity> getSynchronizables();

	/** Set the synchronizable entities associated to this user **/
	public abstract void setSynchronizables(List<SynchronizableEntity> syncs);

	/**
	 * Add an entity type to the list of entity types that the user can
	 * synchronize
	 * 
	 * @param synchronizable an entity type
	 */
	public abstract void addSynchronizable(SynchronizableEntity synchronizable);
	
	public abstract Map<String, String> getNotificationData();

	public abstract void setNotificationData(Map<String, String> notificationData);

	/** Get the local ISO code used by this actor */
	public abstract String getNotificationLocale();

	/** Set the local ISO code used by this actor */
	public abstract void setNotificationLocale(String iso);

	/** get the default method used to notify this actor */
	public abstract Integer getNotificationMethod();

	/** Set the default method used to notify this actor */
	public abstract void setNotificationMethod(Integer method);

	/**
	 * Get the notification data (ie: phone number, email); for the specified
	 * notification method.
	 * 
	 * @param method the notification method
	 * @return the data associated with the method
	 */
	public abstract String getNotificationDataMethodName(String method);

	/** Is the notification activated for this user */
	public abstract Boolean getBeNotified();

	/** Set if the notification is activated for this user */
	public abstract void setBeNotified(Boolean notify);

	/**
	 * Check if the specified role is already assigned to the user
	 * 
	 * @param id the role id
	 * @return true if the role is already assigned
	 */
	public abstract boolean isAssignedRole(String id);
}
