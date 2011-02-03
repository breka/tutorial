package org.imogene.notifier.server.common;

import java.util.Set;

import org.imogene.common.entity.ImogActor;
import org.imogene.common.entity.ImogRole;


public class NotificationInstance {	
	
	private String id;
	
	private String message;
	
	private String name;
	
	private String sourceCard;
	
	private String title;
	
	private Integer method;
	
	private Set<ImogRole> roleRecipients;
	
	private Set<ImogActor> userRecipients;
		
		
	
	public String getId() {		
		return id;
	}


	public String getMessage() {		
		return message;
	}


	public String getName() {		
		return name;
	}
	
	public Integer getMethod(){
		return method;
	}

	
	public Set<ImogRole> getRoleRecipients() {	
		return roleRecipients;
	}

	
	public String getSourceCard() {		
		return sourceCard;
	}

	
	public Set<ImogActor> getUserRecipients() {		
		return userRecipients;
	}

	
	public void setId(String pId) {
		id=pId;		
	}

	
	public void setMessage(String pMessage) {
		message = pMessage;		
	}

	
	public void setName(String pName) {
		name = pName;		
	}

	
	public void setRoleRecipients(Set<ImogRole> roles) {
		roleRecipients = roles;
	}

	
	public void setSourceCard(String pSource) {
		sourceCard = pSource;
		
	}

	public void setMethod(Integer pMethod){
		method = pMethod;
	}
	
	public void setUserRecipients(Set<ImogActor> users) {
		userRecipients = users;		
	}

	
	public String getTitle() {
		return title;
	}

	
	public void setTile(String pTitle) {
		title = pTitle;
	}		

}
