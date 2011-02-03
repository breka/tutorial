package org.imogene.rcp.core.widget.field.clientfilter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.common.dao.criteria.CriteriaConstants;

/**
 * Widget that enables to create a Relation 
 * form field when cardinality = n
 * @author Medes-IMPS
 */
public class ClientFilterRelationFieldComposite extends ClientFilterFieldComposite {

	
	public ClientFilterRelationFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText) {
		super(parent);
		createComposite(toolkit, fieldLabelText);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new ClientFilterRelationCombo(this);	
	}
	
	protected  void fillOperatorCombo() {
		addOperator(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL, CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL);
		addOperator(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL, CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL);
	}
	
	public boolean isEmpty() {
		return false;
	}
	
	protected boolean isExpressionValidated(String regex) {
		return true;
	}
	
	public void setEditable(boolean editable) {
		((ClientFilterRelationCombo)fieldValue).setEnabled(editable);
	}
	
	public void setReadonly(boolean readonly) {
	}
	
	public ClientFilterRelationCombo getValueControl() {
		return (ClientFilterRelationCombo)fieldValue;
	}
	

	
	
	
}
