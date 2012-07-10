package org.imogene.web.gwt.client.ui.field.search;

import org.imogene.common.criteria.CriteriaConstants;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.util.NumericUtil;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Composite to search Float fields
 * 
 * @author Medes-IMPS
 */
public class ImogSearchFloatField extends ImogSearchFieldAbstract<Float> {

	private HorizontalPanel main;
	private TextBox textBox;
	private Label unit;

	/**
	 * Simple constructor
	 */
	public ImogSearchFloatField() {
		layout();
		properties();
	}

	/**
	 * Simple constructor
	 * 
	 * @param label
	 *            label of the field to be searched
	 */
	public ImogSearchFloatField(String label) {
		this();
		thisLabel = label;
	}

	/**
	 * 
	 */
	private void layout() {
		fillOperatorValues();

		main = new HorizontalPanel();
		textBox = new TextBox();
		unit = new Label();
		main.add(textBox);
		main.add(unit);
		addValueWidget(main);
	}

	/**
	 * 
	 */
	private void properties() {

		main.setCellWidth(textBox, "100%");
		main.setCellVerticalAlignment(textBox,
				HasVerticalAlignment.ALIGN_MIDDLE);
		main.setCellHorizontalAlignment(textBox,
				HasHorizontalAlignment.ALIGN_LEFT);
		textBox.setStylePrimaryName("imogene-FormText");

		unit.setStylePrimaryName("imogene-FormText-unit");
		main.setCellVerticalAlignment(unit, HasVerticalAlignment.ALIGN_MIDDLE);
		main.setCellHorizontalAlignment(unit, HasHorizontalAlignment.ALIGN_LEFT);

		layout.setCellVerticalAlignment(main, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(main,
				HasHorizontalAlignment.ALIGN_LEFT);
	}

	@Override
	public Float getValue() {
		return NumericUtil.parseToFloat(textBox.getText());
	}

	public String getDisplayValue() {
		return NumericUtil.parseToString(getValue());
	}

	/**
	 * Set the unit label
	 * 
	 * @param strUnit
	 *            the unit label
	 */
	public void setUnit(String strUnit) {
		unit.setText(strUnit);
	}

	@Override
	public void cancel() {
		thisOperator.setSelectedIndex(0);
		textBox.setText("");
	}

	@Override
	protected void fillOperatorValues() {
		thisOperator.addItem(BaseNLS.constants().search_not_searched(), "");
		thisOperator.addItem(BaseNLS.constants()
				.search_operator_numeric_equal(),
				CriteriaConstants.FLOAT_OPERATOR_EQUAL);
		thisOperator.addItem(BaseNLS.constants().search_operator_superior(),
				CriteriaConstants.FLOAT_OPERATOR_SUP);
		thisOperator.addItem(BaseNLS.constants().search_operator_inferior(),
				CriteriaConstants.FLOAT_OPERATOR_INF);
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