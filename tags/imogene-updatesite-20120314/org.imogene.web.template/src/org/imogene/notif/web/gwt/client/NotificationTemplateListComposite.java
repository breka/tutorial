package org.imogene.notif.web.gwt.client;

import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

import org.imogene.gwt.widgets.client.dynaTable.ColumnHeader;
import org.imogene.gwt.widgets.client.dynaTable.DynaTableDataProvider;
import org.imogene.gwt.widgets.client.dynaTable.DynaTableWidget;

/**
 *
 */
public class NotificationTemplateListComposite extends Composite implements ClickHandler {

	private DynaTableWidget dynaTable;

	/**
	 * Creates the composite that 
	 * displays entities in a table widget.
	 */
	public NotificationTemplateListComposite() {
		this(new NotificationTemplateDataProvider());
	}

	/**
	 * Creates the composite that 
	 * displays entities in a table widget.
	 *
	 *@param provider The data provider
	 */
	public NotificationTemplateListComposite(DynaTableDataProvider provider) {
		ColumnHeader[] columns = new ColumnHeader[2];
		String[] styles = new String[2];

		columns[0] = new ColumnHeader("Name",
				"name");
		styles[0] = "style1";
		columns[1] = new ColumnHeader("Title", "title");
		styles[1] = "style1";
		/* IF YOU WANT TO USE AN AUTOREFRESH TABLE */
		/* dynaTable = new AutoRefreshDynaTableWidget(new NotificationTemplateDataProvider(), columns, styles, 15);
		   dynaTable.setResfreshPeriod(30000); */
		dynaTable = new DynaTableWidget(provider, columns, styles, 20);
		dynaTable.addClickHandler(this);
		initWidget(dynaTable);

	}

	/**
	 * Method called when the composite is loaded
	 * Refreshes the enclosed dynatable
	 */
	public void onLoad() {
		dynaTable.refresh();
	}

	@Override
	public void onClick(ClickEvent event) {
		Cell cell = dynaTable.getCellForEvent(event);
		if ((cell.getRowIndex() != 0 && cell.getCellIndex() != 0)
				|| !dynaTable.isCheckable()) {
			String entityId = (String) dynaTable.getIds().get(
					cell.getRowIndex() - 1);
			History.newItem("view/notif/" + entityId, true);
		} else {
			if (cell.getRowIndex() == 0) { // first row == column headers
				dynaTable.setSortColumn(cell.getCellIndex());
				refreshTable();
			}
		}
	}

	/**
	 * Refreshes the enclosed table
	 */
	public void refreshTable() {
		dynaTable.refresh();
	}

	/**
	 * Returns the selected entities IDs 
	 *
	 * @return Set of the selected entities IDs
	 */
	public Set<String> getSelectedRowIds() {
		return dynaTable.getSelectedRowIds();
	}

	/**
	 * Unselect the row id that presents the 
	 * entity with the specified ID.
	 *
	 *@param id The entity ID
	 */
	public void unSelect(String id) {
		dynaTable.unSelect(id);
	}

}
