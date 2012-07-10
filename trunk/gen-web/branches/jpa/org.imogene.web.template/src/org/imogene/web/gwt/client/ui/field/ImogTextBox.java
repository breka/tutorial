package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;

public class ImogTextBox extends ImogFieldAbstract<String> {

	/* status - behavior */
	private String thisLabel;
	private String thisValue;
	private boolean edited = false;

	/* widgets */
	private HorizontalPanel layout;
	private Image errorImage;
	private TextBox textBox;

	public ImogTextBox() {
		layout();
		properties();
	}

	private void layout() {
		layout = new HorizontalPanel();
		errorImage = new Image(GWT.getModuleBaseURL());
		textBox = new TextBox();
		layout.add(errorImage);
		layout.add(textBox);
		initWidget(layout);
	}

	private void properties() {
		layout.setCellWidth(textBox, "100%");
		errorImage.setVisible(false);
		textBox.setStylePrimaryName("imogene-FormText");
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				notifyValueChange();
			}
		});
	}

	public TextBox getEmbedded() {
		return textBox;
	}

	public void setInError(boolean inError) {
		if (inError)
			textBox.addStyleDependentName("error");
		else
			textBox.removeStyleDependentName("error");
	}

	public void setEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
		if (!enabled) {
			textBox.addStyleDependentName("disabled");
		} else {
			textBox.removeStyleDependentName("disabled");
		}
		edited = enabled;
	}

	@Override
	public boolean validate() {
		if (isMandatory() && textBox.getText().equals("")) {
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		/* validation rules */
		if (!textBox.getText().equals("")) {
			for (ValidationRule rule : rules) {
				if (!textBox.getText().matches(rule.getRegex())) {
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
		return textBox.getText();
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(String value) {
		if (value == null)
			value = "";
		thisValue = (String) value;
		textBox.setText(thisValue);
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
