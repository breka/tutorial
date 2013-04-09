package org.imogene.notifier.server.common;

import java.util.Set;

import org.imogene.common.entity.ImogRole;


/**
 * Interface that describes an Imogene notification
 * @author Medes-IMPS 
 */
public interface Notification {

	/** notification id */
	public String getId();
	
	public void setId(String pId);
	
	
	/** notification title */	
	public String getTitle();
	
	public void setTitle(String title);
		
	
	/** notification name */	
	public String getName();
	
	public void setName(String pName);
	
	
	/** notification message */
	public String getMessage();
	
	public void setMessage(String pMessage);
	
	
	/** source card */	
	public String getSourceCard();
	
	public void setSourceCard(String pSource);
	
	
	/** comma separated list of user is */
	public String getUserRecipients();
	
	public void setUserRecipients(String users);
	
	
	/** roles to be notified */
	public Set<ImogRole> getRoleRecipients();
	
	public void setRoleRecipients(Set<ImogRole> roles);
	
	
	/** card operation */
	public String getOperation();
	
	public void setOperation(String operation);
	
	
	/** notification method */
	public Integer getMethod();
	
	public void setMethod(Integer pMethod);
}
