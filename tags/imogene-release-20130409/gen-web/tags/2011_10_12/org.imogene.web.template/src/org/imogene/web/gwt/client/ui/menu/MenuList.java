package org.imogene.web.gwt.client.ui.menu;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * The left side menu which enables
 * to access the entity forms.
 * @author Medes-IMPS 
 */
public class MenuList extends Composite {
	
	private VerticalPanel layout;	
	
	
	public MenuList(){
		layout();
		properties();	
	}
	
	private void layout(){			
		layout = new VerticalPanel();			
		initWidget(layout);
	}
	
	private void properties(){		
		layout.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		layout.setStyleName("imogene-MenuList");	
		layout.setSpacing(0);
	}

	
	
	public void addItem(MenuListEntity item){
		layout.add(item);
		//layout.setCellWidth(item, "100%");	
	}
	
	public void addItem(MenuListTema item){
		layout.add(item);
		//layout.setCellWidth(item, "100%");	
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
