package org.imogene.web.gwt.client.ui.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Fields that presents an address in a text area and proposes a button, 
 * to throw an action on click.
 * The default action open the point corresponding to the address on the 
 * GoogleMaps web site.
 * @author Medes-IMPS
 */
public class ImogAddressField extends ImogFieldAbstract<String> implements ClickHandler {	
	
	private HorizontalPanel main;
	
	private ImogTextArea textArea;
	
	private Image viewOnMap;
	
	private String thisLabel;
	
	private boolean clickable=true;
	
	private boolean edited=false;
	
	private AddressFieldBehavior thisBehavior;
	
	/**
	 * Create a field to display and edit a postal address
	 */
	public ImogAddressField(){
		this(null);		
	}
	
	/**
	 * Create a field to display and edit a postal address
	 * @param behavior the behavior when the field is clicked
	 */
	public ImogAddressField(AddressFieldBehavior behavior){
		layout();
		if(behavior==null){
			thisBehavior = new DefaultAddressBehavior();
		}else{
			thisBehavior = behavior;
		}	
	}
	
	/**
	 * Layout the composite
	 */
	private void layout(){
		main = new HorizontalPanel();
		textArea = new ImogTextArea();
		viewOnMap = new Image(GWT.getModuleBaseURL()+"/images/view_on_map-24.png");
		main.add(textArea);
		main.add(viewOnMap);
		initWidget(main);
		properties();
	}
	
	/**
	 * Set the widget properties
	 */
	private void properties(){
		viewOnMap.addClickHandler(this);
		viewOnMap.setStylePrimaryName("imogene-ImageLink");
		textArea.setWidth("100%");		
		main.setCellWidth(viewOnMap, "24px");
		main.setCellVerticalAlignment(viewOnMap, HorizontalPanel.ALIGN_MIDDLE);		
	}
	
	@Override
	public boolean validate() {
		return textArea.validate();
	}

	@Override
	public void onClick(ClickEvent event) {		
		if(event.getSource().equals(viewOnMap) && clickable ){
			if(textArea.getEmbedded().getText().length()>0)
				thisBehavior.addressAction(textArea.getValue());
			else
				Window.alert("Please, type an address.");
		}
	}

	@Override
	public void setLabel(String pLabel) {
		thisLabel = pLabel;
	}

	@Override
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}

	@Override
	public String getValue() {		
		return textArea.getValue();
	}

	@Override
	public void setValue(String value) {
		textArea.setValue(value);
	}

	@Override
	public void setEnabled(boolean editable) {		
		textArea.setEnabled(editable);
		clickable=!editable;
		edited = editable;
	}
	
	@Override
	public boolean isEdited() {		
		return edited;
	}	

	/* INTERNAL CLASSES/INTERFACES */
	/**
	 * Interface which describes the behavior of teh widget when 
	 * the user choose to open thes address.
	 */
	public interface AddressFieldBehavior {
		public void addressAction(String address);
	}
	
	/**
	 * Behavior that permits to display the address location
	 * on the Google maps web site.
	 * @author Medes-IMPS
	 */
	
	public static class DefaultAddressBehavior implements AddressFieldBehavior {
		
		private final static String GMAPS_TMPL = "http://maps.google.com?q=%ADDRESS%"; 
		
		@Override
		public void addressAction(String address) {
			String encodedAddress = address.replace("\n", " ");
			Window.open(GMAPS_TMPL.replace("%ADDRESS%", encodedAddress), "Google maps", "");
		}
	}
		
	
}
