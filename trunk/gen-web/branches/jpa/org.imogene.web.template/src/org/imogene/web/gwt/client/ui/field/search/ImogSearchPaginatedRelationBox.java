package org.imogene.web.gwt.client.ui.field.search;

import org.imogene.common.criteria.CriteriaConstants;
import org.imogene.common.criteria.ImogJunction;
import org.imogene.common.entity.ImogBean;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.MainFieldsUtil;
import org.imogene.web.gwt.client.ui.field.paginatedList.AbstractImogListBoxDataProvider;
import org.imogene.web.gwt.client.ui.field.paginatedList.ImogPaginatedListBox;
import org.imogene.web.gwt.client.ui.field.paginatedList.ListValueChangeHandler;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

/**
 * Composite to search relation fields with cardinality 1
 * 
 * @author Medes-IMPS
 */
public class ImogSearchPaginatedRelationBox<T extends ImogBean> extends
		ImogSearchFieldAbstract<T> implements ListValueChangeHandler {

	private ImogPaginatedListBox<T> paginatedList;

	/**
	 * Simple constructor
	 */
	public ImogSearchPaginatedRelationBox() {
		layout();
		properties();
	}

	/**
	 * Simple constructor
	 * 
	 * @param label
	 *            label of the field to be searched
	 */
	public ImogSearchPaginatedRelationBox(String label) {
		this();
		thisLabel = label;
	}

	/**
	 * 
	 */
	private void layout() {
		fillOperatorValues();

		paginatedList = new ImogPaginatedListBox<T>();
		paginatedList.addListValueChangeHandler(this);
		addValueWidget(paginatedList);
	}

	/**
	 * 
	 */
	private void properties() {
		layout.setCellVerticalAlignment(paginatedList,
				HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(paginatedList,
				HasHorizontalAlignment.ALIGN_LEFT);
	}

	/**
	 * 
	 * @return
	 */
	public ImogPaginatedListBox<T> getEmbedded() {
		return paginatedList;
	}

	@Override
	public T getValue() {
		return paginatedList.getValue();
	}

	public String getDisplayValue() {
		return (String) paginatedList.getText();
	}

	/**
	 * 
	 */
	public void clear() {
		paginatedList.clear();
	}

	/**
	 * Method launched when the listbox selected value changes
	 */
	public void onListValueChange() {
		notifyValueChange();
	}

	/**
	 * 
	 * @param dataProvider
	 *            Data provider to feed the paginated list with bean instances
	 * @param mainFieldsUtil
	 *            Utility class to get the bean instances display values
	 */
	public void setDataProvider(AbstractImogListBoxDataProvider dataProvider,
			MainFieldsUtil mainFieldsUtil) {
		paginatedList.setDataProvider(dataProvider, mainFieldsUtil);
	}

	/**
	 * Sets filtering criterions for which values have to be filtered
	 * 
	 * @param criterions
	 *            ImogJunction including the criterions for which the values
	 *            have to be filtered
	 */
	public void setFilterParameters(ImogJunction criterions) {
		paginatedList.setFilterParameters(criterions);
	}

	protected void fillOperatorValues() {
		thisOperator.addItem(BaseNLS.constants().search_not_searched(), "");
		thisOperator.addItem(
				BaseNLS.constants().search_operator_string_equal(),
				CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL);
		thisOperator.addItem(BaseNLS.constants().search_operator_isnull(),
				CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL);
		thisOperator.setSelectedIndex(0);
	}

	@Override
	public void cancel() {
		clear();
		thisOperator.setSelectedIndex(0);
	}

	@Override
	public boolean toBeSearched() {
		return (getOperatorValue() != null && getOperatorValue().equals(
				CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL))
				|| (getOperatorValue() != null && getValue() != null);
	}
}
