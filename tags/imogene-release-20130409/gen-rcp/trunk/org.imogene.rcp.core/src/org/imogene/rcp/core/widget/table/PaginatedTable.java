package org.imogene.rcp.core.widget.table;

import org.eclipse.swt.widgets.Composite;

public interface PaginatedTable {
	
	/**
	 * Get the table composite
	 * @return
	 */
	public abstract Composite getContainerComposite();
	
	/**
	 * Sets the table start row
	 * @param row the table start row
	 */
	public abstract void setStartRow(int row);
	
	/**
	 * Gets the table start row
	 * @return the table start row
	 */
	public abstract int getStartRow();
	
	/**
	 * Sets the table nb of rows per page
	 * @return nb of rows per page
	 */
	public abstract int getMaxRows();
	
	/**
	 * Gets the total nb of rows in database
	 * @return total nb of rows in database
	 */
	public abstract int getTotalNbOfRows();
	
	/**
	 * Refreshes the table
	 */
	public abstract void refreshTable();

}
