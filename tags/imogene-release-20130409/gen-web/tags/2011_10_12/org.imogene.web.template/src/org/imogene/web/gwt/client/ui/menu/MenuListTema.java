package org.imogene.web.gwt.client.ui.menu;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * @author Medes-IMPS 
 */
public class MenuListTema extends Composite implements SelectionHandler {
	
	private DisclosurePanel container;
	private VerticalPanel layout;	
	private List<MenuItem> items = new ArrayList<MenuItem>();
	private HTML header;
	
	
	public MenuListTema(){
		layout();
		properties();	
	}
	
	private void layout(){	
		container = new DisclosurePanel();
		
		container.setAnimationEnabled(true);
		layout = new VerticalPanel();	
		container.setContent(layout);
		header = new HTML("");
		container.setHeader(header);
		initWidget(container);
	}
	
	private void properties(){		
		layout.setWidth("100%");
		container.setWidth("100%");
		
		layout.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		container.setStyleName("tema-DisclosurePanel");
		
	}
	
	public void setOpen(boolean open) {
		container.setOpen(open);
	}
	
	public void setHeader (String title) {
		header.setText(title);
	}
	

	@Override
	public void selectionEvent(MenuItem item) {		
		for(MenuItem current: items){
			if(current.equals(item))
				current.setSelected(true);
			else
				current.setSelected(false);
		}
	}
	
	/**
	 * Adds an item to the menu
	 * @param label the item label
	 * @param token the link token
	 * @param color the associated color
	 */
	public void addItem(String label, String token, String color){
		MenuItem item = new MenuItemImpl(label, token, color);
		items.add(item);
		layout.add(item);
		layout.setCellWidth(item, "100%");		
	}
	
	public void addItem(MenuItem item){
		items.add(item);
		layout.add(item);
		layout.setCellWidth(item, "100%");	
	}
	
	/**
	 * Counts the number of items added to the tema
	 * @return the number of items added to the tema
	 */
	public int getItemCount() {
		return items.size();
	}
	
	/**
	 * Adds a line separator
	 */
	public void addSeparator(){
		Label separator = new Label();
		separator.setStyleName("imogene-MenuItemSeparator");
		layout.add(separator);
		layout.setCellHorizontalAlignment(separator, VerticalPanel.ALIGN_LEFT);
	}
	
}
