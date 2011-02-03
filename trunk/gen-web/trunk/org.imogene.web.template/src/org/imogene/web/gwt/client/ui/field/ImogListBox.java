package org.imogene.web.gwt.client.ui.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;

public class ImogListBox extends ImogFieldAbstract<String> {
	
	/* status - behavior */
	
	private String thisLabel;
	
	private boolean edited = false;
	
	/* widgets */
	
	private HorizontalPanel layout;
	
	private Image errorImage;
	
	private ListBox listBox;	
	
	public ImogListBox(){
		layout();
		properties();
	}
	
	public ImogListBox(String label){
		
	}
	
	private void layout(){
		layout = new HorizontalPanel();
		errorImage = new Image(GWT.getModuleBaseURL());
		listBox = new ListBox();
		layout.add(errorImage);
		layout.add(listBox);
		initWidget(layout);
	}
	
	private void properties(){
		layout.setCellWidth(errorImage, "16px");
		layout.setCellWidth(listBox, "100%");
		errorImage.setVisible(false);
		listBox.setStylePrimaryName("imogene-FormText");
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
	
	@Override
	public void setEnabled(boolean enabled){
		listBox.setEnabled(enabled);
		if(!enabled){
			listBox.addStyleDependentName("disabled");
		}else{
			listBox.removeStyleDependentName("disabled");
		}
		edited = enabled;
	}
	
	public void addItem(String display, String value){
		listBox.addItem(display, value);
	}
	
	public int getIndexForValue(String value){
		for(int i = 0; i<listBox.getItemCount(); i++){
			if(listBox.getValue(i).equals(value))
				return i;
		}
		return -1;
	}

	@Override
	public boolean validate() {		
		if(isMandatory() && listBox.getSelectedIndex()==-1){
			displayError("This field is mandatory.");
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
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@Override
	public void setValue(String value) {
		int index = getIndexForValue((String)value);
		if(index > -1)
			listBox.setSelectedIndex(index);
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
		
}
