package org.imogene.web.gwt.client.ui.panel;

import org.imogene.web.gwt.client.ui.menu.MenuList;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * This panel is the main content panel,
 * which contains all panels displayed after
 * a successful login.
 * 
 * This panel displays a list menu and different 
 * information panels on the left side, and a main 
 * content panel on the right side.
 * @author Medes-IMPS
 */
public class MainContentPanel extends Composite {		
	
	private HorizontalPanel layout;	
	private VerticalPanel leftSide;
	private MenuList menuList;	
	private Widget content;
	private SimplePanel specific;
	
			
	public MainContentPanel(){
		layout();
		properties();
	}

	private void layout(){	
		layout = new HorizontalPanel();
		leftSide = new VerticalPanel();
		menuList = new MenuList();
		specific = new SimplePanel();
		leftSide.add(specific);
		leftSide.add(menuList);	
		layout.add(leftSide);				
		initWidget(layout);	
	}
	
	private void properties(){	
		layout.setStyleName("imogene-ContentComposite");
		//layout.setCellWidth(menuList, "250px");		
		layout.setHeight("100%");		
	}
	
	public void setContent(Widget pContent){
		if(content != null)
			layout.remove(content);
		content = pContent;
		content.setWidth("100%");
		layout.add(content);
		layout.setCellWidth(content, "100%");
		
	}
	
	public void addInfoPanel(Composite infoPanel){
		leftSide.add(infoPanel);
	}
	
	/**
	 * Gets the reference of the embedded menu
	 * @return the embedded menu
	 */
	public MenuList getMenuList(){
		return menuList;
	}
	
	public void setSpecificPart(Widget w){
		if(specific.getWidget()!=null)
			throw new RuntimeException("A specific part has already been added");
		else
			specific.add(w);
	}
	
}
