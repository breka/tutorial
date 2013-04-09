package org.imogene.web.gwt.client.ui.menu;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;

public abstract class MenuItem extends Composite implements ClickHandler {

	public abstract void setSelected(boolean selected);
	
	public abstract void addSelectionHandler(SelectionHandler handler);
		
}
