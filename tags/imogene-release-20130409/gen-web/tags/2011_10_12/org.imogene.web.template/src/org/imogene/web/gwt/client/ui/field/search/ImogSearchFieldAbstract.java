package org.imogene.web.gwt.client.ui.field.search;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract class that proposes some common field behaviors
 * for search fields
 * @author Medes-IMPS 
 * @param <T> the field type 
 */
public abstract class ImogSearchFieldAbstract<T> extends Composite implements ImogSearchField<T> {	
	

	protected Set<SearchFieldValueChangeHandler> valueChangeHandler = new HashSet<SearchFieldValueChangeHandler>();
		
	protected HorizontalPanel layout;	
	protected String thisLabel;
	protected ListBox thisOperator;
	
	
	/**
	 * Field common parts
	 */
	public ImogSearchFieldAbstract(){
		layoutField();
		propertiesForField();
	}
	
	/**
	 * Layout the field composite
	 */
	private void layoutField(){
		layout = new HorizontalPanel();
		thisOperator = new ListBox();
		layout.add(thisOperator);
		super.initWidget(layout);
	}
	
	/**
	 * Set the widget properties
	 */
	private void propertiesForField(){
		thisOperator.setStylePrimaryName("imogene-search-operator");
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellVerticalAlignment(thisOperator, HasVerticalAlignment.ALIGN_MIDDLE);
	}
	
	/**
	 * 
	 */
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}
	
	/**
	 * 
	 */
	public void setLabel(String label) {
		thisLabel = label;
	}

	/**
	 * 
	 */
	public String getOperatorValue() {
		if (thisOperator.getSelectedIndex()>0)
			return thisOperator.getValue(thisOperator.getSelectedIndex());
		else
			return null;
	}
	
	/**
	 * An handler notified when the field value change
	 * @param handler the handler to add
	 */
	public void addFieldValueChangeHandler(SearchFieldValueChangeHandler handler){
		valueChangeHandler.add(handler);
	}
	
	/**
	 * Remove a handler notified when the field value change
	 * @param handler the handler to remove
	 */
	public void removeFieldValueChangeHandler(SearchFieldValueChangeHandler handler){
		valueChangeHandler.remove(handler);
	}
		
	/**
	 * Notify handler the the field value changed 
	 */
	public void notifyValueChange() {		
		for(SearchFieldValueChangeHandler handler : valueChangeHandler){
			if(handler!=null)
				handler.onFieldValueChange(this);
		}
	}
	
			
	protected void addValueWidget(Widget widget) {
		layout.add(widget);
	}
	
	protected abstract void fillOperatorValues();

}
