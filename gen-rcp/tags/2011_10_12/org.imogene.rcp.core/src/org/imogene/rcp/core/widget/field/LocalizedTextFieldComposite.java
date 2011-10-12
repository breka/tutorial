package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.LocalizedTextCombo;

public class LocalizedTextFieldComposite extends FormFieldComposite {

	protected String[] mLocales;
	
	public LocalizedTextFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent, String[] locales) {
		super(parent);
		mLocales = locales;
		setVisibilityDependent(dependent);
		createComposite(toolkit, fieldLabelText, isRequired);
	}

	@Override
	public LocalizedTextCombo getValueControl() {
		return (LocalizedTextCombo) fieldValue;
	}

	@Override
	protected void createFieldValue(FormToolkit toolkit) {
		fieldValue = new LocalizedTextCombo(this, mLocales);	
	}

	@Override
	public boolean isEmpty() {
		return getValueControl().isEmpty();
	}

	@Override
	protected boolean isExpressionValidated(String regex) {
		return getValueControl().isExpressionValidated(regex);
	}

	@Override
	protected void setEditable(boolean editable) {
		getValueControl().setEditable(editable);
	}

	@Override
	public void setReadonly(boolean readonly) {
		getValueControl().setReadOnly(readonly);
	}

}
