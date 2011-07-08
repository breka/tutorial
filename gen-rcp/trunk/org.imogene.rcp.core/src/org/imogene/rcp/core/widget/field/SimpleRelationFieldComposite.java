package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.RelationCombo;


/**
 * Widget that enables to create a simple relation form field
 * @author MEDES-IMPS
 */
public class SimpleRelationFieldComposite extends FormFieldComposite {

	private String className;
	private String formID;

	
	public SimpleRelationFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, String className, String formID) {
		super(parent);
		this.className = className;
		this.formID = formID;
		createComposite(toolkit, fieldLabelText, isRequired);
	}

	protected void createFieldValue(FormToolkit toolkit) {
		fieldValue = new RelationCombo(this, true, className, formID);	
	}
	
	public boolean isEmpty() {
		RelationCombo widget = (RelationCombo)fieldValue;
		return (widget.getSelected()==null);
	}	
	
	protected boolean isExpressionValidated(String regex) {
		return true;
	}
	
	public void setEditable(boolean editable) {
		((RelationCombo)fieldValue).setEnabled(editable);
	}
	
	public RelationCombo getValueControl() {
		return (RelationCombo)fieldValue;
	}
	
	public void setReadonly(boolean readonly) {
		setEditable(!readonly);
	}
	
	

	
	
	
}
