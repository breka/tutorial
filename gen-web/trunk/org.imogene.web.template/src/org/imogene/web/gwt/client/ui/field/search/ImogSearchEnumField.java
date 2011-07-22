package org.imogene.web.gwt.client.ui.field.search;

import java.util.List;
import java.util.Vector;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.common.criteria.CriteriaConstants;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Composite to search Enum fields
 * @author Medes-IMPS
 */
public class ImogSearchEnumField extends ImogSearchFieldAbstract<String> {

	/* status - behavior */
	private boolean thisMultiple = false;	
	private boolean listWidget = false;
	
	/* widget */	
	private EnumFieldPanelList fieldPanel;		
	private ListBox listChoices;
	
	
	/**
	 * @param multiple true if multiple selection are allowed
	 */
	public ImogSearchEnumField(boolean multiple){
		this(multiple, false);
	}
	
	/**
	 * @param multiple true if multiple selection are allowed
	 * @param list true if single selection are presented as a list.
	 */
	public ImogSearchEnumField(boolean multiple, boolean list){
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
	public ImogSearchEnumField(boolean multiple, String label){
		this(multiple);
		thisLabel = label;
	}
	
	/**
	 * @param multiple true if multiple selection are allowed
	 * @param list true if single enumeration have to be displayed in a list box.
	 * @param label The field label
	 */
	public ImogSearchEnumField(boolean multiple, boolean list, String label){
		this(multiple, list);
		thisLabel = label;
	}
	
	/**
	 */
	private void layout(){
		fillOperatorValues();
		
		if(!listWidget){
			fieldPanel = new EnumFieldPanelList();
			fieldPanel.setStylePrimaryName("imogene-FormText");
			addValueWidget(fieldPanel);
			layout.setCellVerticalAlignment(fieldPanel, HasVerticalAlignment.ALIGN_MIDDLE);
			layout.setCellHorizontalAlignment(fieldPanel, HasHorizontalAlignment.ALIGN_LEFT);
		}
		else{
			listChoices = new ListBox();
			listChoices.addItem(BaseNLS.constants().enumeration_unknown(), "");
			listChoices.setSelectedIndex(0);
			listChoices.setStylePrimaryName("imogene-FormText");
			addValueWidget(listChoices);
			layout.setCellVerticalAlignment(listChoices, HasVerticalAlignment.ALIGN_MIDDLE);
			layout.setCellHorizontalAlignment(listChoices, HasHorizontalAlignment.ALIGN_LEFT);
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
			fieldPanel.addField(box);
		}else{
			listChoices.addItem(label, value);
		}
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
	public boolean toBeSearched() {
		return (getOperatorValue() != null && getOperatorValue().equals(CriteriaConstants.OPERATOR_ISNULL_OR_EMPTY))	|| 
			   (getOperatorValue() != null && getValue() != null && !getValue().equals(""));
	}
	
	@Override
	public void cancel() {
		if(!listWidget){
			fieldPanel.cancelSelections();
		}
		else{
			listChoices.setSelectedIndex(-1);
		}
		thisOperator.setSelectedIndex(0);
	}

	@Override
	protected void fillOperatorValues() {
		thisOperator.addItem(BaseNLS.constants().search_not_searched(), "");
		if(thisMultiple) {
			thisOperator.addItem(BaseNLS.constants().search_operator_contains_all(), CriteriaConstants.ENUM_MULT_OPERATOR_CONTAINS_ALL);
			thisOperator.addItem(BaseNLS.constants().search_operator_contains_one(), CriteriaConstants.ENUM_MULT_OPERATOR_CONTAINS_ONE_OF);
		}
		else
			thisOperator.addItem(BaseNLS.constants().search_operator_string_equal(), CriteriaConstants.STRING_OPERATOR_EQUAL);
		thisOperator.addItem(BaseNLS.constants().search_operator_isnull(), CriteriaConstants.OPERATOR_ISNULL_OR_EMPTY);
		thisOperator.setSelectedIndex(0);
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
			this.add(choice);
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
		 */
		public void clear(){
			for(CheckBox choice : choices){
				remove(choice);
			}
		}
		
		public void cancelSelections() {
			for(CheckBox choice : choices){
				if(choice.getValue()){
					choice.setValue(false);
				}
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
		 */
		public void clear(){
			for(CheckBox choice : choices){
				remove(choice);
			}
		}
		
	}




}
