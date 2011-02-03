package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;


public class ImogEmailField extends ImogFieldAbstract<String> implements ValueChangeHandler<String>{

	public static final String EMAIL_REGEX = "[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]";
	
	private static final String LINK_TMPL = "<a href=\"mailto:%EMAIL%\">%EMAIL%</a>";

	/* status - behavior */
	
	private String thisLabel;

	private String thisValue;
	
	private boolean edited = false;
	
	/* widgets */
	
	private HorizontalPanel main;
	
	private TextBox emailEdit;
	
	private HTML emailDisplay;

	/**
	 */
	public ImogEmailField(){
		layout();
	}

	/**
	 * @param title field label
	 */
	public ImogEmailField(String title){
		this();
		thisLabel = title;
	}
	
	/**
	 */
	private void layout(){
		main = new HorizontalPanel();
		emailDisplay = new HTML();
		emailEdit = new TextBox();
		initWidget(main);
		properties();
	}

	/**
	 */
	private void properties(){
		main.setWidth("100%");
		emailEdit.addValueChangeHandler(this);
		emailEdit.setStylePrimaryName("imogene-FormText");
	}
	
	
	
	@Override
	public void onValueChange(ValueChangeEvent<String> arg0) {		
		notifyValueChange();
	}

	@Override
	public boolean validate() {		
		if(isMandatory() && emailEdit.getText().matches("")){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		if(!emailEdit.getText().matches("") && !emailEdit.getText().matches(EMAIL_REGEX)){
			displayError(BaseNLS.constants().field_correct_email());
			return false;
		}
		hideError();
		return  true;
	}

	@Override
	public String getLabel() {
		if (thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public String getValue() {		
		return emailEdit.getText();
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(String value) {
		if (value != null && ((String) value).matches(EMAIL_REGEX)) {
			thisValue = (String) value;
			emailDisplay.setHTML(LINK_TMPL.replace("%EMAIL%", thisValue));
			emailEdit.setText(thisValue);
		}
	}

	@Override
	public void setEnabled(boolean editable) {
		main.clear();
		if(editable){
			main.add(emailEdit);
		}else{
			main.add(emailDisplay);
		}
		edited = editable;
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
}
