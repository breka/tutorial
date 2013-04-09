package org.imogene.notifier.server.common;

import java.util.Set;

import org.imogene.common.entity.ImogRole;


public class NotificationTemplate implements Notification {
	
	private String id;
	
	private String message;
	
	private String name;
	
	private String sourceCard;
	
	private String title;
	
	private String operation;
	
	private String userRecipients;
	
	private Integer method;
	
	private Set<ImogRole> roleRecipients;			

	@Override
	public String getId() {		
		return id;
	}

	@Override
	public String getMessage() {		
		return message;
	}

	@Override
	public String getName() {		
		return name;
	}
	
	@Override
	public Integer getMethod(){
		return method;
	}

	@Override
	public Set<ImogRole> getRoleRecipients() {		
		return roleRecipients;
	}

	@Override
	public String getSourceCard() {		
		return sourceCard;
	}	

	@Override
	public void setId(String pId) {
		id = pId;		
	}

	@Override
	public void setMessage(String pMessage) {
		message = pMessage;
		
	}

	@Override
	public void setName(String pName) {
		name = pName;
		
	}
	
	@Override
	public void setMethod(Integer pMethod){
		method = pMethod;
	}

	@Override
	public void setRoleRecipients(Set<ImogRole> roles) {		
		roleRecipients = roles;
	}

	@Override
	public void setSourceCard(String pSource) {		
		sourceCard = pSource;
	}


	@Override
	public String getTitle() {		
		return title;
	}

	@Override
	public void setTitle(String pTitle) {
		title = pTitle;
		
	}

	@Override
	public String getUserRecipients() {
		return userRecipients;
	}
	
	@Override
	public void setUserRecipients(String userRecipients) {
		this.userRecipients = userRecipients;
	}

	@Override
	public String getOperation() {
		return operation;
	}

	@Override
	public void setOperation(String operation) {
		this.operation = operation;
	}
				
}
