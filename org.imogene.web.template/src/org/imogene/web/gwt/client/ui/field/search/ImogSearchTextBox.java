package org.imogene.web.gwt.client.ui.field.search;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.common.criteria.CriteriaConstants;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;


/**
 * Composite to search Text fields
 * @author Medes-IMPS
 */
public class ImogSearchTextBox extends ImogSearchFieldAbstract<String> {
	
	private TextBox thisValue;
	
	
	/**
	 * Simple constructor
	 */	
	public ImogSearchTextBox(){
		layout();
		properties();
	}
	
	/**
	 * Simple constructor
	 * @param label label of the field to be searched
	 */
	public ImogSearchTextBox(String label){
		this();
		thisLabel = label;
	}
	
	/**
	 * 
	 */
	private void layout(){		
		fillOperatorValues();
		
		thisValue = new TextBox();
		addValueWidget(thisValue);	
	}
	
	/**
	 * 
	 */
	private void properties(){
		layout.setCellWidth(thisValue, "100%");
		thisValue.setStylePrimaryName("imogene-FormText");
		
		layout.setCellVerticalAlignment(thisValue, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(thisValue, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	public TextBox getEmbedded(){
		return thisValue;
	}

	@Override
	public String getValue() {
		String result = thisValue.getText();
		if (result != null && !result.equals(""))
			return result;
		else
			return null;
	}
	
	@Override
	protected void fillOperatorValues() {
		thisOperator.addItem(BaseNLS.constants().search_not_searched(), "");
		thisOperator.addItem(BaseNLS.constants().search_operator_contains(), CriteriaConstants.STRING_OPERATOR_CONTAINS);
		thisOperator.addItem(BaseNLS.constants().search_operator_startwith(), CriteriaConstants.STRING_OPERATOR_STARTWITH);
		thisOperator.addItem(BaseNLS.constants().search_operator_string_equal(), CriteriaConstants.STRING_OPERATOR_EQUAL);
		thisOperator.addItem(BaseNLS.constants().search_operator_isnull(), CriteriaConstants.OPERATOR_ISNULL_OR_EMPTY);
		thisOperator.setSelectedIndex(0);
	}
	
	@Override
	public void cancel() {
		thisValue.setText("");
		thisOperator.setSelectedIndex(0);
	}
	
	@Override
	public boolean toBeSearched() {
		return (getOperatorValue() != null && getOperatorValue().equals(CriteriaConstants.OPERATOR_ISNULL_OR_EMPTY))	|| 
			   (getOperatorValue() != null && getValue() != null && !getValue().equals(""));
	}
	
}
