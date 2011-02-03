package org.imogene.testsynchro.entity;

import java.util.Date;

import org.imogene.common.data.Synchronizable;


/**
 * Bean Implementation
 * @author MEDES-IMPS
 */
public class Patient implements Synchronizable {

	/* Synchronizable properties */
	private String id;
	private Date modified;
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private Date created;
	private String createdBy;

	/* Entity properties */
	private String name;
	private String firstName;
	private String address;
	private Date birthday;
	private String phoneNumber;
	private MedicalCenter patientMedicalCenter;

	public Patient() {

		// Add the ENABLED keyword before 'START' to protect this area 
		/*PROTECTED REGION ID(initPAT) START*/
		/*PROTECTED REGION END*/

	}

	/* Entity getters and setters */

	public String getName() {
		return name;
	}

	public void setName(String param) {
		name = param;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String param) {
		firstName = param;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String param) {
		address = param;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date param) {
		birthday = param;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String param) {
		phoneNumber = param;
	}

	public MedicalCenter getPatientMedicalCenter() {
		return patientMedicalCenter;
	}

	public void setPatientMedicalCenter(MedicalCenter param) {
		patientMedicalCenter = param;
	}

	/* Synchronizable getters and setters */

	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date pModified) {
		modified = pModified;
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

	public void setModifiedBy(String id) {
		modifiedBy = id;
	}
	
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public void setModifiedFrom(String modifiedFrom) {
		this.modifiedFrom = modifiedFrom;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/* other getters and setters */

	public void setEntity(Patient entity) {
		this.setId(entity.getId());
		this.setModified(entity.getModified());
		this.setModifiedBy(entity.getModifiedBy());
		this.setCreated(entity.getCreated());
		this.setCreatedBy(entity.getCreatedBy());
		this.setName(entity.getName());
		this.setFirstName(entity.getFirstName());
		this.setAddress(entity.getAddress());
		this.setBirthday(entity.getBirthday());
		this.setPhoneNumber(entity.getPhoneNumber());
		this.setPatientMedicalCenter(entity.getPatientMedicalCenter());
	}
	
	public String getDisplayValue() {
		return name;
	}

}
