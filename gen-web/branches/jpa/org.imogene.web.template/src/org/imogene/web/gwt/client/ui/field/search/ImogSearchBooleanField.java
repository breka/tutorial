package org.imogene.web.gwt.client.ui.field.search;

import org.imogene.common.criteria.CriteriaConstants;
import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * Composite to search Boolean fields
 * 
 * @author Medes-IMPS
 */
public class ImogSearchBooleanField extends ImogSearchFieldAbstract<Boolean> {

	private HorizontalPanel main;
	private RadioButton yesBox;
	private RadioButton noBox;

	/**
	 * Simple constructor
	 */
	public ImogSearchBooleanField() {
		layout();
		properties();
	}

	/**
	 * Simple constructor
	 * 
	 * @param label
	 *            label of the field to be searched
	 */
	public ImogSearchBooleanField(String label) {
		this();
		thisLabel = label;
	}

	/**
	 */
	private void layout() {
		fillOperatorValues();

		main = new HorizontalPanel();
		String groupName = String.valueOf(this.hashCode());
		yesBox = new RadioButton(groupName);
		yesBox.setText(BaseNLS.constants().boolean_true());
		noBox = new RadioButton(groupName);
		noBox.setText(BaseNLS.constants().boolean_false());
		main.add(yesBox);
		main.add(noBox);
		addValueWidget(main);
	}

	/**
	 */
	private void properties() {
		main.setCellVerticalAlignment(yesBox, HasVerticalAlignment.ALIGN_MIDDLE);
		main.setCellVerticalAlignment(noBox, HasVerticalAlignment.ALIGN_MIDDLE);

		layout.setCellVerticalAlignment(main, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(main,
				HasHorizontalAlignment.ALIGN_LEFT);
	}

	@Override
	public Boolean getValue() {
		if (yesBox.getValue())
			return true;
		if (noBox.getValue())
			return false;
		return null;
	}

	public String getDisplayValue() {
		if (yesBox.getValue())
			return "true";
		if (noBox.getValue())
			return "false";
		return "";
	}

	@Override
	public void cancel() {
		thisOperator.setSelectedIndex(0);
		yesBox.setValue(null);
		noBox.setValue(null);
	}

	@Override
	protected void fillOperatorValues() {
		thisOperator.addItem(BaseNLS.constants().search_not_searched(), "");
		thisOperator.addItem(
				BaseNLS.constants().search_operator_string_equal(),
				CriteriaConstants.BOOLEAN_OPERATOR_EQUAL);
		thisOperator.addItem(BaseNLS.constants().search_operator_isnull(),
				CriteriaConstants.OPERATOR_ISNULL);
		thisOperator.setSelectedIndex(0);
	}

	@Override
	public boolean toBeSearched() {
		return (getOperatorValue() != null && getOperatorValue().equals(
				CriteriaConstants.OPERATOR_ISNULL))
				|| (getOperatorValue() != null && getValue() != null);
	}

}
