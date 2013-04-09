package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.DateText;


/**
 * Widget that enables to create a time form field
 * @author MEDES-IMPS
 */
public class TimeFieldComposite extends DateFieldComposite {

	public TimeFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}

	public TimeFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new DateText(this, true, SWT.TIME);
	}
	
}
