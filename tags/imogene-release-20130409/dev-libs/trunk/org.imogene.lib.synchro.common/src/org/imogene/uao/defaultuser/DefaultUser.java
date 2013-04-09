package org.imogene.uao.defaultuser;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;
import org.imogene.uao.role.Role;
import org.imogene.uao.synchronizable.SynchronizableEntity;




public class DefaultUser implements SynchronizableUser
{

	/* Synchronizable properties */
	private String id;	
	private Date modified;	
	private Date uploadDate;
	private String modifiedBy;	
	private String modifiedFrom;
	private String createdBy;
	private Date created;
	
	/* SynchronizableUser properties */
	private String _login;
	private String _password;	
	
	/** 
	 * Store the assigned roles as set in the external user management application 
	 */
	private Set<Role> assignedRoles = new HashSet<Role>();
	
	/** 
	 * Store the assigned synchronizable entities as set in the external user management application 
	 */
	private Set<SynchronizableEntity> assignedSynchronizables = new HashSet<SynchronizableEntity>();
	
	
	
	public DefaultUser() {

	}
	
	
	/* SynchronizableUser getters and setters */

	public String getLogin() {
		return _login;
	}
	
	public void setLogin(String login) {
		_login = login;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}	

	public Set<Role> getRoles() {
		return assignedRoles;
	}

	public void setRoles(Set<Role> roles) {
		assignedRoles = roles;
	}
	
	public void addRole(Role role) {
		this.assignedRoles.add(role);
	}
	
	public Set<SynchronizableEntity> getSynchronizables() {
		return assignedSynchronizables;
	}

	public void setSynchronizables(Set<SynchronizableEntity> synchronizables) {
		this.assignedSynchronizables = synchronizables;
	}	
	
	public void addSynchronizable(SynchronizableEntity synchronizable) {
		assignedSynchronizables.add(synchronizable);
	}
 	

	/* Synchronizable getters and setters */

	public Date getModified() {
		return modified;
	}
	
	public Date getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(Date date) {
		uploadDate = date;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public String getId() {		
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}

	public void setModified(Date pModified) {
		modified = pModified;
	}

	public void setModifiedBy(String id) {
		modifiedBy = id;
	}
	
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public void setModifiedFrom(String modifiedFrom) {
		this.modifiedFrom = modifiedFrom;
	}

	public void setCreatedBy(String id) {
		createdBy = id;
	}
	
	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}
	
	public void setEntity(Synchronizable entity) {
		// TODO Auto-generated method stub	
	}

	public String getDisplayValue() {
		return _login;
	}
	
	

		    
 } // end class User 