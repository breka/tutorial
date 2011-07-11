package org.imogene.web.gwt.client.ui.field;

import java.util.List;
import java.util.Vector;

import org.imogene.web.gwt.client.i18n.BaseNLS;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Composite to manage the display of enumeration fields
 * @author Medes-IMPS
 */
public class ImogEnumField extends ImogFieldAbstract<String> implements ValueChangeHandler<Boolean>, ChangeHandler{

	/* status - behavior */
	private String thisLabel;	
	private boolean edited = false;	
	private boolean thisMultiple = false;	
	private boolean listWidget = false;
	
	/* widget */	
	private EnumFieldPanelList fieldPanel;		
	private ListBox listChoices;
	
	
	/**
	 * @param multiple true if multiple selection are allowed
	 */
	public ImogEnumField(boolean multiple){
		this(multiple, false);
	}
	
	/**
	 * @param multiple true if multiple selection are allowed
	 * @param list true if single selection are presented as a list.
	 */
	public ImogEnumField(boolean multiple, boolean list){
		thisMultiple = multiple;
		if(multiple)
			listWidget=false;
		else
			listWidget = list;
		layout();
	}
	
	/**
	 * @param multiple true if multiple selection are allowed	 
	 * @param label The field label
	 */
	public ImogEnumField(boolean multiple, String label){
		this(multiple);
		thisLabel = label;
	}
	
	/**
	 * @param multiple true if multiple selection are allowed
	 * @param list true if single enumeration have to be displayed in a list box.
	 * @param label The field label
	 */
	public ImogEnumField(boolean multiple, boolean list, String label){
		this(multiple, list);
		thisLabel = label;
	}
	
	/**
	 */
	private void layout(){
		if(!listWidget){
			fieldPanel = new EnumFieldPanelList();
			fieldPanel.setStylePrimaryName("imogene-FormText");
			initWidget(fieldPanel);
		}
		else{
			listChoices = new ListBox();
			listChoices.addItem(BaseNLS.constants().enumeration_unknown(), "");
			listChoices.setSelectedIndex(0);
			listChoices.addChangeHandler(this);
			listChoices.setStylePrimaryName("imogene-FormText");
			initWidget(listChoices);
		}
	}
	
	/**
	 * Is this field allowing a multiple selection
	 * @return true is multiple selection is allowed
	 */
	public boolean isMultiple(){
		return thisMultiple;
	}
	
	/**
	 * Add an item to the enumeration field.
	 * @param label the item label
	 * @param value the item value.
	 */
	public void addItem(String label, String value){	
		CheckBox box;
		if (!listWidget) {
			if (thisMultiple) {
				box = new CheckBox();
			} else {
				box = new RadioButton(String.valueOf(hashCode()));
			}
			box.setText(label);
			box.setFormValue(value);
			box.addValueChangeHandler(this);
			fieldPanel.addField(box);
		}else{
			listChoices.addItem(label, value);
		}
	}	
	
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> arg0) {
		notifyValueChange();
	}		

	@Override
	public void onChange(ChangeEvent arg0) {
		notifyValueChange();		
	}

	@Override
	public boolean validate() {
		
		if(isMandatory() && !listWidget && fieldPanel.getValue().equals("")){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		if(isMandatory() && listWidget && listChoices.getSelectedIndex()==0){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		hideError();
		return true;
	}

	@Override
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public String getValue() {
		if(!listWidget)
			return fieldPanel.getValue();
		else
			return listChoices.getValue(listChoices.getSelectedIndex());
	}
	
	public String getDisplayValue() {
		if(!listWidget)
			return fieldPanel.getDisplayValue();
		else
			return listChoices.getItemText(listChoices.getSelectedIndex());
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(String value) {	
		if(!listWidget)
			fieldPanel.setValue(value);
		else{
			listChoices.setSelectedIndex(0);
			for(int i=0; i<listChoices.getItemCount(); i++){
				if(listChoices.getValue(i).equals(value)){
					listChoices.setSelectedIndex(i);
					return;
				}
			}
		}	
	}
	
	@Override
	public void setValue(String value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public void setEnabled(boolean editable) {
		if(!listWidget)
			fieldPanel.setEnabled(editable);
		else
			listChoices.setEnabled(editable);
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
	/* INTERNAL CLASSES */
	
	/**
	 * Embedded panel that 
	 * display each selectable item.
	 */
	private class EnumFieldPanelList extends VerticalPanel {
		
		private List<CheckBox> choices = new Vector<CheckBox>();
		
		/**
		 * Add an choice item widget
		 * @param choice the widget to add
		 */
		public void addField(CheckBox choice){
			choices.add(choice);
			choice.setEnabled(edited);
			this.add(choice);
		}
		
		/**
		 * Enable or disable all widgets in the panel.
		 * @param editable true to edit the panel
		 */		
		public void setEnabled(boolean editable){
			for(CheckBox choice: choices){
				choice.setEnabled(editable);
			}
		}
		
		/**
		 * Get the string value representation
		 * of the selected widgets
		 * @return value string representation
		 */
		public String getValue(){
			StringBuffer result = new StringBuffer();
			for(CheckBox choice : choices){
				if(choice.getValue()){
					result.append(choice.getFormValue());
					result.append(";");
				}
			}
			String str = result.toString();
			if(str.length()>0)
				return str.substring(0, str.length()-1);
			return "";
		}
		
		/**
		 * 
		 * @return
		 */
		public String getDisplayValue(){
			StringBuffer result = new StringBuffer();
			for(CheckBox choice : choices){
				if(choice.getValue()){
					result.append(choice.getText());
					result.append(";");
				}
			}
			String str = result.toString();
			if(str.length()>0)
				return str.substring(0, str.length()-1);
			return "";
		}
		
		/**
		 * Set the value of the enum field.
		 * @param value
		 */
		public void setValue(String value){
			if(value == null){
				for(CheckBox choice : choices)
					choice.setValue(false);
			}else{
				String[] values = value.split(";");
				for(String formValue : values){
					for(CheckBox choice : choices){
						if(formValue.equals(choice.getFormValue()))
							choice.setValue(true);
					}
				}
			}
		}
		
		/**
		 */
		public void clear(){
			for(CheckBox choice : choices){
				remove(choice);
			}
		}
		
	}	
	
	
	/**
	 * Embedded panel that 
	 * display each selectable item.
	 */
	@SuppressWarnings("unused")
	private class EnumFieldPanelTable extends FlexTable {
		
		private static final int MAX_COL=3;
		
		private List<CheckBox> choices = new Vector<CheckBox>();
		
		/**
		 * Add an choice item widget
		 * @param choice the widget to add
		 */
		public void addField(CheckBox choice){
			choices.add(choice);
			choice.setEnabled(edited);
			int rowCount = getRowCount();
			if(rowCount==0){
				setWidget(0, 0, choice);
			}else{
				int colCount = getCellCount(rowCount-1);
				if(colCount<MAX_COL)
					setWidget(rowCount-1, colCount, choice);
				else
					setWidget(rowCount, 0, choice);
			}
		}
		
		/**
		 * Enable or disable all widgets in the panel.
		 * @param editable true to edit the panel
		 */		
		public void setEnabled(boolean editable){
			for(CheckBox choice: choices){
				choice.setEnabled(editable);
			}
		}
		
		/**
		 * Get the string value representation
		 * of the selected widgets
		 * @return value string representation
		 */
		public String getValue(){
			StringBuffer result = new StringBuffer();
			for(CheckBox choice : choices){
				if(choice.getValue()){
					result.append(choice.getFormValue());
					result.append(";");
				}
			}
			String str = result.toString();
			if(str.length()>0)
				return str.substring(0, str.length()-1);
			return "";
		}
		
		/**
		 * Set the value of the enum field.
		 * @param value
		 */
		public void setValue(String value){
			if(value == null){
				for(CheckBox choice : choices)
					choice.setValue(false);
			}else{
				String[] values = value.split(";");
				for(String formValue : values){
					for(CheckBox choice : choices){
						if(formValue.equals(choice.getFormValue()))
							choice.setValue(true);
					}
				}
			}
		}
		
		/**
		 */
		public void clear(){
			for(CheckBox choice : choices){
				remove(choice);
			}
		}
		
	}

}
