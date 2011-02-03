package org.imogene.web.gwt.client.ui.field;

import java.util.Date;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.util.DateUtil;
import org.zenika.widget.client.datePicker.DatePicker;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;


public class ImogDateField extends ImogFieldAbstract<Date> {
	
	/* status - behavior */		
	private String thisLabel;	
	
	//private Date thisValue;	
	private boolean edited = false;

	/* widgets */
	private HorizontalPanel layout;	
	
	/* display widget */
	private TextBox dateDisplay;
	/* edit widget */
	private DatePicker dateBox;
	
	/**
	 */
	public ImogDateField(){
		layout();
		properties();
	}
	
	/**
	 * @param label the field label
	 */
	public ImogDateField(String label){
		this();
		thisLabel = label;
	}
	
	/**
	 */
	private void layout(){
		layout = new HorizontalPanel();		
		dateBox = new DatePicker();
		dateDisplay = new TextBox();
		layout.add(dateDisplay);
		initWidget(layout);
	}
	
	/**
	 */
	private void properties(){					
		dateDisplay.setStylePrimaryName("imogene-FormText");	
		dateDisplay.setEnabled(false);
		dateBox.setStylePrimaryName("imogene-FormText");	
		dateBox.addChangeHandler(new ChangeHandler(){
			@Override
			public void onChange(ChangeEvent arg0) {
				notifyValueChange();
			}
			
		});
	}
	
	public DatePicker getEmbedded(){
		return dateBox;
	}
	
	public void setInError(boolean inError){
		if(inError)
			dateBox.addStyleDependentName("error");
		else
			dateBox.removeStyleDependentName("error");
	}
	
	public void setEnabled(boolean enabled){
		if(!edited && enabled){
			layout.remove(dateDisplay);
			layout.add(dateBox);
		}
		if(edited && !enabled){
			layout.remove(dateBox);
			layout.add(dateDisplay);
		}
		edited = enabled;
	}

	@Override
	public boolean validate() {
		if(isMandatory() && dateBox.getSelectedDate()==null){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		hideError();
		return true;
	}

	@Override
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public Date getValue() {
		return dateBox.getSelectedDate();
	}
	
	public String getDisplayValue() {
		return dateDisplay.getText();
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(Date value) {				
		if(value!=null){
			dateBox.setSelectedDate(value);
			dateDisplay.setText(DateUtil.getFormatedDate(value));
		}
	}

	@Override
	public boolean isEdited() {
		return edited;
	}	
	
}
