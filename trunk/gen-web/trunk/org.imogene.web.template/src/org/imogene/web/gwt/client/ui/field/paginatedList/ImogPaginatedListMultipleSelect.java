package org.imogene.web.gwt.client.ui.field.paginatedList;

import java.util.HashSet;
import java.util.Set;

import org.imogene.web.gwt.client.ui.field.MainFieldsUtil;
import org.imogene.web.gwt.common.entity.ImogBean;

/**
 * This class implements a paginated list of bean instances that are retrieved by using 
 * asynchronous call to the database.
 * Listbox that enables multiple selection
 * @author MEDES-IMPS
 */
public class ImogPaginatedListMultipleSelect extends ImogPaginatedList {


	/**
	 * Simple constructor
	 */	
	public ImogPaginatedListMultipleSelect(ImogPaginatedListBoxDataProvider provider, MainFieldsUtil mainFieldsUtil) {
		super(provider, mainFieldsUtil, true);
	}
	
	/**
	 * Gets the lisbox selected values
	 * @return the selected values of the listbox
	 */
	public Set<ImogBean> getValues() {
		Set<ImogBean> result = new HashSet<ImogBean>();
		for(int i=0; i<box.getItemCount(); i++){
			if(box.isItemSelected(i)){
				result.add(values.get(i));					
			}
		}
		return result;
	}
}
