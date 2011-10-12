package org.imogene.ws.entity;

import java.util.Date;
import java.util.Set;


/**
 * This interface describe a Medoo actor, which is a 
 * Medoo bean that is an application user too.
 * @author Medes-IMPS
 */
public interface MedooActor extends MedooBean {
 
	/** Get the assigned login for this user */
	public String getLogin();
	
	/** Set the assigned login for this user */
	public void setLogin(String login);

	/** Get the assigned password for this user */
	public String getPassword();

	/** Set the assigned password for this user */
	public void setPassword(String password);

	/** Get the actor last login date */
	public Date getLastLoginDate();

	/** Set the actor last login date */
	public void setLastLoginDate(Date lastLoginDate);
	
	
	/** Get the roles associated to this user */
	public Set<MedooRole> getRoles();

	/** Set the roles associated to this user */
	public void setRoles(Set<MedooRole> pRoles);
	
	/** Get the synchronizable entities associated to this user */
	public Set<SynchronizableEntity> getSynchronizables();
	
	/** Set the synchronizable entities associated to this user **/
	public void setSynchronizables(Set<SynchronizableEntity> syncs);

	/** Get the local ISO code used by this actor */
	public String getNotificationLocale();

	/** Set the local ISO code used by this actor */
	public void setNotificationLocale(String iso);
	
	/** get the default method used to notify this actor */
	public Integer getNotificationMethod();

	/** Set the default method used to notify this actor */
	public void setNotificationMethod(Integer method);	

	/** Is the notification activated for this user */
	public Boolean getBeNotified();

	/** Set if the notification is activated for this user */
	public void setBeNotified(Boolean notify);

 

}
