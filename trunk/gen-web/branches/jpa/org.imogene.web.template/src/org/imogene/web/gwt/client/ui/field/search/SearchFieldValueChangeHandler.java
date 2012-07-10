package org.imogene.web.gwt.client.ui.field.search;

/**
 * Handler that manages value changes in Imogene search fields
 * 
 * @author MEDES-IMPS
 */
public interface SearchFieldValueChangeHandler {

	/**
	 * Actions that shall be performs when a value changes in an Imogene field
	 * 
	 * @param source
	 *            the source field
	 */
	public void onFieldValueChange(ImogSearchField<?> source);

}
