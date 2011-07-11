package org.imogene.web.gwt.client.ui.field;

import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.client.ui.field.paginatedList.AbstractImogListBoxDataProvider;
import org.imogene.web.gwt.client.ui.field.paginatedList.ImogPaginatedListBox;
import org.imogene.web.gwt.client.ui.field.paginatedList.ListValueChangeHandler;
import org.imogene.web.gwt.common.criteria.ImogJunction;
import org.imogene.web.gwt.common.entity.ImogBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;



/**
 * Composite to manage the display of relation fields with cardinality 1
 * @author Medes-IMPS
 */
public class ImogPaginatedRelationBox<T extends ImogBean> extends ImogFieldAbstract<T> implements ListValueChangeHandler {

	/* status */
	private String thisLabel;	
	private boolean edited = false;
	private boolean canCreateEntity = true;
	private boolean isPaginated = true;
	
	/* widgets */
	private HorizontalPanel layout;	
	private Image errorImage;	
	private Image addImage;	
	private Image viewImage;	
	
	private ImogPaginatedListBox<T> paginatedList;
	
	
	
	public ImogPaginatedRelationBox(){
		layout();
		properties();		
	}
	
	public ImogPaginatedRelationBox(String label){
		this();
		thisLabel = label;
	}
	
	public ImogPaginatedRelationBox(String label, boolean canCreateEntity) {
		this();
		thisLabel = label;
		this.canCreateEntity = canCreateEntity;
	}
	
	public ImogPaginatedRelationBox(String label, boolean canCreateEntity, boolean isPaginated) {
		this();
		thisLabel = label;
		this.canCreateEntity = canCreateEntity;
		this.isPaginated = isPaginated;
	}
	
	private void layout(){
		layout = new HorizontalPanel();
		
		errorImage = new Image(GWT.getModuleBaseURL());
		layout.add(errorImage);
		
		if(isPaginated)
			paginatedList = new ImogPaginatedListBox<T>();
		else
			paginatedList = new ImogPaginatedListBox<T>(false);		
		paginatedList.addListValueChangeHandler(this);		
		layout.add(paginatedList);	
		
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
		layout.setCellVerticalAlignment(paginatedList, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(paginatedList, HasHorizontalAlignment.ALIGN_LEFT);		
		
		viewImage.setStyleName("imogene-FormImageLink");
		viewImage.setVisible(false);
		layout.setCellVerticalAlignment(viewImage, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(viewImage, HasHorizontalAlignment.ALIGN_LEFT);
		
		addImage.setStyleName("imogene-FormImageLink");
		layout.setCellVerticalAlignment(addImage, HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setCellHorizontalAlignment(addImage, HasHorizontalAlignment.ALIGN_LEFT);
		
		errorImage.setVisible(false);		
	}
	
	/**
	 * 
	 * @return
	 */
	public ImogPaginatedListBox<T> getEmbedded(){
		return paginatedList;
	}
	
	/**
	 * 
	 * @param inError
	 */
	public void setInError(boolean inError){
		if(inError)
			paginatedList.addStyleDependentName("error");
		else
			paginatedList.removeStyleDependentName("error");
	}
	
	/**
	 * 
	 */
	public void setEnabled(boolean enabled){

		paginatedList.setEnabled(enabled && !isLocked());
		if(!enabled || isLocked()){
			paginatedList.addStyleDependentName("disabled");
			addImage.setVisible(false);
		}else{
			paginatedList.removeStyleDependentName("disabled");
			if (canCreateEntity)
				addImage.setVisible(true);
			else
				addImage.setVisible(false);
		}
		edited = enabled;
	}
	
	/**
	 * Adds an item explicitly to the choice list of the listbox
	 * @param display
	 * @param id
	 * @param value
	 */
	public void addItem(String display, String id, T value){
		paginatedList.addItem(display, id, value);
	}
	
	public void setAddClickHandler(ClickHandler handler){
		addImage.addClickHandler(handler);
	}
	
	public void setViewClickHandler(ClickHandler handler){
		viewImage.addClickHandler(handler);
	}
	
	@Override
	public T getValue() {	
		return paginatedList.getValue();
	}
	
	public String getDisplayValue() {
		return (String)paginatedList.getText();
	}

	@Override
	public boolean validate() {
		if(isMandatory() && paginatedList.getText().equals("")){
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
		paginatedList.setValue(value);
		if(value!=null){
			viewImage.setVisible(true);
		}else{
			viewImage.setVisible(false);
		}
	}
	
	@Override
	public void setValue(T value, boolean notifyChange) {
		paginatedList.setValue(value, notifyChange);
		if(value!=null){
			viewImage.setVisible(true);
		}else{
			viewImage.setVisible(false);
		}
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
	/**
	 * 
	 */
	public void clear() {
		paginatedList.clear();
	}
	
	/**
	 * Method launched when the listbox selected value changes
	 */
	public void onListValueChange() {
		
		if(!paginatedList.getText().equals(""))
			viewImage.setVisible(true);
		else
			viewImage.setVisible(false);
			
		notifyValueChange();
	}
	

	/**
	 * 
	 * @param dataProvider Data provider to feed the paginated list with bean instances
	 * @param mainFieldsUtil Utility class to get the bean instances display values
	 */
	public void setDataProvider(AbstractImogListBoxDataProvider dataProvider, MainFieldsUtil mainFieldsUtil) {
		paginatedList.setDataProvider(dataProvider, mainFieldsUtil);
	}
		
	
	/**
	 * Sets filtering criterions for which values have to be filtered
	 * @param criterions ImogJunction including the  criterions 
	 * for which the values have to be filtered
	 */
	public void setFilterParameters(ImogJunction criterions) {
			paginatedList.setFilterParameters(criterions);
	}
}
