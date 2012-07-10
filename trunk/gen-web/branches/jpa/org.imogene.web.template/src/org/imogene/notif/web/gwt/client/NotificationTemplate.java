package org.imogene.notif.web.gwt.client;

import java.util.Set;

import org.imogene.common.role.Role;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NotificationTemplate implements IsSerializable {

	private String id;

	private String message;

	private String name;

	private String sourceCard;

	private String title;

	private String operation;

	private String userRecipients;

	private Integer method;

	private Set<Role> roleRecipients;

	public String getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public String getName() {
		return name;
	}

	public Integer getMethod() {
		return method;
	}

	public Set<Role> getRoleRecipients() {
		return roleRecipients;
	}

	public String getSourceCard() {
		return sourceCard;
	}

	public void setId(String pId) {
		id = pId;
	}

	public void setMessage(String pMessage) {
		message = pMessage;

	}

	public void setName(String pName) {
		name = pName;

	}

	public void setMethod(Integer pMethod) {
		method = pMethod;
	}

	public void setRoleRecipients(Set<Role> roles) {
		roleRecipients = roles;
	}

	public void setSourceCard(String pSource) {
		sourceCard = pSource;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String pTitle) {
		title = pTitle;

	}

	public String getUserRecipients() {
		return userRecipients;
	}

	public void setUserRecipients(String userRecipients) {
		this.userRecipients = userRecipients;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
