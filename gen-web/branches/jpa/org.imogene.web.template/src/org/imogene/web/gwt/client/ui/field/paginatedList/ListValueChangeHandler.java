package org.imogene.web.gwt.client.ui.field.paginatedList;

/**
 * Handler that manages value changes in Imogene paginated listboxes
 * 
 * @author MEDES-IMPS
 */
public interface ListValueChangeHandler {

	/**
	 * Actions that shall be performs when a value changes in an Imogene
	 * paginated listbox
	 * 
	 * @param source
	 *            the source field
	 */
	public void onListValueChange();

}
