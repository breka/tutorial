package org.imogene.common.localized;

import javax.persistence.Entity;

import org.imogene.common.entity.ImogBeanImpl;

/**
 * ImogBean implementation for the entity LocalizedText
 * 
 * @author MEDES-IMPS
 */
@Entity
public class LocalizedText extends ImogBeanImpl {

	/* Entity fields */
	private String fieldId;
	private String locale;
	private String value;
	private Boolean originalValue;
	private Boolean potentialyWrong;
	
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

	@Override
	public String getDisplayValue() {
		return value;
	}
}
