package org.imogene.web.gwt.client.ui.field;

public interface FieldValueChangeHandler {

	/**
	 * 
	 * @param source the field source
	 * @param value the current value displayed in the field
	 */
	public void onFieldValueChange(ImogField<?> source);
	
}
