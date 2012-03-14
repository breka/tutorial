package org.imogene.rcp.core.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.imogene.rcp.core.wrapper.CoreMessages;


public class BooleanRadio extends Composite {

	private FormToolkit toolkit;
	
	private Button yesButton;
	
	private Button noButton;
	
	private Button ukButton;
		
	
	public BooleanRadio(Composite parent){
		this(parent, false);
	}
	
	public BooleanRadio(Composite parent, boolean formStyle){
		super(parent, SWT.NONE);
		setLayout(new GridLayout(3, false));
		if(formStyle)
			createFormWidgets();
		else
			createStandardWidgets();
		
	}
	
	private void createStandardWidgets(){
		ukButton = new Button(this, SWT.RADIO);
		ukButton.setText(CoreMessages.getString("boolean_unknown"));		
		yesButton = new Button(this, SWT.RADIO);
		yesButton.setText(CoreMessages.getString("boolean_true"));
		noButton = new Button(this, SWT.RADIO);
		noButton.setText(CoreMessages.getString("boolean_false"));
	}
	
	private void createFormWidgets(){
		toolkit = new FormToolkit(getDisplay());
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		ukButton = toolkit.createButton(this, CoreMessages.getString("boolean_unknown"), SWT.RADIO);
		yesButton = toolkit.createButton(this, CoreMessages.getString("boolean_true"), SWT.RADIO);
		noButton = toolkit.createButton(this, CoreMessages.getString("boolean_false"), SWT.RADIO);
		toolkit.paintBordersFor(this);
	}
	
	public Boolean getResult(){
		if(yesButton.getSelection()) return new Boolean(true);
		else if (noButton.getSelection()) return new Boolean(false);
		else return null;
	}
	
	public void setResult(Boolean value){
		if (value!=null) {
			if(value){
				yesButton.setSelection(true);
				noButton.setSelection(false);
				ukButton.setSelection(false);
			}		
			else {
				noButton.setSelection(true);
				yesButton.setSelection(false);
				ukButton.setSelection(false);
			}			
		}
		else {
			noButton.setSelection(false);
			yesButton.setSelection(false);
			ukButton.setSelection(true);			
		}

	}
	
	public void setEditable(boolean editable){
		ukButton.setEnabled(editable);
		noButton.setEnabled(editable);
		yesButton.setEnabled(editable);
	}
	
	/**
	  * Add a selection listener 
	  * @param listener the listener to add
	  */
	 public void addSelectionListener(SelectionListener listener){
		 ukButton.addSelectionListener(listener);
		 yesButton.addSelectionListener(listener);
		 noButton.addSelectionListener(listener);
	 }

	 /**
	  * Remove the selection listener
	  * @param listener the listener to remove
	  */
	 public void removeSelectionListener(SelectionListener listener){
		 ukButton.removeSelectionListener(listener);
		 yesButton.removeSelectionListener(listener);
		 noButton.removeSelectionListener(listener);
	 }
	
	@Override
	 public boolean isFocusControl() {  
		return (ukButton.isFocusControl() || yesButton.isFocusControl() || noButton.isFocusControl() || super.isFocusControl());
	 }
}
