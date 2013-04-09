package org.imogene.rcp.core.widget.field;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.RelationPaginatedCombo;


/**
 * Widget that enables to create a simple relation 
 * form field with paginated list
 * @author Medes-IMPS
 */
public class SimpleRelationFieldPaginatedListComposite extends FormFieldComposite {

	private String className;
	private String formID;
	private String sortField;
	private boolean sortOrder;

	
	public SimpleRelationFieldPaginatedListComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, String className, String formID, String sortField, boolean sortOrder) {
		super(parent);
		this.className = className;
		this.formID = formID;
		this.sortField = sortField;
		this.sortOrder = sortOrder;
		createComposite(toolkit, fieldLabelText, isRequired);
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = new RelationPaginatedCombo(this, toolkit, className, formID, sortField, sortOrder);		
	}
	
	public boolean isEmpty() {
		RelationPaginatedCombo widget = (RelationPaginatedCombo)fieldValue;
		return (widget.getSelected()==null);
	}	
	
	protected boolean isExpressionValidated(String regex) {
		return true;
	}
	
	public void setEditable(boolean editable) {
		((RelationPaginatedCombo)fieldValue).setEnabled(editable);
	}
	
	public RelationPaginatedCombo getValueControl() {
		return (RelationPaginatedCombo)fieldValue;
	}
	
	public void setReadonly(boolean readonly) {
		setEditable(!readonly);
	}
	
	

	
	
	
}
