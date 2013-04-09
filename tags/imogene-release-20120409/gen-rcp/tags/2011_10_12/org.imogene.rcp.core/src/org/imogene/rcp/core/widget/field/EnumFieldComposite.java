package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.EnumSelect;


/**
 * Widget that enables to create a enum form field
 * @author MEDES-IMPS
 */
public class EnumFieldComposite extends FormFieldComposite {
	
	public EnumFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}

	public EnumFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new EnumSelect(this, false, true);	
	}
	
	public boolean isEmpty() {
		EnumSelect widget = (EnumSelect)fieldValue;
		return ( widget.getResult()==null || widget.getResult().equals(""));
	}
	
	
	protected boolean isExpressionValidated(String regex) {
		String widgetText = ((EnumSelect)fieldValue).getResult();
		if (widgetText!=null && !widgetText.equals("") && widgetText.matches(regex))
			return true;
		return false;
	}
	
	public void setEditable(boolean editable) {
		((EnumSelect)fieldValue).setEnabled(editable);
	}
	
	public void setReadonly(boolean readonly) {
		setEditable(!readonly);
	}
	
	public EnumSelect getValueControl() {
		return (EnumSelect)fieldValue;
	}
	
	
	

	
	
	
}
