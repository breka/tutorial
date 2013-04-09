package org.imogene.web.gwt.client.ui.panel;

import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Rounded panel that wraps content in a deco panel 
 * @author Medes-IMPS
 */
public class RoundedWrapperPanel extends Composite {	
	
	private String title;	
	private Widget child;	
	private VerticalPanel layout;	
	private HorizontalPanel titlePanel;	
	private Label titleLabel;	
	private DecoratorPanel decoPanel;	
	private String color;	
	private Hyperlink[] links;	
	private MenuBar entityMenuBar = null;
	
	
	/**	 
	 * @param pTitle the wrapper title	
	 * @param colorStyle the deco panel color/style
	 */
	public RoundedWrapperPanel(String pTitle, String colorStyle){		
		this(pTitle, colorStyle, null);
	}
	/**	 
	 * @param pTitle the wrapper title
	 * @param pChild the content child
	 * @param colorStyle the deco panel color/style
	 */
	public RoundedWrapperPanel(String pTitle, Widget pChild, String colorStyle){		
		this(pTitle, pChild, colorStyle, null);
	}
	
	/**
	 * @param pTitle the wrapper title	
	 * @param colorStyle the deco panel color/style
	 * @param pCommands the top wrapper commands
	 */
	public RoundedWrapperPanel(String pTitle, String colorStyle, Hyperlink[] pCommands){		
		this(pTitle, null, colorStyle, pCommands);
	}
	
	public RoundedWrapperPanel(Map<String, Command> commands, String pTitle, Widget pChild, String colorStyle){		
		child = pChild;
		title = pTitle;
		color = colorStyle;
		if (commands!=null && commands.size()>0)
			addMenuItems(commands);
		layout();
		properties();
	}
	
	/**
	 * @param pTitle the wrapper title
	 * @param pChild the content child
	 * @param colorStyle the deco panel color/style
	 * @param pCommands the top wrapper commands
	 */
	public RoundedWrapperPanel(String pTitle, Widget pChild, String colorStyle, Hyperlink[] pCommands){
		child = pChild;
		title = pTitle;
		color = colorStyle;
		links = pCommands;
		layout();
		properties();
	}
	
	private void layout(){
		decoPanel = new DecoratorPanel();
		layout = new VerticalPanel();
		titlePanel = createTitlePanel();
		
		layout.add(titlePanel);
		if(child != null)
			layout.add(child);
		decoPanel.setWidget(layout);
		initWidget(decoPanel);
	}
	
	private void properties(){			
		layout.setWidth("100%");
		if(child != null)
			child.setWidth("100%");
		titlePanel.setStylePrimaryName("imogene-TableHeader");
		titlePanel.addStyleDependentName(color);
		titleLabel.setStylePrimaryName("imogene-TableTitle");
		titlePanel.setWidth("100%");			
		decoPanel.setStylePrimaryName("imogene-DecoratorPanel");
		decoPanel.addStyleDependentName(color);
		if (entityMenuBar!=null)
			entityMenuBar.setStyleName("imogene-MenuBar");
	}
	
	private HorizontalPanel createTitlePanel(){
		HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		titleLabel = new Label(title);
		titlePanel.add(titleLabel);
		titlePanel.setCellVerticalAlignment(titleLabel, VerticalPanel.ALIGN_TOP);
		if (links != null) {
			for (Hyperlink link : links) {
				titlePanel.add(link);
				titlePanel.setCellHorizontalAlignment(link,
						HorizontalPanel.ALIGN_RIGHT);
				titlePanel.setCellVerticalAlignment(link, VerticalPanel.ALIGN_TOP);
			}
		}
		if (entityMenuBar!=null) {
			titlePanel.add(entityMenuBar);
			titlePanel.setCellHorizontalAlignment(entityMenuBar,HorizontalPanel.ALIGN_RIGHT);
			titlePanel.setCellVerticalAlignment(entityMenuBar, VerticalPanel.ALIGN_TOP);
		}
		return titlePanel;
	}
	
	public void initChild(Widget pChild){
		if(child!=null)
			throw new RuntimeException("Child already initialized.");
		else{
			child = pChild;
			child.setWidth("100%");
			layout.add(child);
		}
	}
	
	private void addMenuItems(Map<String, Command> commands) {
		if(entityMenuBar==null)
			entityMenuBar = new MenuBar();
		else
			entityMenuBar.clearItems();
		
		for (Entry<String, Command> entry:commands.entrySet()) {		
			MenuItem menuItem = new MenuItem(entry.getKey(),entry.getValue());
			entityMenuBar.addItem(menuItem);
		}		
	}
		
}
