package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;


/**
 * Widget that enables to create a string form field
 * @author MEDES-IMPS
 */
public class TextFieldComposite extends StringFieldComposite {

	public TextFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}
		
	public TextFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
		((GridData)fieldValue.getLayoutData()).heightHint=80;
		toolkit.paintBordersFor(this);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = toolkit.createText(this, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);			
	}

}
