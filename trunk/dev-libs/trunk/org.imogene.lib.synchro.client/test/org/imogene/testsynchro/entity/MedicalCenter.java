package org.imogene.testsynchro.entity;

import java.util.Date;
import java.util.Set;

import org.imogene.common.data.Synchronizable;


/**
 * Bean Implementation
 * @author MEDES-IMPS
 */
public class MedicalCenter implements Synchronizable {

	/* Synchronizable properties */
	private String id;
	private Date modified;
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private Date created;
	private String createdBy;

	/* Entity properties */
	private String code;
	private String name;
	private Set<Doctor> medicalCenterDoctors;

	public MedicalCenter() {

		// Add the ENABLED keyword before 'START' to protect this area 
		/*PROTECTED REGION ID(initCEN) START*/
		medicalCenterDoctors = new java.util.HashSet<Doctor>();
		/*PROTECTED REGION END*/

	}

	/* Entity getters and setters */

	public String getCode() {
		return code;
	}

	public void setCode(String param) {
		code = param;
	}

	public String getName() {
		return name;
	}

	public void setName(String param) {
		name = param;
	}

	public Set<Doctor> getMedicalCenterDoctors() {
		return medicalCenterDoctors;
	}

	public void setMedicalCenterDoctors(Set<Doctor> param) {
		medicalCenterDoctors = param;
	}

	/**
	 * @param param the Doctor to add to the medicalCenterDoctors collection
	 */
	public void addTomedicalCenterDoctors(Doctor param) {
		medicalCenterDoctors.add(param);
	}

	/**
	 * @param param the Doctor to remove from the medicalCenterDoctors collection
	 */
	public void removeFrommedicalCenterDoctors(Doctor param) {
		medicalCenterDoctors.remove(param);
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

	public void setEntity(MedicalCenter entity) {
		this.setId(entity.getId());
		this.setModified(entity.getModified());
		this.setModifiedBy(entity.getModifiedBy());
		this.setCreated(entity.getCreated());
		this.setCreatedBy(entity.getCreatedBy());
		this.setCode(entity.getCode());
		this.setName(entity.getName());
		this.setMedicalCenterDoctors(entity.getMedicalCenterDoctors());
	}
	
	public String getDisplayValue() {
		return name;
	}

}
