package org.imogene.web.gwt.client.ui.menu;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The left side menu which enables to access the entity forms.
 * 
 * @author Medes-IMPS
 */
public class MenuListEntity extends Composite implements SelectionHandler {

	private VerticalPanel layout;
	private Set<MenuItem> items = new HashSet<MenuItem>();

	public MenuListEntity() {

		layout();
		properties();
	}

	private void layout() {
		layout = new VerticalPanel();
		initWidget(layout);
	}

	private void properties() {
		layout.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		layout.setStyleName("imogene-MenuList");
	}

	@Override
	public void selectionEvent(MenuItem item) {
		for (MenuItem current : items) {
			if (current.equals(item))
				current.setSelected(true);
			else
				current.setSelected(false);
		}
	}

	/**
	 * Adds an item to the menu
	 * 
	 * @param label
	 *            the item label
	 * @param token
	 *            the link token
	 * @param color
	 *            the associated color
	 */
	public void addItem(String label, String token, String color) {
		MenuItem item = new MenuItemImpl(label, token, color);
		item.addSelectionHandler(this);
		items.add(item);
		layout.add(item);
		layout.setCellWidth(item, "100%");
	}

	public void addItem(MenuItem item) {
		item.addSelectionHandler(this);
		items.add(item);
		layout.add(item);
		layout.setCellWidth(item, "100%");
	}

	/**
	 * Adds a line separator
	 */
	public void addSeparator() {
		Label separator = new Label();
		separator.setStyleName("imogene-MenuItemSeparator");
		layout.add(separator);
		layout.setCellHorizontalAlignment(separator, VerticalPanel.ALIGN_LEFT);
	}

}
