package org.imogene.rcp.core.view;

import org.eclipse.jface.viewers.ViewerSorter;

/**
 * This class permits to sort a table by the column values.
 * just extends this class and override the method 'compare' from ViewerSorter.
 * @author Medes-IMPS 
 */

public class EntityListSorter extends ViewerSorter {

	protected int propertyIndex;
	
	protected static final int DESCENDING = 1;
	protected static final int ASCENDING = 0;
	
	protected int direction = DESCENDING;
	
	public EntityListSorter(){
		propertyIndex = 0;
		direction = DESCENDING;
	}
	
	/**
	 * set the column on which it has to sort.
	 * @param column the column index
	 */
	public void setColumn(int column) {
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}
	
}
