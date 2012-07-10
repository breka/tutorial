package org.imogene.web.gwt.client.ui.field.paginatedList;

import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.ui.field.MainFieldsUtil;

/**
 * This class implements a paginated list of bean instances that are retrieved
 * by using asynchronous call to the database. Listbox that enables only one
 * selection
 * 
 * @author MEDES-IMPS
 */
public class ImogPaginatedListOneSelect extends ImogPaginatedList {

	/**
	 * Simple constructor
	 */
	public ImogPaginatedListOneSelect(
			ImogPaginatedListBoxDataProvider provider,
			MainFieldsUtil mainFieldsUtil) {
		super(provider, mainFieldsUtil, false);
	}

	/**
	 * Gets the value of the listbox that is located at a given index
	 * 
	 * @param i
	 *            the index for which the value shall be retrieved
	 * @return the value of the listbox that is located at index i
	 */
	public ImogBean getValue(int i) {
		if (i < 0)
			return null;
		return values.get(i);
	}

	/**
	 * Gets the lisbox selected value
	 * 
	 * @return the selected value of the listbox
	 */
	public ImogBean getValue() {
		return getValue(box.getSelectedIndex());
	}

	/**
	 * Gets the display value of the listbox selected value
	 * 
	 * @return the display value of the listbox selected value
	 */
	public String getDisplayValue() {
		return box.getItemText(box.getSelectedIndex());
	}

}
