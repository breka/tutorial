package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextArea;


public class ImogTextArea extends ImogFieldAbstract<String> {

	/* status - behavior */
	private String thisLabel;

	private String thisValue;
	
	private boolean edited = false;
	
	/* widgets */

	private HorizontalPanel layout;

	private Image errorImage;

	private TextArea textArea;

	public ImogTextArea() {
		layout();
		properties();
	}

	private void layout() {
		layout = new HorizontalPanel();
		errorImage = new Image(GWT.getModuleBaseURL());
		textArea = new TextArea();
		layout.add(errorImage);
		layout.add(textArea);
		initWidget(layout);
	}

	private void properties() {
		layout.setCellWidth(errorImage, "16px");
		layout.setCellWidth(textArea, "100%");
		errorImage.setVisible(false);
		textArea.setStylePrimaryName("imogene-FormTextArea");
		textArea.addChangeHandler(new ChangeHandler() {						
			@Override
			public void onChange(ChangeEvent arg0) {				
				notifyValueChange();
			}
		});
	}

	public TextArea getEmbedded() {
		return textArea;
	}

	public void setInError(boolean inError) {
		if (inError)
			textArea.addStyleDependentName("error");
		else
			textArea.removeStyleDependentName("error");
	}

	public void setEnabled(boolean enabled) {
		textArea.setEnabled(enabled);
		if (!enabled) {
			textArea.addStyleDependentName("disabled");
		} else {
			textArea.removeStyleDependentName("disabled");
		}
		edited = enabled;
	}

	@Override
	public boolean validate() {
		if(isMandatory() && textArea.getText().equals("")){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		if(!textArea.getText().equals("")){
			for(ValidationRule rule : rules){
				if(!textArea.getText().matches(rule.getRegex())){
					displayError(rule.getText());
					return false;
				}
			}
		}		
		hideError();
		return true;
	}

	@Override
	public String getLabel() {
		if (thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public String getValue() {
		return textArea.getText();
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(String value) {
		thisValue = (String) value;
		textArea.setText(thisValue);
	}
	
	@Override
	public void setValue(String value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
}
