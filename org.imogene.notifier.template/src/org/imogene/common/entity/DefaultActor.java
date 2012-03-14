package org.imogene.common.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.imogene.notifier.server.Constants;



public class DefaultActor implements ImogActor {
	
	/* Imog bean fields */
	private String id;
	private Date creationDate;
	private String creator;
	private Date lastModificationDate;
	private String modifier;
	private String modifiedFrom;
	private Date uploadDate;
	
	/* Imog actor fields */
	public static String ENTITY_NAME = "DefaultActor";
	private String login;
	private String password;
	private String notifLocale;
	private Integer defaultNotificationMethod;
	private Boolean beNotified;
	private Set<ImogRole> assignedRoles = new HashSet<ImogRole>();
	/* Notification data (one per method) Key = String for method, value = getter For data.
	 * See the NotificationMethod enumerated values */
	private HashMap<String, String> notificationData = new HashMap<String, String>();	

	/* Entity fields */

	/* Identification group fields */
	private String name;
	private String firstName;

	public DefaultActor() {
		// Add the ENABLED keyword before 'START' to protect this area 
		/*PROTECTED REGION ID(initDOC) START*/
		/*PROTECTED REGION END*/
	}

	/* Getters for Imog bean fields */

	public String getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getCreator() {
		return creator;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public String getModifier() {
		return modifier;
	}

	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	/* Setters for Imog bean fields */

	public void setId(String pId) {
		id = pId;
	}

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setCreator(String pCreator) {
		creator = pCreator;
	}

	public void setLastModificationDate(Date date) {
		lastModificationDate = date;
	}

	public void setModifier(String pModifier) {
		modifier = pModifier;
	}

	public void setModifiedFrom(String terminal) {
		modifiedFrom = terminal;
	}

	public void setUploadDate(Date date) {
		uploadDate = date;
	}

	/* Getters and Setters for Entity fields */

	/* Getters and Setters for Identification group fields */
	public String getName() {
		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String value) {
		firstName = value;
	}

	public String getDisplayValue() {
		return id;
	}
	
	/* Getters and Setters for Actor fields */
	
	/** 
	 * Get the assigned roles for this actor. 
	 * @return a list of role names (EMPTY_LIST if none). 	  
	 */
	public Set<ImogRole> getRoles() {
		return assignedRoles;
	}

	/**
	 * Set the list of roles assigned to this actor. 	 
	 * @param roles the list of roles assigned. 
	 */
	public void setRoles(Set<ImogRole> roles) {
		assignedRoles = roles;
	}

	/** 
	 * Get this actor login.
	 *@return the actor login
	 */
	public String getLogin() {
		return login;
	}

	/** 
	 * Get the assigned password for this actor.
	 *@return the actor password 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the actor login.
	 *@param pLogin the actor login.
	 */
	public void setLogin(String pLogin) {
		login = pLogin;
	}

	/**
	 * set the actor password
	 *@param pPassword the actor password 
	 */
	public void setPassword(String pPassword) {
		password = pPassword;
	}

	/** 
	 * Get the notification locale ISO code to use for this actor
	 *@return the locale ISO code
	 */
	public String getNotificationLocale() {
		return notifLocale;
	}

	/** 
	 * Set the notification locale ISO code
	 *@param pLocale the locale ISO code
	 */
	public void setNotificationLocale(String locale) {
		notifLocale = locale;
	}

	/** 
	 * Get the notification method to use (if none : NO_METHOD). 
	 * @return the notification method id
	 */
	public Integer getNotificationMethod() {
		return defaultNotificationMethod;
	}

	/**
	 * Set the notification method to use.
	 *@param method the notification method
	 */
	public void setNotificationMethod(Integer method) {
		defaultNotificationMethod = method;
	}

	/**
	 * get th notification data (ie: phone number, email) 
	 * for the specified notification method.
	 * @param method the notification method
	 * @return the data associated with the method
	 */
	public String getNotificationDataMethodName(String method) {
		return (String) notificationData.get(method);
	}

	/** 
	 * This actor can be notified ?
	 *@return true if actor can be notified.
	 */
	public Boolean getBeNotified() {
		return beNotified;
	}
	
	/**
	 * Set if this actor can be notified
	 *@param notif true if it can be notified
	 */
	public void setBeNotified(Boolean notif) {
		beNotified = notif;
	}

	@Override
	public String getNotificationData(int method) {
		if(method == Constants.SMSMethod)
			return "0678664038";
		if(method == Constants.MailMethod)
			return "ylapeyre@gmail.com";
		return null;
	}
	
	
	
	
}
