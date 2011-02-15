package org.imogene.web.gwt.client.ui.field;

import java.util.List;
import java.util.Vector;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.common.entity.ImogBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;



/**
 * Composite to manage the display of relation fields with cardinality 1
 * @author Medes-IMPS
 */
public class ImogRelationBox<T extends ImogBean > extends ImogFieldAbstract<T> implements ChangeHandler {

	/* status */
	private List<T> values = new Vector<T>();
	private String thisLabel;	
	private boolean edited = false;
	private boolean canCreateEntity = true;
	
	/* widgets */
	private HorizontalPanel layout;	
	private Image errorImage;	
	private Image addImage;	
	private Image viewImage;	
	private ListBox listBox;
	
	
	
	public ImogRelationBox(){
		layout();
		properties();		
	}
	
	public ImogRelationBox(String label){
		this();
		thisLabel = label;
	}
	
	public ImogRelationBox(String label, boolean canCreateEntity) {
		this();
		thisLabel = label;
		this.canCreateEntity = canCreateEntity;
	}
	
	private void layout(){
		layout = new HorizontalPanel();
		
		errorImage = new Image(GWT.getModuleBaseURL());
		layout.add(errorImage);
		
		listBox = new ListBox();
		listBox.addChangeHandler(this);
		// Empty item automatically added to the listbox
		listBox.addItem(BaseNLS.constants().enumeration_unknown(), "");
		listBox.setSelectedIndex(0);
		layout.add(listBox);
		
		viewImage = new Image(GWT.getModuleBaseURL() + "/images/relation_view.png");
		viewImage.setTitle(BaseNLS.constants().button_view());
		layout.add(viewImage);
		
		addImage = new Image(GWT.getModuleBaseURL() + "/images/relation_add.png");
		addImage.setTitle(BaseNLS.constants().button_add());
		layout.add(addImage);

		initWidget(layout);
	}
	
	private void properties(){
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
		listBox.setStylePrimaryName("imogene-FormText");
		layout.setCellVerticalAlignment(listBox, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(listBox, HasHorizontalAlignment.ALIGN_LEFT);
		
		viewImage.setStyleName("imogene-FormImageLink");
		viewImage.setVisible(false);
		layout.setCellVerticalAlignment(viewImage, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(viewImage, HasHorizontalAlignment.ALIGN_LEFT);
		
		addImage.setStyleName("imogene-FormImageLink");
		layout.setCellVerticalAlignment(addImage, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(addImage, HasHorizontalAlignment.ALIGN_LEFT);
		
		errorImage.setVisible(false);		
	}
	
	public ListBox getEmbedded(){
		return listBox;
	}
	
	public void setInError(boolean inError){
		if(inError)
			listBox.addStyleDependentName("error");
		else
			listBox.removeStyleDependentName("error");
	}
	
	public void setEnabled(boolean enabled){
		listBox.setEnabled(enabled && !isLocked());
		if(!enabled || isLocked()){
			listBox.addStyleDependentName("disabled");
			addImage.setVisible(false);
		}else{
			listBox.removeStyleDependentName("disabled");
			if (canCreateEntity)
				addImage.setVisible(true);
			else
				addImage.setVisible(false);
		}
		edited = enabled;
	}
	
	public void addItem(String display, String id, T value){
		values.add(value);
		listBox.addItem(display, id);
	}
	
	public int getIndexForValue(String value){
		for(int i = 0; i<listBox.getItemCount(); i++){
			if(listBox.getValue(i).equals(value))
				return i;
		}
		return -1;
	}
	
	public void setAddClickHandler(ClickHandler handler){
		addImage.addClickHandler(handler);
	}
	
	public void setViewClickHandler(ClickHandler handler){
		viewImage.addClickHandler(handler);
	}
	
	public T getValue(int i){
		if(i<0)
			return null;
		return values.get(i);
	}
	
	@Override
	public T getValue() {		
		// (-1) because there is an empty value in the listbox
		return (T)getValue(listBox.getSelectedIndex()-1);
	}
	
	public String getDisplayValue() {
		return listBox.getItemText(listBox.getSelectedIndex());
	}

	@Override
	public boolean validate() {
		if(isMandatory() && listBox.getSelectedIndex()==0){
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
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(T value) {	
		if(value!=null){
			int index = getIndexForValue(value.getId());
			if(index>-1){
				listBox.setSelectedIndex(index);
				viewImage.setVisible(true);
				
			}else{
				listBox.setSelectedIndex(0);
				viewImage.setVisible(false);
			}
		}else{
			listBox.setSelectedIndex(0);
			viewImage.setVisible(false);
		}
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
	/**
	 * Selects the item matching the selected imogene bean id 
	 * @param id the imogene bean id
	 */
	public void select(String id){
		for(int i=0; i<listBox.getItemCount(); i++){
			String valueId= listBox.getValue(i);
			if(valueId.equals(id)){
				listBox.setSelectedIndex(i);
				viewImage.setVisible(true);
				return;
			}
		}
	}
	

	public void clear() {
		for(int i=listBox.getItemCount()-1; i>=1; i--){
			listBox.removeItem(i);
		}
		values.clear();
	}
	
	public void onChange(ChangeEvent arg0) {
		if (listBox.getSelectedIndex()>0)
			viewImage.setVisible(true);
		else
			viewImage.setVisible(false);
			
		notifyValueChange();		
	}
	
	public boolean isEmpty() {
		return values.isEmpty();
	}
				
}
