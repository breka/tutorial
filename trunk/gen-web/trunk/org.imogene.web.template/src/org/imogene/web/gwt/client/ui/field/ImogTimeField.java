package org.imogene.web.gwt.client.ui.field;

import java.util.Date;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class ImogTimeField extends ImogFieldAbstract<Date> {

	/* status - behavior */
	private String thisLabel;
	private boolean edited = false;
	
	/* widgets */
    private HorizontalPanel main;
	private ListBox hoursBox;
	private Label hoursLabel;
	private ListBox minutesBox;
	private Label minutesLabel;
			
	public ImogTimeField(){
		layout();
		properties();
	}
	
	private void layout(){
		main = new HorizontalPanel();
		main.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(main);
		hoursBox = new ListBox();
		main.add(hoursBox);
		hoursLabel = new Label("h");
		main.add(hoursLabel);
		minutesBox = new ListBox();
		main.add(minutesBox);
		minutesLabel = new Label("min");
		main.add(minutesLabel);
	}
	
	private void properties(){
		setHours();
		setMinutes();
	}
	
	private void setHours(){
		for(int i=0; i<25; i++){
			hoursBox.addItem(String.valueOf(i), String.valueOf(i));
		}
	}
	
	private void setMinutes(){
		for(int i=0; i<60; i++){
			minutesBox.addItem(String.valueOf(i), String.valueOf(i));
		}
	}
	
	@Override
	public boolean validate() {
		
		return true;
	}

	@Override
	public String getLabel() {
		if(thisLabel != null)
			return thisLabel;
		return "";
	}

	@SuppressWarnings("deprecation")
	@Override
	public Date getValue() {
		Date result = null;
		if(hoursBox.getSelectedIndex()!=-1){
			result = new Date();
			result.setHours(Integer.valueOf(hoursBox.getValue(hoursBox.getSelectedIndex())));
			result.setMinutes(Integer.valueOf(minutesBox.getValue(minutesBox.getSelectedIndex())));
		}		
		return result;
	}

	@Override
	public void setLabel(String label) {
		thisLabel = label;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setValue(Date value) {
		if(value != null){
			int hours = value.getHours();
			for(int i=0; i<hoursBox.getItemCount();i++){
				if(hoursBox.getValue(i).equals(String.valueOf(hours))){
					hoursBox.setSelectedIndex(i);
					break;
				}
			}
			int minutes	= value.getMinutes();
			for(int i=0; i<minutesBox.getItemCount();i++){
				if(minutesBox.getValue(i).equals(String.valueOf(minutes))){
					minutesBox.setSelectedIndex(i);
					break;
				}
			}
		}
	}
	
	@Override
	public void setValue(Date value, boolean notifyChange) {
		setValue(value);
		if (notifyChange)
			notifyValueChange();
	}

	@Override
	public void setEnabled(boolean editable) {
		edited = editable;
		hoursBox.setEnabled(editable);
		minutesBox.setEnabled(editable);
	}

	@Override
	public boolean isEdited() {
		return edited;
	}
	
	

}
