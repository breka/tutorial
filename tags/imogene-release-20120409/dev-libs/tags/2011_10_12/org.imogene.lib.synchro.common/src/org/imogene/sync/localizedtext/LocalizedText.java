package org.imogene.sync.localizedtext;

import java.util.Date;

import org.imogene.common.data.Synchronizable;

/**
 * LocalizedText Bean Implementation
 * @author Medes-IMPS
 */
public class LocalizedText implements Synchronizable {

	/* Synchronizable properties */
	private String id;
	private Date modified;
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private Date created;
	private String createdBy;

	/* Entity properties */
	private String fieldId;
	private String locale;
	private String value;
	private Boolean originalValue;
	private Boolean potentialyWrong;

	public LocalizedText() {

		// Add the ENABLED keyword before 'START' to protect this area 
		/*PROTECTED REGION ID(initTRSN) START*/
		/*PROTECTED REGION END*/

	}

	/* Entity getters and setters */

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String param) {
		fieldId = param;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String param) {
		locale = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String param) {
		value = param;
	}
	
	public Boolean getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(Boolean originalValue) {
		this.originalValue = originalValue;
	}

	public Boolean getPotentialyWrong() {
		return potentialyWrong;
	}

	public void setPotentialyWrong(Boolean potentialyWrong) {
		this.potentialyWrong = potentialyWrong;
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

	public void setEntity(LocalizedText entity) {
		this.setId(entity.getId());
		this.setModified(entity.getModified());
		this.setModifiedBy(entity.getModifiedBy());
		this.setCreated(entity.getCreated());
		this.setCreatedBy(entity.getCreatedBy());
		this.setFieldId(entity.getFieldId());
		this.setLocale(entity.getLocale());
		this.setValue(entity.getValue());
		this.setOriginalValue(entity.getOriginalValue());
		this.setPotentialyWrong(entity.getPotentialyWrong());
	}

	public String getDisplayValue() {
		StringBuffer displayValue = new StringBuffer();
		return displayValue.toString().trim();
	}

}
