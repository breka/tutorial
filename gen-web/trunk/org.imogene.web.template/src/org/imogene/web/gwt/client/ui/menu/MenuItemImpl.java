package org.imogene.web.gwt.client.ui.menu;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;

public class MenuItemImpl extends MenuItem {

	private SimplePanel layout;	
	private Hyperlink link;	
	private String label;	
	private String token;	
	private String color;
	private Set<SelectionHandler> listeners = new HashSet<SelectionHandler>();
	
	
	
	public MenuItemImpl(String pLabel, String pToken, String styleColor){
		label = pLabel;
		token = pToken;
		color = styleColor;
		layout();
		properties();
		behavior();
	}
	
	private void layout(){
		layout = new SimplePanel();
		link = new Hyperlink(label, token);
		layout.setWidget(link);
		initWidget(layout);
	}
	
	private void properties(){
		layout.setWidth("100%");
		layout.setStylePrimaryName("imogene-MenuItem");		
	}
	
	private void behavior(){
		link.addClickHandler(this);
	}		
	
	@Override
	public void onClick(ClickEvent event) {
		for(SelectionHandler handler:listeners){
			handler.selectionEvent(this);
		}
		
	}

	public void setSelected(boolean selected){
		if(selected)
			layout.addStyleDependentName(color+"selected");
		else
			layout.removeStyleDependentName(color+"selected");
	}
	
	public void addSelectionHandler(SelectionHandler handler){
		listeners.add(handler);
	}
	
	public void removeSelectionHandler(SelectionHandler handler){
		listeners.remove(handler);
	}
		
}
