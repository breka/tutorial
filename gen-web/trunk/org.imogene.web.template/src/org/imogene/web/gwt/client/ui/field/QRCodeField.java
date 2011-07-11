package org.imogene.web.gwt.client.ui.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;

public class QRCodeField extends ImogFieldAbstract<String>{

	private Image barcode;
	
	public QRCodeField(){
		barcode = new Image();
		initWidget(barcode);
	}
	
	@Override
	public String getLabel() {		
		return "";
	}

	@Override
	public String getValue() {		
		return null;
	}

	@Override
	public boolean isEdited() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setEnabled(boolean editable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLabel(String label) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String value) {		
		barcode.setUrl(GWT.getHostPageBaseURL()+"getbinary?barcodeId="+value);
	}
	
	@Override
	public void setValue(String value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
