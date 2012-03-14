package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.IntegerInputValidator;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * Widget that enables to create a integer form field
 * @author MEDES-IMPS
 */
public class IntegerFieldComposite extends StringFieldComposite {
	
	public IntegerFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}
	
	public IntegerFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
		
		((Text)fieldValue).addVerifyListener(new IntegerInputValidator());
		setValueFieldWidth(Integer.parseInt(CoreMessages.getString("form_field_int_width")));
	}
	
	
}
