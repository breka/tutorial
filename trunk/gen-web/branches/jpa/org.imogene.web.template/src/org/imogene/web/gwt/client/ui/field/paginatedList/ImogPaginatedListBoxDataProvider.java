package org.imogene.web.gwt.client.ui.field.paginatedList;

import org.imogene.common.entity.ImogBean;

/**
 * Interface that describes a data provider for the list box that displays a big
 * quantity of data.
 * 
 * @author MEDES-IMPS
 */
public interface ImogPaginatedListBoxDataProvider {

	/**
	 * Update the rows with incoming data
	 * 
	 * @param startRow
	 *            The index of the starting row
	 * @param maxRows
	 *            number max of row to retrieve
	 * @param acceptor
	 *            The acceptor that have in charge to populate the widget
	 */
	void updateRowData(int startRow, int maxRows, RowDataAcceptor acceptor);

	/**
	 * Fills the listBox with the beans for which the searched text is contained
	 * in the main fields values
	 * 
	 * @param text
	 *            searched value
	 */
	void fullTextSearch(String text);

	/**
	 * Interface that describes the class that has to populate the list box with
	 * the received data.
	 * 
	 * @author MEDES-IMPS
	 * 
	 */
	interface RowDataAcceptor {

		/**
		 * Accept the incoming data to populate the list
		 * 
		 * @param startRow
		 *            the starting row index
		 * @param rowsDisplay
		 *            Array of the values to display
		 * @param rowsValue
		 *            Array of the values associated with the display value.
		 */
		void accept(int startRow, ImogBean[] array, int totalNbOfRows);

		/**
		 * Called when an error occurred retrieving the data.
		 * 
		 * @param caught
		 *            the error caught
		 */
		void failed(Throwable caught);

		/**
		 * Called when no data has to be received
		 */
		void acceptEmpty();
	}
}
