package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.MultiRelationCombo;


/**
 * Widget that enables to create a Relation 
 * form field when cardinality = n
 * @author Medes-IMPS
 */
public class MultiRelationFieldComposite extends FormFieldComposite {

	private String formID;

	
	public MultiRelationFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired,String formID) {
		super(parent);
		this.formID = formID;
		createComposite(toolkit, fieldLabelText, isRequired);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new MultiRelationCombo(this, formID);	
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	protected boolean isExpressionValidated(String regex) {
		return true;
	}
	
	public void setEditable(boolean editable) {
		((MultiRelationCombo)fieldValue).setEnabled(editable);
	}
	
	public void setReadonly(boolean readonly) {
	}
	
	public MultiRelationCombo getValueControl() {
		return (MultiRelationCombo)fieldValue;
	}
	

	
	
	
}
