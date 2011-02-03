package org.imogene.testsynchro.entity;

import java.util.Date;

import org.imogene.common.data.Synchronizable;


/**
 * Bean Implementation
 * @author MEDES-IMPS
 */
public class Doctor implements Synchronizable {

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
	private MedicalCenter doctorMedicalCenter;
	private String photo;
	private String address;
	private String phoneNumber;
	private String mobileNumber;

	public Doctor() {

		// Add the ENABLED keyword before 'START' to protect this area 
		/*PROTECTED REGION ID(initDOC) START*/
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

	public MedicalCenter getDoctorMedicalCenter() {
		return doctorMedicalCenter;
	}

	public void setDoctorMedicalCenter(MedicalCenter param) {
		doctorMedicalCenter = param;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String param) {
		photo = param;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String param) {
		address = param;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String param) {
		phoneNumber = param;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String param) {
		mobileNumber = param;
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

	public void setEntity(Doctor entity) {
		this.setId(entity.getId());
		this.setModified(entity.getModified());
		this.setModifiedBy(entity.getModifiedBy());
		this.setCreated(entity.getCreated());
		this.setCreatedBy(entity.getCreatedBy());
		this.setName(entity.getName());
		this.setFirstName(entity.getFirstName());
		this.setDoctorMedicalCenter(entity.getDoctorMedicalCenter());
		this.setPhoto(entity.getPhoto());
		this.setAddress(entity.getAddress());
		this.setPhoneNumber(entity.getPhoneNumber());
		this.setMobileNumber(entity.getMobileNumber());
	}
	
	public String getDisplayValue() {
		return name;
	}

}
