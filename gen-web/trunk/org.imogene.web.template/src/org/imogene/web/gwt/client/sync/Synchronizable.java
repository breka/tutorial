package org.imogene.web.gwt.client.sync;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This interface describes a synchronizable entity
 * @author MEDES-IMPS
 */
public interface Synchronizable extends IsSerializable {
	
	/**
	 * return the id of the entity
	 * @return The entity id
	 */
	public String getId();
	
	/**
	 * Set the entity id
	 * @param id the entity id
	 */
	public void setId(String id);
	
	/**
	 * Get the last modification date
	 * @return The last modification date
	 */
	public Date getModified();
	
	/**
	 * Set the last modification date
	 * @param modified The last modification date
	 */
	public void setModified(Date modified);
	
	/**
	 * Get the last upload date
	 * @return The last upload date
	 */
	public Date getUploadDate();
	
	/**
	 * Set the last upload date
	 * @param modified The last upload date
	 */
	public void setUploadDate(Date date);
	
	/**
	 * Get the id of the last update user 
	 * that modify this entity
	 * @return The user id
	 */
	public String getModifiedBy();
	
	/**
	 * Set the id of the last update user 
	 * that modified this entity
	 * @param id The user id
	 */
	public void setModifiedBy(String id);
	
	/**
	 * Get the terminal id of the last update session
	 * that modify this entity
	 * @return The user id
	 */
	public String getModifiedFrom();
	
	/**
	 * Set the terminal id of the last update session 
	 * that modified this entity
	 * @param id The user id
	 */
	public void setModifiedFrom(String id);
	

	/**
	 * Get the id of the last user 
	 * that modify this entity
	 * @return The user id
	 */
	public String getCreatedBy();
	
	/**
	 * Set the id of the user
	 * that created this entity
	 * @param id The user id
	 */
	public void setCreatedBy(String id);
	
	/**
	 * Get the entity creation date
	 * @return The user id
	 */
	public Date getCreated();
	
	/**
	 * Set the entity creation date
	 * @param id The user id
	 */
	public void setCreated(Date created);
	
	
	/**
	 * Get a string representation of the entity
	 * @return a string representation of the entity
	 */
	public String getDisplayValue();
	
}
