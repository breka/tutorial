package org.imogene.testsynchro.entity;

import java.util.Date;

import org.imogene.common.data.Synchronizable;


/**
 * Bean Implementation
 * @author MEDES-IMPS
 */
public class MedicalRequest implements Synchronizable {

	/* Synchronizable properties */
	private String id;
	private Date modified;
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private Date created;
	private String createdBy;

	/* Entity properties */
	private Patient medicalRequestPatient;
	private Doctor medicalRequestDoctor;
	private MedicalCenter medicalRequestMedicalCenter;
	private String context;
	private String request;
	private String photo;

	public MedicalRequest() {

		// Add the ENABLED keyword before 'START' to protect this area 
		/*PROTECTED REGION ID(initREQ) START*/
		/*PROTECTED REGION END*/

	}

	/* Entity getters and setters */

	public Patient getMedicalRequestPatient() {
		return medicalRequestPatient;
	}

	public void setMedicalRequestPatient(Patient param) {
		medicalRequestPatient = param;
	}

	public Doctor getMedicalRequestDoctor() {
		return medicalRequestDoctor;
	}

	public void setMedicalRequestDoctor(Doctor param) {
		medicalRequestDoctor = param;
	}

	public MedicalCenter getMedicalRequestMedicalCenter() {
		return medicalRequestMedicalCenter;
	}

	public void setMedicalRequestMedicalCenter(MedicalCenter param) {
		medicalRequestMedicalCenter = param;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String param) {
		context = param;
	}

	public String getRequest() {
		return request;
	}
	

	public void setRequest(String param) {
		request = param;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String param) {
		photo = param;
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

	public void setEntity(MedicalRequest entity) {
		this.setId(entity.getId());
		this.setModified(entity.getModified());
		this.setModifiedBy(entity.getModifiedBy());
		this.setCreated(entity.getCreated());
		this.setCreatedBy(entity.getCreatedBy());
		this.setMedicalRequestPatient(entity.getMedicalRequestPatient());
		this.setMedicalRequestDoctor(entity.getMedicalRequestDoctor());
		this.setMedicalRequestMedicalCenter(entity
				.getMedicalRequestMedicalCenter());
		this.setContext(entity.getContext());
		this.setRequest(entity.getRequest());
		this.setPhoto(entity.getPhoto());
	}
	
	public String getDisplayValue() {
		return context;
	}

}
