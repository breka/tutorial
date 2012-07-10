package org.imogene.web.gwt.client.ui.field;

/**
 * Handler that manages value changes in Imogene fields
 * 
 * @author MEDES-IMPS
 */
public interface FieldValueChangeHandler {

	/**
	 * Actions that shall be performs when a value changes in an Imogene field
	 * 
	 * @param source
	 *            the source field
	 */
	public void onFieldValueChange(ImogField<?> source);

}
