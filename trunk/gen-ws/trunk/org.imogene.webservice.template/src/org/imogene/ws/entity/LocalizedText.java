package org.imogene.ws.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.imogene.ws.serializer.xml.DateAdapter;


/**
 * ImogBean implementation for the entity LocalizedText
 * @author MEDES-IMPS
 */
@XmlRootElement(name = "localizedtext")
@XmlType(propOrder = {"fieldId", "locale", "value", "creationDate",
		"lastModificationDate", "uploadDate", "creator", "modifier",
		"modifiedFrom"})
public class LocalizedText implements MedooBean {

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

	@XmlJavaTypeAdapter(value = DateAdapter.class)
	@XmlElement(name = "creationdate")
	public Date getCreationDate() {
		return creationDate;
	}

	public String getCreator() {
		return creator;
	}

	@XmlAttribute
	public String getId() {
		return id;
	}

	@XmlJavaTypeAdapter(value = DateAdapter.class)
	@XmlElement(name = "lastmodificationdate")
	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	@XmlElement(name = "modifiedfrom")
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public String getModifier() {
		return modifier;
	}

	@XmlJavaTypeAdapter(value = DateAdapter.class)
	@XmlElement(name = "uploaddate")
	public Date getUploadDate() {
		return uploadDate;
	}

	/* Setters for Medoo bean fields */

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
	@XmlElement(name = "fieldid")
	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String value) {
		fieldId = value;
	}
	@XmlElement(name = "locale")
	public String getLocale() {
		return locale;
	}

	public void setLocale(String value) {
		locale = value;
	}
	@XmlElement(name = "value")
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
