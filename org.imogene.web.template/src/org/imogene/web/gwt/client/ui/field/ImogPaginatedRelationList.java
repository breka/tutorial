package org.imogene.web.gwt.client.ui.field;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.paginatedList.ImogRelationPaginatedSelectionBox;
import org.imogene.web.gwt.common.entity.ImogBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This class enables to display and edit a relation with cardinality N
 * @author Medes-IMPS
 * @param <T> the ImogBean class that this field is handling 
 */
public class ImogPaginatedRelationList<T extends ImogBean> extends ImogFieldAbstract<Set<T>> implements ClickHandler {

	
	/* status */
	private Map<String,T> values = new HashMap<String, T>();
	private boolean isEdited = false;
	private boolean canCreateEntity = true;
	private boolean isEmpty = true;
	private String label;
	private MainFieldsUtil fieldsUtil;
	
	/* widgets */
	private HorizontalPanel panel;
	private ListBox list;
	private ImogRelationPaginatedSelectionBox<T> selectionBox;
	
	/* buttons */
	private VerticalPanel buttons;
	private Image addButton;
	private Image newButton;
	private Image removeButton;
	private Image viewButton;

	/* handlers */
	private ListButtonClickHandler newHandler;
	private ListButtonClickHandler viewHandler;
	

	
	/**
	 * Creates the Relation list
	 * @param pSelectionBox the associated selection box (used to select and attach entities to parent)
	 * @param pFieldsUtil the main fields creator.
	 */
	public ImogPaginatedRelationList(ImogRelationPaginatedSelectionBox<T> pSelectionBox, MainFieldsUtil pFieldsUtil){	
		selectionBox = pSelectionBox;
		selectionBox.setParentField(this);
		fieldsUtil = pFieldsUtil;
		layout();
		properties();
		setBehavior();
	}
	
	/**
	 * Creates the Relation list
	 * @param pLabel The field label
	 * @param pSelectionBox the associated selection box (used to select and attach entities to parent)
	 * @param pFieldsUtil the main fields creator.
	 */
	public ImogPaginatedRelationList(String pLabel, ImogRelationPaginatedSelectionBox<T> pSelectionBox, MainFieldsUtil pFieldsUtil){
		this(pSelectionBox, pFieldsUtil);
		label = pLabel;	
	}
	
	/**
	 * Creates the Relation list
	 * @param pLabel The field label
	 * @param pSelectionBox the associated selection box (used to select and attach entities to parent)
	 * @param pFieldsUtil the main fields creator.
	 * @param canCreateEntity true if button 'add' to be visible
	 */
	public ImogPaginatedRelationList(String pLabel, ImogRelationPaginatedSelectionBox<T> pSelectionBox, MainFieldsUtil pFieldsUtil, boolean canCreateEntity){
		this(pSelectionBox, pFieldsUtil);
		label = pLabel;	
		this.canCreateEntity = canCreateEntity;
	}
	
	/**
	 * Layout the field
	 */
	private void layout(){
		panel = new HorizontalPanel();
		list = new ListBox(true);
		panel.add(list);
		
		/* buttons */
		buttons = new VerticalPanel();
		HorizontalPanel cButtons = new HorizontalPanel();
		addButton = new Image(GWT.getModuleBaseURL() + "images/relation_affect.png");
		addButton.setTitle(BaseNLS.constants().button_assign());
		removeButton = new Image(GWT.getModuleBaseURL() + "images/relation_remove.png");
		removeButton.setTitle(BaseNLS.constants().button_remove());
		viewButton = new Image(GWT.getModuleBaseURL() + "images/relation_view.png");
		viewButton.setTitle(BaseNLS.constants().button_view());
		newButton = new Image(GWT.getModuleBaseURL() + "images/relation_add.png");
		newButton.setTitle(BaseNLS.constants().button_add());
		cButtons.add(addButton);
		cButtons.add(newButton);
		buttons.add(cButtons);
		buttons.add(removeButton);
		buttons.add(viewButton);
		panel.add(buttons);
		
		initWidget(panel);
	}
	
	/**
	 * Sets the field widgets properties
	 */
	private void properties(){
		list.setVisibleItemCount(5);
		list.setStylePrimaryName("imogene-FormText");
		
		addButton.setVisible(false);
		newButton.setVisible(false);
		removeButton.setVisible(false);
		viewButton.setVisible(false);
	}
	
	/**
	 * Sets the field behavior properties
	 */
	private void setBehavior(){
		addButton.addClickHandler(this);
		removeButton.addClickHandler(this);
		viewButton.addClickHandler(this);
		newButton.addClickHandler(this);
	}
	
	@Override
	public String getLabel() {		
		return label;
	}

	@Override
	public Set<T> getValue() {	
		Set<T> result = new HashSet<T>();
		for(T entity : values.values()){
			result.add(entity);
		}				
		return result;
	}

	@Override
	public boolean isEdited() {		
		return isEdited;
	}

	@Override
	public void setEnabled(boolean editable) {		
		addButton.setVisible(editable && !isLocked());
		removeButton.setVisible(!isEmpty && editable && !isLocked());
		newButton.setVisible(canCreateEntity && editable && !isLocked());
		viewButton.setVisible(!isEmpty);
	}

	@Override
	public void setLabel(String pLabel) {
		label = pLabel;		
	}

	@Override
	public void setValue(Set<T> pValue) {
		values = new HashMap<String, T>();
		list.clear();
		for(T b : pValue){
			values.put(b.getId(), b);				
			list.addItem(fieldsUtil.getDisplayValue(b), b.getId());
			if (isEmpty) {
				isEmpty = false;
				viewButton.setVisible(true);
			}
		}	
		notifyValueChange();
	}
	
	@Override
	public void setValue(Set<T>value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public boolean validate() {	
		if(isLocked()) return true;
		return true;
	}		
	
	@Override
	public void onClick(ClickEvent event) {		
		
		if(event.getSource().equals(addButton)){			
			selectionBox.show(addButton.getAbsoluteLeft(), addButton.getAbsoluteTop());
		}
		
		else if(event.getSource().equals(removeButton)){
			for(int i=0; i<list.getItemCount(); i++){
				if(list.isItemSelected(i)){
					String toRemove = list.getValue(i);
					values.remove(toRemove);
					list.removeItem(i);
				}
			}
			notifyValueChange();
			if(values.size()==0)
				isEmpty = true;
		}	
		
		else if(event.getSource().equals(newButton)){
			if(newHandler!=null)
				newHandler.onClick(this, "");
		}
		
		else if(event.getSource().equals(viewButton)){
			if(viewHandler!=null){
				String id = "";
				if(list.getSelectedIndex()>-1){
					id = list.getValue(list.getSelectedIndex());
				}
				viewHandler.onClick(this,id);
			}
				
		}
	}
	
	/**
	 * Removes all elements from the widget
	 */
	public void emptyList() {
		values.clear();
		list.clear();
		isEmpty = true;
		notifyValueChange();
	}
			
	/**
	 * Adds a value to this item
	 * @param label the item display text
	 * @param entity the item to addButton
	 */
	public void addValue(String label, T entity){
		list.addItem(label, entity.getId());
		values.put(entity.getId(), entity);
		if (isEmpty) {
			isEmpty = false;
			viewButton.setVisible(true);
		}
		notifyValueChange();
	}
	
	/**
	 * Removes a value to this item
	 * @param entity the value to be removed
	 */
	public void removeValue(T entity) {
		String id = entity.getId();

		for(int i=0; i<list.getItemCount(); i++){
			String key = list.getValue(i);
			if (key.equals(id))
				list.removeItem(i);
		}
		values.remove(id);
	}
	
	/**
	 * Returns true if the specified entity is attached to this field
	 * @param toTest the entity to test
	 * @return true is the entity is already present.
	 */
	public boolean isPresent(T toTest){
		for (int i = 0; i < list.getItemCount(); i++) {
			if (list.getValue(i).equals(toTest.getId()))
				return true;
		}
		return false;
	}	
	
	/**
	 * Registers a click handler on the 'new' button.
	 * @param handler the click handler
	 * @return the handler registration
	 */
	public void setNewButtonHandler(ListButtonClickHandler handler){
		newHandler = handler;
	}
	
	/**
	 * Registers a click handler on the 'view' button.
	 * @param handler the click handler
	 * @return the handler registration
	 */
	public void setViewButtonHandler(ListButtonClickHandler handler){
		viewHandler = handler;
	}
	
	/** INTERNAL CLASSES AND INTERFACES **/
	
	/**
	 * Interface that describe an handler that could be 
	 * called when this field buttons are clicked.
	 */
	public interface ListButtonClickHandler {
		
		/**
		 * Called when the button is clicked
		 * @param source the parent class
		 * @param entityId the first selected entity id
		 */
		public void onClick(ImogPaginatedRelationList<?> source, String entityId);
	}

}
