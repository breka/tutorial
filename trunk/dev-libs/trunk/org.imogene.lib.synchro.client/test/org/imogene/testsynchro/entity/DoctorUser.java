package org.imogene.testsynchro.entity;

import java.util.HashSet;
import java.util.Set;

import org.imogene.common.data.SynchronizableUser;
import org.imogene.uao.role.Role;
import org.imogene.uao.synchronizable.SynchronizableEntity;


/**
 * User Bean Implementation
 * @author MEDES-IMPS
 */
public class DoctorUser extends Doctor implements SynchronizableUser {

	private String login;

	private String password;

	/** 
	 * Store the assigned roles as they are set in the external user management application 
	 */
	private Set<Role> assignedRoles = new HashSet<Role>();

	/** 
	 * Store the assigned synchronizable entities as they are set in the external user management application 
	 */
	private Set<SynchronizableEntity> assignedSynchronizables = new HashSet<SynchronizableEntity>();

	public DoctorUser() {
		super();
	}

	/* SynchronizableUser getters and setters */

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return assignedRoles;
	}

	public void setRoles(Set<Role> roles) {
		this.assignedRoles = roles;
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

	/* Filter fields management */

	/** 
	 * Filter fields for the MedicalCenter entity 
	 */
	private Set<MedicalCenter> medicalCenterFilterDoctorFilterField = new HashSet<MedicalCenter>();

	/**
	 * Set the list of allowed MedicalCenters for card access.
	 */
	public void setMedicalCenterFilterDoctorFilterField(
			Set<MedicalCenter> filterFields) {
		medicalCenterFilterDoctorFilterField = filterFields;
	}

	/**
	 * Get the list of allowed MedicalCenters for card access.
	 * @return a list of MedicalCenters 
	 */
	public Set<MedicalCenter> getMedicalCenterFilterDoctorFilterField() {
		return medicalCenterFilterDoctorFilterField;
	}

	/**
	 * Add a MedicalCenter to list of allowed MedicalCenters for card access.
	 */
	public void addMedicalCenterFilterDoctorFilterField(
			MedicalCenter synchronizable) {
		medicalCenterFilterDoctorFilterField.add(synchronizable);
	}

}
