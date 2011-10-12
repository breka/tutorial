package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.BinaryText;


/**
 * Widget that enables to create a binary form field
 * @author MEDES-IMPS
 */
public class BinaryFieldComposite extends FormFieldComposite {

	public BinaryFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}
	
	public BinaryFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new BinaryText(this, true);	
	}
	
	public boolean isEmpty() {
		BinaryText widget = (BinaryText)fieldValue;
		return ( widget.getFile()==null );
	}	
	
	protected boolean isExpressionValidated(String regex) {
		return true;
	}
	
	public void setEditable(boolean editable) {
		((BinaryText)fieldValue).setEditable(editable);
	}
	
	public void setReadonly(boolean readonly) {
		((BinaryText)fieldValue).setReadonly(readonly);;
	}

	public BinaryText getValueControl() {
		return (BinaryText)fieldValue;
	}
	

	
	
	
}
