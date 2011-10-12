package org.imogene.web.gwt.client.ui.field.search;

import java.util.Date;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.util.DateUtil;
import org.imogene.web.gwt.common.criteria.CriteriaConstants;
import org.zenika.widget.client.datePicker.DatePicker;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;


/**
 * Composite to search Date fields
 * @author Medes-IMPS
 */
public class ImogSearchDateField extends ImogSearchFieldAbstract<Date[]> {
	
	private HorizontalPanel main;	
	private DatePicker fromDatePicker;	
	private Label andLabel;	
	private DatePicker toDatePicker;
	
	
	/**
	 * Simple constructor
	 */
	public ImogSearchDateField(){
		layout();
		properties();
	}
	
	/**
	 * Simple constructor
	 * @param label label of the field to be searched
	 */
	public ImogSearchDateField(String label){
		this();
		thisLabel = label;
	}
	
	/**
	 */
	private void layout(){		
		fillOperatorValues();
		
		main = new HorizontalPanel();
		
		fromDatePicker = new DatePicker(90);
		fromDatePicker.setDateFormatter(DateUtil.getDateFormater());
		main.add(fromDatePicker);
		
		andLabel = new Label();
		andLabel.setText(BaseNLS.constants().search_date_label_and());
		main.add(andLabel);	
		
		toDatePicker = new DatePicker(90);
		toDatePicker.setDateFormatter(DateUtil.getDateFormater());
		main.add(toDatePicker);
		
		addValueWidget(main);
	}
	
	/**
	 */
	private void properties(){					
		fromDatePicker.setStylePrimaryName("imogene-FormTextDate");	
		andLabel.addStyleName("imogene-FormTextDate-and");
		toDatePicker.setStylePrimaryName("imogene-FormTextDate");		
		
		main.setCellVerticalAlignment(andLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		main.setCellHorizontalAlignment(andLabel, HasHorizontalAlignment.ALIGN_LEFT);
		
		layout.setCellVerticalAlignment(main, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(main, HasHorizontalAlignment.ALIGN_LEFT);
	}
		

	@Override
	public Date[] getValue() {
		Date[] result = new Date[2];
		result[0] = fromDatePicker.getSelectedDate();
		result[1] = toDatePicker.getSelectedDate();
		return result;
	}
	
	public String getFromDateDisplayValue() {
		return fromDatePicker.getText();
	}
	
	public String getToDateDisplayValue() {
		return toDatePicker.getText();
	}

	@Override
	public void cancel() {
		thisOperator.setSelectedIndex(0);
		fromDatePicker.setSelectedDate(null);
		toDatePicker.setSelectedDate(null);
		fromDatePicker.setText("");
		toDatePicker.setText("");
	}

	@Override
	protected void fillOperatorValues() {
		thisOperator.addItem(BaseNLS.constants().search_not_searched(), "");
		thisOperator.addItem(BaseNLS.constants().search_operator_date_between(), CriteriaConstants.DATE_OPERATOR_BETWEEN);
		thisOperator.addItem(BaseNLS.constants().search_operator_isnull(), 	CriteriaConstants.OPERATOR_ISNULL);
		thisOperator.setSelectedIndex(0);
	}
	
	@Override
	public boolean toBeSearched() {
		return (getOperatorValue() != null	&& getOperatorValue().equals(CriteriaConstants.OPERATOR_ISNULL)) || 
				(
						getOperatorValue() != null	&& getOperatorValue().equals(CriteriaConstants.DATE_OPERATOR_BETWEEN) && 
					(
							(getFromDateDisplayValue() != null && !getFromDateDisplayValue().equals("")) || 
							(getToDateDisplayValue() != null && !getToDateDisplayValue().equals(""))
					)
				)
				;
	}

}
