package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * Widget that enables to create a string form field
 * @author MEDES-IMPS
 */
public class StringFieldComposite extends FormFieldComposite {


	public StringFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}
	
	public StringFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
		setValueFieldWidth(Integer.parseInt(CoreMessages.getString("form_field_string_width")));
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = toolkit.createText(this, "");
		toolkit.paintBordersFor(this);
	}
	
	public boolean isEmpty() {
		Text widget = (Text)fieldValue;
		return ( widget.getText()==null || widget.getText().equals(""));
	}
	
	protected boolean isExpressionValidated(String regex) {
		String widgetText = ((Text)fieldValue).getText();
		if (widgetText!=null && !widgetText.equals("") && widgetText.matches(regex))
			return true;
		return false;
	}
	
	public void setReadonly(boolean readonly) {
		Text valueComp = (Text)fieldValue;
		valueComp.setEditable(!readonly);
		if (readonly) 
			valueComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			valueComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setEditable(boolean editable) {
		((Text)fieldValue).setEditable(editable);
	}

	public Text getValueControl() {
		return (Text)fieldValue;
	}
	

	
	
	
}
