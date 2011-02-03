 

package org.imogene.gwt.widgets.client.dynaTable;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public interface DynaTableDataProvider {

  interface RowDataAcceptor {
    void accept(int startRow, String[][] rows, int totalNbOfRows);
    void accept(int startRow, Widget[][] widgets, int totalNbOfRows);
    void accept(int startRow, Widget[][] widgets, String[] entitiesIds, int totalNbOfRows); 
    void failed(Throwable caught);
  }

  void updateRowData(int startRow, int maxRows, RowDataAcceptor acceptor);
  void updateRowData(int startRow, int maxRows, RowDataAcceptor acceptor, String sortProperty, Boolean sortOrder);
  
	/**
	 * Fills the Dynatable with the beans for which the searched text is contained
	 * in the column fields values
	 * @param text searched value
	 */
	void fullTextSearch(String text);
  
  List<String> getRowDataIds();
}
