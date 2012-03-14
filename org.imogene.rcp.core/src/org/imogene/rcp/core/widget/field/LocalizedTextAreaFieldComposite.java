package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.LocalizedTextCombo;

public class LocalizedTextAreaFieldComposite extends LocalizedTextFieldComposite {

	public LocalizedTextAreaFieldComposite(Composite parent,
			FormToolkit toolkit, String fieldLabelText, boolean isRequired,
			boolean dependent, String[] locales) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent, locales);
	}
	
	@Override
	protected void createFieldValue(FormToolkit toolkit) {
		fieldValue = new LocalizedTextCombo(this, mLocales, true);	
	}

}
