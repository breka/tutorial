package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.BooleanRadio;


/**
 * Widget that enables to create a boolean form field
 * @author MEDES-IMPS
 */
public class BooleanFieldComposite extends FormFieldComposite {

	public BooleanFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}
	
	public BooleanFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		
		fieldValue = new BooleanRadio(this, true);	
	}
	
	public boolean isEmpty() {		
		return ((BooleanRadio)fieldValue).getResult()==null;
	}	
	
	protected boolean isExpressionValidated(String regex) {
		return regex.matches(String.valueOf(((BooleanRadio)fieldValue).getResult()));
	}
	
	public void setEditable(boolean editable) {
		((BooleanRadio)fieldValue).setEditable(editable);
	}
	
	public BooleanRadio getValueControl() {
		return (BooleanRadio)fieldValue;
	}

	public void setReadonly(boolean readonly) {
		setEditable(!readonly);
	}
	
	
	

	
	
	
}
