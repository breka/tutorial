package org.imogene.gwt.widgets.client.dynaTable;

import com.google.gwt.user.client.ui.Label;

/**
 * Column Header
 * 
 * @author MEDES-IMPS
 */
public class ColumnHeader extends Label {
	
	// Bean property to which correspond the column values
	private String property;
	
	// Tells if the column sort order is ascending
	private Boolean asc = false;
	
	
	/**
	 * @param header The text that will be displayed in the column header
	 * @param property The Bean property to which correspond the column values
	 */
	public ColumnHeader(String header, String property) {
		super();
		this.setText(header);
		this.property = property;
		this.removeStyleName("gwt-Label");
	}

	/**
	 * @return The Bean property to which correspond the column values
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property The Bean property to which correspond the column values
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return true if the sort order is ascending
	 */
	public Boolean isAsc() {
		return asc;
	}

	/**
	 * @param asc true if the sort order is ascending
	 */
	public void setAsc(boolean asc) {
		this.asc = asc;
	}
	
}
