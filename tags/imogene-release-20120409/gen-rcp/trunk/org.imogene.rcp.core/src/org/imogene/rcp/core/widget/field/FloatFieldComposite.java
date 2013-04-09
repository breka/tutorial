package org.imogene.rcp.core.widget.field;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.widget.RealInputValidator;
import org.imogene.rcp.core.wrapper.CoreMessages;


/**
 * Widget that enables to create a float form field
 * @author MEDES-IMPS
 */
public class FloatFieldComposite extends StringFieldComposite {
	
	private int decimalsCount;

	public FloatFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired) {
		this(parent, toolkit, fieldLabelText, isRequired, false);
	}
	
	public FloatFieldComposite(Composite parent, FormToolkit toolkit, String fieldLabelText, boolean isRequired, boolean dependent) {
		super(parent, toolkit, fieldLabelText, isRequired, dependent);
		
		((Text)fieldValue).addVerifyListener(new RealInputValidator());
		setValueFieldWidth(Integer.parseInt(CoreMessages.getString("form_field_float_width")));
	}

	public int getDecimalsCount() {
		return decimalsCount;
	}

	public void setDecimalsCount(int decimalsCount) {
		this.decimalsCount = decimalsCount;
	}
		
	public void setFormatedValue(float floatValue){
		NumberFormat formatter = new DecimalFormat("0.00");
		String toDisplay = formatter.format(floatValue).replace(",", ".");
		getValueControl().setText("");		
		getValueControl().setText(toDisplay);
	}
	
	

	
}
