package org.imogene.web.gwt.common.entity;

import java.util.Date;


/**
 * ImogBean implementation for the entity LocalizedText
 * @author MEDES-IMPS
 */
public class LocalizedText implements ImogBean {

	/* Imog bean fields */
	private String id;
	private Date creationDate;
	private Date lastModificationDate;
	private Date uploadDate;
	private String creator;
	private String modifier;
	private String modifiedFrom;

	/* Entity fields */
	private String fieldId;
	private String locale;
	private String value;
	
	

	public LocalizedText() {
	}

	/* Getters for Imog bean fields */

	public Date getCreationDate() {
		return creationDate;
	}

	public String getCreator() {
		return creator;
	}

	public String getId() {
		return id;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public String getModifier() {
		return modifier;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	/* Setters for Imog bean fields */

	public void setCreationDate(Date date) {
		creationDate = date;
	}

	public void setCreator(String pCreator) {
		creator = pCreator;
	}

	public void setId(String pId) {
		id = pId;
	}

	public void setLastModificationDate(Date date) {
		lastModificationDate = date;
	}

	public void setModifiedFrom(String terminal) {
		modifiedFrom = terminal;
	}

	public void setModifier(String pModifier) {
		modifier = pModifier;
	}

	public void setUploadDate(Date date) {
		uploadDate = date;
	}

	/* Getters and Setters for Entity fields */

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String value) {
		fieldId = value;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String value) {
		locale = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String pValue) {
		value = pValue;
	}

	public String getDisplayValue() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(value);
		return buffer.toString();
	}
}
