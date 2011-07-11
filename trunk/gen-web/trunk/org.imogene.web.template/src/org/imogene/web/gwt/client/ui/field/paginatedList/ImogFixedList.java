package org.imogene.web.gwt.client.ui.field.paginatedList;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.imogene.web.gwt.common.entity.ImogBean;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class implements a list that contains bean instances.
 * @author MEDES-IMPS
 */
public class ImogFixedList extends Composite implements ClickHandler {


	/* listbox properties */
	private List<ImogBean> values = new Vector<ImogBean>();
	private ListBox box;
	private Image clearImage;
	private int itemByPage=10;
	
	/* listeners */
	private HashSet<ClickHandler> listeners = new HashSet<ClickHandler>();



	/**
	 * Simple constructor
	 */
	public ImogFixedList(){
		super();
		
		VerticalPanel vertical = new VerticalPanel();
		vertical.setSpacing(0);
		vertical.setStyleName("ImogListBox-PopupPanel-content");
		
		/* the list box */
		box  = new ListBox();
		box.setVisibleItemCount(itemByPage);
		box.addClickHandler(this);
		box.setStyleName("ImogListBox-Listbox ");
		vertical.add(box);
		
		/* the navigation panel */
		HorizontalPanel navbarPanel = new HorizontalPanel();
		navbarPanel.setSpacing(0);
		navbarPanel.setWidth("100%");
		
		/* the clear image*/	
		clearImage = new Image(GWT.getModuleBaseURL()+ "images/icon_clear.gif");
		clearImage.addClickHandler(this);	
		navbarPanel.add(clearImage);
		navbarPanel.setCellHorizontalAlignment(clearImage, HasHorizontalAlignment.ALIGN_RIGHT);
		navbarPanel.setCellVerticalAlignment(clearImage, HasVerticalAlignment.ALIGN_MIDDLE);
		
		vertical.add(navbarPanel);	
		
		initWidget(vertical);	
	}
	
	
	/**
	 * Get the maximum number that 
	 * could by displayed in a page.
	 * @return The maximum number of items in a page
	 */
	public int getItemByPage() {
		return itemByPage;
	}

	/**
	 * Sets the maximum number of item that
	 * could be displayed in a page.
	 * @param itemByPage maximum number of item in page
	 */
	public void setItemByPage(int itemByPage) {
		this.itemByPage = itemByPage;
	}
	
	/**
	 * Adds an item to the listbox
	 * @param display the item display value
	 * @param id the item id
	 * @param value the item value
	 */
	public void addItem(String display, String id, ImogBean value){
		values.add(value);
		box.addItem(display, id);
	}
	
	/**
	 * Gets the value of the listbox that is located at a given index
	 * @param i the index for which the value shall be retrieved
	 * @return the value of the listbox that is located at index i
	 */
	public ImogBean getValue(int i){
		if(i<0)
			return null;
		return values.get(i);
	}

	/**
	 * Gets the lisbox selected value
	 * @return the selected value of the listbox
	 */
	public ImogBean getValue() {		
		return getValue(box.getSelectedIndex());
	}
	
	/**
	 * Gets the display value of the listbox selected value
	 * @return the display value of the listbox selected value
	 */
	public String getDisplayValue() {
		return box.getItemText(box.getSelectedIndex());
	}
	
	public ListBox getListBox() {
		return box;
	}
	
	public Image getClearImage() {
		return clearImage;
	}	

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	public void onClick(ClickEvent event) {
		
		if(event.getSource().equals(box)){
			for(ClickHandler listener:listeners){
				listener.onClick(event);
			}
		}
		
		if(event.getSource().equals(clearImage)){
			box.setSelectedIndex(-1);			
			for(ClickHandler listener:listeners){
				listener.onClick(event);
			}
		}
	}

	/**
	 * Adds a click listener, notified 
	 * when an item is selected in the list box.
	 * @param listener The listener to add
	 */
	public void addClickListener(ClickHandler listener){
		listeners.add(listener);
	}
	
	/**
	 * Removes a click listener
	 * @param listener The listener to remove
	 */
	public void removeClickListener(ClickHandler listener){
		listeners.remove(listener);
	}
	
	
	
}
