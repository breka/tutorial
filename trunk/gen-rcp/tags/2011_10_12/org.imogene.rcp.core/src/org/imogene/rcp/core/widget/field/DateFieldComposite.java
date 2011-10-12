package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.DateText;


/**
 * Widget that enables to create a date form field
 * @author MEDES-IMPS
 */
public class DateFieldComposite extends FormFieldComposite {
	
	public DateFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}

	public DateFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new DateText(this, true);
	}
	
	public boolean isEmpty() {
		DateText widget = (DateText)fieldValue;
		return ( widget.getDate()==null );
	}
	
	protected boolean isExpressionValidated(String regex) {
		return true;
	}
	
	public DateText getValueControl() {
		return (DateText)fieldValue;
	}
	
	public void setEditable(boolean editable) {
		((DateText)fieldValue).setEditable(editable);
	}
	
	public void setReadonly(boolean readonly) {
		((DateText)fieldValue).setReadonly(readonly);;
	}
	

	
	
	
}
