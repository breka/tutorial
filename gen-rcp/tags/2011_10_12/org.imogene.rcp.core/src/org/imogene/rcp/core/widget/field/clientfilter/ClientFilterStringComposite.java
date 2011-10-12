package org.imogene.rcp.core.widget.field.clientfilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.common.dao.criteria.CriteriaConstants;
import org.imogene.rcp.core.wrapper.CoreMessages;

/**
 * Widget that enables to create a string form field
 * @author MEDES-IMPS
 */
public class ClientFilterStringComposite extends ClientFilterFieldComposite {


	public ClientFilterStringComposite(Composite parent, FormToolkit toolkit, String fieldLabelText) {
		this(parent, toolkit, fieldLabelText, false);
	}
	
	public ClientFilterStringComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean dependent) {
		super(parent, toolkit, fieldLabelText, dependent);
		setValueFieldWidth(Integer.parseInt(CoreMessages.getString("form_field_string_width")));
	}

	protected  void createFieldValue(FormToolkit toolkit) {
		fieldValue = toolkit.createText(this, "");
		toolkit.paintBordersFor(this);
	}
	
	protected  void fillOperatorCombo() {
		addOperator(CriteriaConstants.STRING_OPERATOR_CONTAINS, CriteriaConstants.STRING_OPERATOR_CONTAINS);
		addOperator(CriteriaConstants.STRING_OPERATOR_STARTWITH, CriteriaConstants.STRING_OPERATOR_STARTWITH);
		addOperator(CriteriaConstants.STRING_OPERATOR_EQUAL, CriteriaConstants.STRING_OPERATOR_EQUAL);
		addOperator(CriteriaConstants.STRING_OPERATOR_DIFF, CriteriaConstants.STRING_OPERATOR_DIFF);
	}
	
	public boolean isEmpty() {
		Text widget = (Text)fieldValue;
		return ( widget.getText()==null || widget.getText().equals(""));
	}
	
	protected boolean isExpressionValidated(String regex) {
		String widgetText = ((Text)fieldValue).getText();
		if (widgetText!=null && !widgetText.equals("") && widgetText.matches(regex))
			return true;
		return false;
	}
	
	public void setReadonly(boolean readonly) {
		Text valueComp = (Text)fieldValue;
		valueComp.setEditable(!readonly);
		if (readonly) 
			valueComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			valueComp.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	}
	
	public void setEditable(boolean editable) {
		((Text)fieldValue).setEditable(editable);
	}

	public Text getValueControl() {
		return (Text)fieldValue;
	}
	
	
	

	
	
	
}
