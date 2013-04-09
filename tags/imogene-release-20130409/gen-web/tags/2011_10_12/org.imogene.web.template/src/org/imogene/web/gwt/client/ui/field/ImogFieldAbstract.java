package org.imogene.web.gwt.client.ui.field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract class that proposes some common field behaviors.
 * @author Medes-IMPS 
 * @param <T> the field type 
 */
public abstract class ImogFieldAbstract<T> extends Composite implements ImogField<T> {	
	
	/* status */
	private boolean visibleDependent = false;
	private boolean mandatory = false;
	private boolean locked = false;

	protected Set<FieldValueChangeHandler> valueChangeHandler = new HashSet<FieldValueChangeHandler>();
	protected Set<VisibilityChangeHandler> visibilityListeners = new HashSet<VisibilityChangeHandler>();
	
	protected List<ValidationRule> rules = new ArrayList<ValidationRule>();
	
	/* widgets */
	private VerticalPanel main;
	private Label errorLabel;
	
	/**
	 * Field common parts
	 */
	public ImogFieldAbstract(){
		layout();
		properties();
	}
	
	/**
	 * Layout the field composite
	 */
	private void layout(){
		errorLabel = new Label();
		main = new VerticalPanel();
		main.add(errorLabel);
		super.initWidget(main);
	}
	
	/**
	 * Set the widget properties
	 */
	private void properties(){
		errorLabel.setStylePrimaryName("imogene-errorLabel");
		errorLabel.setVisible(false);
	}
	
	/**
	 * Add a validation rule to the field
	 * @param rule the rule to add
	 */
	public void addRule(ValidationRule rule){
		rules.add(rule);
	}
	
	/**
	 * Add a validation rule to this field
	 * @param text the validation error text
	 * @param regex the validation rule regex
	 */
	public void addRule(String text, String regex){
		rules.add(new ValidationRule(text, regex));
	}
	
	/**
	 * A an handler notified when the field value change
	 * @param handler the handler to add
	 */
	public void addFieldValueChangeHandler(FieldValueChangeHandler handler){
		valueChangeHandler.add(handler);
	}
	
	/**
	 * Remove a handler notified when the field value change
	 * @param handler teh handler to remove
	 */
	public void removeFieldValueChangeHandler(FieldValueChangeHandler handler){
		valueChangeHandler.remove(handler);
	}
	
	/**
	 * A an handler notified when the field visibility change
	 * @param handler the handler to add
	 */
	public void addVisibilityChangeHandler(VisibilityChangeHandler handler){
		visibilityListeners.add(handler);
	}
	
	/**
	 * Remove a handler notified when the field visibility change
	 * @param handler the handler to remove
	 */
	public void removeVisibilityChangeHandler(VisibilityChangeHandler handler){
		visibilityListeners.remove(handler);
	}
		
	/**
	 * Notify handler the the field value changed 
	 */
	public void notifyValueChange() {		
		for(FieldValueChangeHandler handler : valueChangeHandler){
			if(handler!=null)
				handler.onFieldValueChange(this);
		}
	}
	
	@Override
	public void setVisible(boolean visible) {		
		super.setVisible(visible);
		for(VisibilityChangeHandler handler : visibilityListeners){
			if(handler!=null)
				handler.onVisibilityChange(visible, this);
		}
	}	
			
	@Override
	protected void initWidget(Widget widget) {
		main.add(widget);
	}

	/**
	 * Set if this field is visible dependent
	 * @param dependent true if the field visibility is dependent
	 */
	public void setVisibleDependent(boolean dependent){
		visibleDependent = dependent;		
	}
	
	/**
	 * Is this field visible dependent
	 * @return true if it is visible dependent
	 */
	public boolean isVisisbleDependent(){
		return visibleDependent;
	}
	
	/**
	 * Display the error message
	 * @param error the error message
	 */
	public void displayError(String error){
		errorLabel.setText(error);
		errorLabel.setVisible(true);
	}
	
	/**
	 * Hide the error message
	 */
	public void hideError(){
		errorLabel.setVisible(false);
	}
	
	/**
	 * Set if this field is mandatory
	 * @param pMandatory true if the field is mandatory
	 */
	public void setMandatory(boolean pMandatory){
		mandatory = pMandatory;
	}
	
	/**
	 * Is this field mandatory
	 * @return true if the field is mandatory
	 */
	public boolean isMandatory(){
		return mandatory;
	}

	/**
	 * A locked field can not be edited and is always valid.
	 * @return true if this field is locked.
	 */
	public boolean isLocked() {
		return locked;
	}
	
	/**
	 * A locked field can not be edited and is always valid.
	 * @param locked set if this field is locked.
	 */
	public void setLocked(boolean locked) {		
		this.locked = locked;
		setEnabled(false);
	}
	
	
	
}
