package org.imogene.common.notification;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.imogene.common.role.Role;

@Entity
public class Notification implements Serializable {

	@Id
	private String id;

	private String message;

	private String name;

	private String sourceCard;

	private String title;

	private String operation;

	private String userRecipients;

	private Integer method;

	@ManyToMany
	@JoinTable(name = "NotificationRoles", joinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
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
