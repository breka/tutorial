package org.imogene.web.gwt.client.ui.field;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.imogene.web.gwt.client.LocalSession;
import org.imogene.web.gwt.client.i18n.BaseNLS;
import org.imogene.web.gwt.common.entity.LocalizedText;
import org.imogene.web.gwt.common.id.ImogKeyGenerator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;


/**
 * Composite to manage the display of translatable text fields
 * @author Medes-IMPS
 */
public class ImogLocalizedTextBox extends ImogFieldAbstract<String> implements ChangeHandler{
	
	/* status - behavior */
	private String thisLabel;	
	private String fieldId;	
	private Map<String,LocalizedText> values = new HashMap<String,LocalizedText>();
	private boolean edited = false;
	private String currentLocale;
	private boolean isNew = true;
	
	/* widgets */
	private HorizontalPanel layout;
	private Image errorImage;
	private TextBox textBox;
	private ListBox localeListBox;
	
	
	public ImogLocalizedTextBox(List<String> isoCodes, String locale){		
		currentLocale = locale;	
		layout();
		properties();
		createLocaleList(isoCodes);
		createNewLocalizedText(currentLocale);
	}
	
	private void layout(){		
		layout = new HorizontalPanel();
		errorImage = new Image(GWT.getModuleBaseURL());
		textBox = new TextBox();
		localeListBox = new ListBox();
		
		layout.add(errorImage);
		layout.add(textBox);
		layout.add(localeListBox);
		
		initWidget(layout);
	}
	
	private void properties(){
		layout.setCellWidth(textBox, "100%");
		errorImage.setVisible(false);
		textBox.setStylePrimaryName("imogene-FormText");
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {			
			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				notifyValueChange();
			}
		});
		localeListBox.setStylePrimaryName("imogene-FormTextLocale");
		layout.setCellVerticalAlignment(localeListBox, HasVerticalAlignment.ALIGN_MIDDLE);
		//layout.setCellHorizontalAlignment(localeListBox, HasHorizontalAlignment.ALIGN_LEFT);
	}
	
	/**
	 * Creates the listbox that lists the availables locales for
	 * the Localized TextBox
	 * @param isoCodes list of iso codes
	 */
	private void createLocaleList(List<String> isoCodes) {
		
		// fill in locale selection listbox
		if (isoCodes!=null){
			int i = 0;
			for(String isoCode:isoCodes) {
				localeListBox.addItem(isoCode);
				// select current locale
				if (isoCode.equals(currentLocale))
					localeListBox.setSelectedIndex(i);
				i++;
			}
		}
		localeListBox.addChangeHandler(this);
	}
	
	/**
	 * Sets the LocalizedTextBox value (the field id)
	 * and retrieves all the localized texts for the given field
	 * @param value the field id
	 */
	@Override
	public void setValue(String value) {
		this.fieldId = value;
	}
	
	@Override
	public void setValue(String value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}
	
	/**
	 * Sets the LocalizedTextBox values (localized texts for the given field)
	 * @param texts the LocalizedTextBox values
	 */
	public void setValue(List<LocalizedText> texts) {

		if (texts!=null && texts.size()>0) {
			isNew= false;
			
			for (LocalizedText current : texts){
				
				if (fieldId ==null)
					fieldId = current.getFieldId();

				// add LocalizedText to map
				values.put(current.getLocale(), current);
				// display text for currentLocale
				if(current.getLocale().equals(currentLocale))  {
					textBox.setText(current.getValue());
				}			
			}
			
			// if no text is available for the current locale, get the first one that is available
			if (textBox.getText()==null || textBox.getText().equals("")) {
				for (LocalizedText text : values.values()){	
					if (text.getValue()!=null && !text.getValue().equals("") ) {
						int localeIndex = getIndexOfLocale(text.getLocale());
						if (localeIndex>=0) {
							textBox.setText(text.getValue());
							localeListBox.setSelectedIndex(localeIndex);
						}
					}
				}
			}		
		}	
	}
	

	/**
	 * Gets the LocalizedTextBox value (the field id)
	 */
	@Override
	public String getValue() {
		if(hasText())
			setFieldId();
		return fieldId;
	}
	
	/**
	 * Gets the LocalizedTextBox display value (string displayed in the field)
	 * @return
	 */
	public String getDisplayValue() {
		return textBox.getText();
	}
	
	/**
	 * Sets a unique id for the field when created
	 */
	private void setFieldId() {				
		if (fieldId == null || fieldId.equals("")) {
			fieldId = ImogKeyGenerator.generateKeyId("FLD");
		}
	}
	
	/**
	 * Indicates if the LocalizedTextBox has text entered 
	 * @return true if the LocalizedTextBox has text entered 
	 */
	private boolean hasText() {
		// check textbox for entered text
		if (textBox.getText()!=null && !textBox.getText().equals("")) {
			return true;
		}
		else {
			// check values for previously entered texts
			for (LocalizedText text : values.values()){	
				if (!text.getLocale().equals(currentLocale) && text.getValue()!=null && !text.getValue().equals("") )
					return true;	
			}
			return false;
		}
	}
	
	/**
	 * Gets the localized texts to be saved defined 
	 * for the LocalizedTextBox 
	 */
	public List<LocalizedText> getLocalizedTextsToSave() {
		
		List<LocalizedText> localizedTextsToSave = new Vector<LocalizedText>();
		storeTextInMap();	
		
		if ((isNew && hasText()) || !isNew) {
			setFieldId();	
			for (LocalizedText current:values.values())							
				localizedTextsToSave.add(updateLocalizedText(current));			
		}
		return localizedTextsToSave;
	}
	
	
	/**
	 * Updates a LocalizedText before it is saved
	 * @param text the LocalizedText
	 * @return the updated LocalizedText
	 */
	private LocalizedText updateLocalizedText(LocalizedText text) {
		
		LocalizedText value = text;
		
		if (text.getId()==null) {			
			text.setId(ImogKeyGenerator.generateKeyId("LTXT"));
			text.setFieldId(fieldId);
			text.setCreationDate(new Date(System.currentTimeMillis()));
			text.setCreator((LocalSession.get().getCurrentUser().getLogin()));			
		}
		
		text.setLastModificationDate(new Date(System.currentTimeMillis()));
		text.setModifier((LocalSession.get().getCurrentUser().getLogin()));
		text.setModifiedFrom("web");
		
		return value;
	}
	
	/**
	 * Stores the text that has been entered in the TextBox
	 * in the LocalizedText map
	 */
	private void storeTextInMap() {
		LocalizedText text = values.get(currentLocale);
		text.setValue(textBox.getText());
	}
	
	
	/**
	 * Called when a new locale is selected
	 * in the locale ListBox
	 */
	@Override
	public void onChange(ChangeEvent arg0) {
		
		// in edition mode, save previously entered text
		if(edited) {
			storeTextInMap();	
		}
		
		// change text depending on locale
		currentLocale = localeListBox.getValue(localeListBox.getSelectedIndex());
		if (values.containsKey(currentLocale)) {
			textBox.setText(values.get(currentLocale).getValue());
		}
		else {
			createNewLocalizedText(currentLocale);
		}
	}
	
	/**
	 * Gets the index of the locale (iso code) in the localeListBox
	 * @param locale the searched locale 
	 * @return the locale index in the localeListBox
	 */
	private int getIndexOfLocale(String locale) {
		
		int result = -1;
		for (int i=0;i<localeListBox.getItemCount();i++) {
			if (localeListBox.getValue(i).equals(locale))
				return i;
		}
		return result;
	}
	
	/**
	 * Creates a new LocalizedText for the field 
	 * @param locale
	 */
	private void createNewLocalizedText (String locale) {
		LocalizedText newText = new LocalizedText();
		newText.setLocale(locale);
		values.put(locale, newText);
		textBox.setText("");
	}
	
	
	
	
	public TextBox getEmbedded(){
		return textBox;
	}
	
	public void setInError(boolean inError){
		if(inError)
			textBox.addStyleDependentName("error");
		else
			textBox.removeStyleDependentName("error");
	}
	
	public void setEnabled(boolean enabled){
		textBox.setEnabled(enabled);
		if(!enabled){
			textBox.addStyleDependentName("disabled");
		}else{
			textBox.removeStyleDependentName("disabled");
		}
		edited = enabled;
	}

	@Override
	public boolean validate() {
		if(isMandatory() && !hasText()){
			displayError(BaseNLS.constants().field_mandatory());
			return false;
		}
		/* validation rules */
		// check textbox for entered text
		if(!textBox.getText().equals("")){
			if (!validateRules(textBox.getText()))
				return false;
		}
		// check values of previously entered texts
		for (LocalizedText text : values.values()){	
			if (text.getValue()!=null && !text.getValue().equals("") ) {
				if (!validateRules(text.getValue()))
					return false;
			}
		}
		
		hideError();
		return true;
	}
	
	/**
	 * Validates a value upon the defined validation rules
	 * @param toBevalidated the value to be validated
	 * @return
	 */
	private boolean validateRules(String toBevalidated) {
		for(ValidationRule rule : rules){
			if(!toBevalidated.matches(rule.getRegex())){
				displayError(rule.getText());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks that the LocalizedText values match a given regex
	 * @param regex the regex to be validated
	 * @return true if the LocalizedText values match the regex
	 */
	public boolean matches(String regex) {

		// check textbox for entered text
		if(!textBox.getText().matches(regex)){
			return false;
		}
		// check values of previously entered texts
		for (LocalizedText text : values.values()){	
			if (text.getValue()!=null && !text.getValue().matches(regex))
				return false;
		}
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
	public boolean isEdited() {		
		return edited;
	}	
	



	
	
	
	
	
}
