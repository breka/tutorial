package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;



public class ImogBooleanField extends ImogFieldAbstract<Boolean> implements ValueChangeHandler<Boolean> {

	/* status - behavior */
	private String thisLabel;
	
	private Boolean thisValue=null;		
	
	private boolean edited = false;
	
	/* widgets */
	private HorizontalPanel main;
	
	private RadioButton yesBox;
	
	private RadioButton noBox;
	
	private RadioButton nspBox;
	
	/**
	 */
	public ImogBooleanField(){
		layout();
	}
	
	/**	 
	 * @param title field label
	 */
	public ImogBooleanField(String title){
		this();
		thisLabel = title;
	}
	
	/**
	 */
	private void layout(){
		main = new HorizontalPanel();
		String groupName = String.valueOf(this.hashCode());
		yesBox = new RadioButton(groupName);
		yesBox.setText(BaseNLS.constants().boolean_true());
		noBox = new RadioButton(groupName);
		noBox.setText(BaseNLS.constants().boolean_false());
		nspBox = new RadioButton(groupName);
		nspBox.setText(BaseNLS.constants().boolean_unknown());
		main.add(yesBox);
		main.add(noBox);
		main.add(nspBox);
		initWidget(main);
		properties();
	}	
	
	/**
	 */
	private void properties(){
		nspBox.setValue(true);
		nspBox.addValueChangeHandler(this);
		noBox.addValueChangeHandler(this);
		yesBox.addValueChangeHandler(this);
	}
				
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> arg0) {		
		notifyValueChange();
	}

	@Override
	public boolean validate() {		
		if(isMandatory() && !nspBox.getValue() && !noBox.getValue() && !yesBox.getValue()){
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
	public Boolean getValue() {		
		if(nspBox.getValue())
			return null;
		if(yesBox.getValue())
			return true;
		if(noBox.getValue())
			return false;
		return thisValue;
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(Boolean value) {
		if(value==null){
			nspBox.setValue(true);			
		}else if(value){
			yesBox.setValue(true);
		}else if(!value){
			noBox.setValue(true);
		}		
		thisValue = value;
	}
	
	@Override
	public void setValue(Boolean value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public void setEnabled(boolean editable) {		
		yesBox.setEnabled(editable);
		noBox.setEnabled(editable);
		nspBox.setEnabled(editable);
		edited = editable;
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
	
}
