package org.imogene.web.gwt.client.ui.menu;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;

public class MenuItemImpl extends MenuItem {

	private SimplePanel layout;	
	private Hyperlink link;	
	private String label;	
	private String token;	
	private String color;
	private Set<SelectionHandler> listeners = new HashSet<SelectionHandler>();
	private boolean selected=false;
	
	
	
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
		MouseOverHandler mouseOver = new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent arg0) {	
				if(!selected)
					layout.addStyleDependentName("selected");
	}
		};
		MouseOutHandler mouseOut = new MouseOutHandler() {
	
			@Override
			public void onMouseOut(MouseOutEvent arg0) {				
				layout.removeStyleDependentName("selected");
			}
		};
		layout.addDomHandler(mouseOver, MouseOverEvent.getType());
		layout.addDomHandler(mouseOut, MouseOutEvent.getType());
		
	}
	
	@SuppressWarnings("deprecation")
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
		this.selected = selected;
		if(selected){
			layout.removeStyleDependentName("selected");
			layout.addStyleDependentName(color+"selected");
		}
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
