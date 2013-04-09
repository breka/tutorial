package org.imogene.rcp.core.widget;

import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Widget that permits to display and edit an EnumField
 * @author yann
 *
 */
public class EnumSelect extends Composite {
	
	private static String VALUE_SEPARATOR=";";

	private boolean multi;
	
	private boolean formStyle;
	
	private FormToolkit toolkit;
	
	// widgets for drop down list
	private Combo combo;
	//private Text selectedItem;
	
	// widgets for multi selection
	private Group group;
	private List<Button> buttons;
	
	public EnumSelect(Composite parent, boolean multi){
		this(parent, multi, false);
	}
	
	public EnumSelect(Composite parent, boolean multi, boolean formStyle){
		super(parent, SWT.NONE);
		this.multi = multi;
		this.formStyle = formStyle;
		if(multi) buttons = new Vector<Button>();
		
		setLayout(new FillLayout());		
		if(formStyle){
			createFormWidgets();
		}
		else{
			createStandardWidgets();
		}
	}
	
	/**
	 * Create standard style widgets
	 */
	private void createStandardWidgets(){
		if(multi){
			group = new Group(this, SWT.NONE);
			group.setLayout(new GridLayout(2, false));
		}
		else{
			combo = new Combo(this, SWT.SINGLE|SWT.READ_ONLY);
			combo.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					combo.getParent().setFocus();
				}
				
			});
			//selectedItem = new Text(this, SWT.NONE);
		}
	}
	
	/**
	 * Create form style widgets
	 */
	private void createFormWidgets(){
		toolkit = new FormToolkit(getDisplay());
		this.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if(multi){
			group = new Group(this, SWT.NONE);		
			group.setLayout(new GridLayout(2, false));
			group.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		}else{
			combo = new Combo(this, SWT.SINGLE|SWT.READ_ONLY);
			//selectedItem = toolkit.createText(this, null);
		}
		toolkit.paintBordersFor(this);
	}
	
	/**
	 * Add a value choice to this enumeration.
	 * @param display the string to display
	 * @param value the associated value
	 */
	public void addValue(String display, String value){
		if(multi){
			Button button;
			if(!formStyle){
				button = new Button(group, SWT.CHECK);
				button.setText(display);
			}
			else{
				button = toolkit.createButton(group, display, SWT.CHECK);				
			}
			button.setData(value);
			buttons.add(button);
		}else{
			combo.add(display);
			combo.setData(String.valueOf(combo.getItemCount()-1), value);
		}
	}
	
	/**
	 * Set the values to display
	 * @param result the values to display
	 */
	public void setResult(String result){
		if(multi){
			String[] choices = result.split(VALUE_SEPARATOR);
			for(String choice:choices){
				for(Button button:buttons){	
					if(choice.equals(button.getData())){
						button.setSelection(true);
					}
				}
			}
		}
		else{
			for(int i=0; i<combo.getItemCount();i++){
				String data = (String)combo.getData(String.valueOf(i));
				if(data.equals(result)){
					combo.select(i);
					//selectedItem.setText(combo.getItem(i));
					break;
				}
			}
		}
	}
	
	/**
	 * Get the selected values 
	 * @return return the values as string
	 */
	public String getResult(){
		if(multi){
			StringBuffer buffer = new StringBuffer();
			for(Button button:buttons){
				if(button.getSelection())
					buffer.append(button.getData()).append(VALUE_SEPARATOR);
			}
			return buffer.toString();
		}else{
			return (String)combo.getData(String.valueOf(combo.getSelectionIndex()));
		}
	}
	
	/**
	  * Add a selection listener 
	  * @param listener the listener to add
	  */
	 public void addSelectionListener(SelectionListener listener){
		 if(!multi)
			 combo.addSelectionListener(listener);
		 else {
				for(Button button:buttons){
					button.addSelectionListener(listener);
				}
		 }
	 }

	 /**
	  * Remove the selection listener
	  * @param listener the listener to remove
	  */
	 public void removeSelectionListener(SelectionListener listener){
		 if(!multi)
			 combo.removeSelectionListener(listener);
		 else {
				for(Button button:buttons){
					button.removeSelectionListener(listener);
				}
		 }
	 }
	 
	 @Override
	 public boolean isFocusControl() { 
		 if(!multi)
			 return (combo.isFocusControl() || super.isFocusControl());
		 else {
			 boolean isFocused = super.isFocusControl();
				for(Button button:buttons){
					isFocused = isFocused || button.isFocusControl();
				}
				return isFocused;
		 }
	 }

	@Override
	public void setEnabled(boolean enabled) {
		if (!multi) {
			combo.setEnabled(enabled);
			// ((GridData)combo.getLayoutData()).exclude = !enabled;
			// ((GridData)selectedItem.getLayoutData()).exclude = enabled;
			// this.layout(true,true);

		} else {
			for (Button button : buttons) {
				button.setEnabled(enabled);
			}
		}
	}

	@Override
	public boolean setFocus() {
		/* return getParent().setFocus(); */
		return false;
	}

}
