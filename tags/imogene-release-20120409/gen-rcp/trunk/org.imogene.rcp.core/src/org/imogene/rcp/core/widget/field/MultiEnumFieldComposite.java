package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.EnumSelect;


/**
 * Widget that enables to create a enum form field
 * @author MEDES-IMPS
 */
public class MultiEnumFieldComposite extends EnumFieldComposite {

	public MultiEnumFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}

	public MultiEnumFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new EnumSelect(this, true, true);	
	}

}
