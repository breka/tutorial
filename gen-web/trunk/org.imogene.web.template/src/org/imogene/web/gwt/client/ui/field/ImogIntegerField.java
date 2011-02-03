package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.util.NumericUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;


public class ImogIntegerField extends ImogFieldAbstract<Integer> {

	private static final String INT_REGEX = "([+-]?[0-9]+)";
	
	/* status - behavior */
	private String thisLabel;
	private Integer thisValue;
	private boolean edited = false;
	
	/* widgets */
	private HorizontalPanel layout;
	private Image errorImage;
	private TextBox textBox;
	private Label unit;
	private Integer minimum=null;
	private Integer maximum=null;

	public ImogIntegerField() {
		layout();
		properties();
	}
	
	private void layout() {
		layout = new HorizontalPanel();
		errorImage = new Image(GWT.getModuleBaseURL());
		unit = new Label();
		textBox = new TextBox();
		layout.add(errorImage);
		layout.add(textBox);
		layout.add(unit);
		initWidget(layout);
	}

	private void properties() {
		errorImage.setVisible(false);
		//layout.setCellWidth(errorImage, "16px");
		
		layout.setCellWidth(textBox, "100%");
		layout.setCellVerticalAlignment(textBox, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(textBox, HasHorizontalAlignment.ALIGN_LEFT);
		textBox.setStylePrimaryName("imogene-FormText");
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {			
			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				notifyValueChange();
			}
		});
		
		unit.setStylePrimaryName("imogene-FormText-unit");
		layout.setCellVerticalAlignment(unit, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(unit, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	/**
	 * Set the minimum constraint
	 * @param min the constraint value
	 */
	public void setMinimum(int min){
		minimum = Integer.valueOf(min);
	}
	
	/**
	 * Set the maximum constraint
	 * @param max the constraint value
	 */
	public void setMaximum(int max){
		maximum = Integer.valueOf(max);
	}
			
	@Override
	public String getLabel() {
		if (thisLabel != null)
			return thisLabel;
		return "";		
	}

	@Override
	public Integer getValue() {		
		return NumericUtil.parseToInteger(textBox.getText());
	}

	@Override
	public boolean isEdited() {
		return edited;
	}

	@Override
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
	public void setLabel(String label) {
		thisLabel = label;		
	}
	
	/**
	 * Set the unit label.
	 * @param strUnit the unit label
	 */
	public void setUnit(String strUnit){
		unit.setText(strUnit);
	}

	@Override
	public void setValue(Integer value) {		
		thisValue = (Integer) value;
		textBox.setText(NumericUtil.parseToString(thisValue));
	}

	@Override
	public boolean validate() {
		if(isMandatory() && textBox.getText().equals("")){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		if(!textBox.getText().matches("") && !textBox.getText().matches(INT_REGEX)){
			displayError(BaseNLS.constants().field_correct_integer());
			return false;
		}
		if(!textBox.getText().matches("")){
			Integer intStr = Integer.valueOf(textBox.getText()); 
			if(minimum!=null && intStr<minimum){				
				displayError(rangeErrorMessage());
				return false;
			}
			if(maximum!=null && intStr>maximum){
				displayError(rangeErrorMessage());
				return false;
			}
		}
		hideError();
		return true;
	}
	
	private String rangeErrorMessage(){
		if(minimum!=null && maximum==null)
			return BaseNLS.messages().error_num_min(String.valueOf(minimum));
		if(minimum==null && maximum!=null)
			return BaseNLS.messages().error_num_max(String.valueOf(maximum));
		return BaseNLS.messages().error_num_range(String.valueOf(minimum), String.valueOf(maximum));
	}
	
}
