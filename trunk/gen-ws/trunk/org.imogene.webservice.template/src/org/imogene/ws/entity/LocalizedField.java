package org.imogene.ws.entity;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ImogBean implementation of a LocalizedField
 * @author MEDES-IMPS
 */
@XmlRootElement(name = "localizedfield")
@XmlType(propOrder = {"translations"})
public class LocalizedField {
	
	/* field id of the LocalizedField */
	private String fieldid;
	/* translations of the LocalizedField as a list of LocalizedText */
	private LocalizedTextList translations;
	
	
	public LocalizedField() {
	}

	@XmlAttribute
	public String getFieldid() {
		return fieldid;
	}
	public void setFieldid(String fieldId) {
		this.fieldid = fieldId;
	}

	@XmlElement(name = "translations")
	public LocalizedTextList getTranslations() {
		return translations;
	}
	public void setTranslations(LocalizedTextList translations) {
		this.translations = translations;
	}
	
	
	
	

}
