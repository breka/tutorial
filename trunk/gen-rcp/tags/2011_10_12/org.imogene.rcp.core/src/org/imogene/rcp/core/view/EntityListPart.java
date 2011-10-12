package org.imogene.rcp.core.view;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.imogene.common.data.Synchronizable;
import org.imogene.rcp.core.widget.table.PaginatedTable;
import org.imogene.sync.client.EntityListener;



public abstract class EntityListPart extends ViewPart implements PaginatedTable, EntityListener {
		
	
	/**
	 * Add a listener for sorting purpose to the specified column.
	 * @param sorter the sorter to use.
	 * @param column the associated column.
	 */
	protected void addSorterListener(final TableViewer viewer, final EntityListSorter sorter, final TableColumn column){
		column.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {
				sorter.setColumn(viewer.getTable().indexOf(column));
				int dir = viewer.getTable().getSortDirection();
				if (viewer.getTable().getSortColumn() == column) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					dir = SWT.DOWN;
				}
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		});		
	}
	

	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.sync.client.EntityListener#entitySavedOrUpdated(org.imogene.rcp.common.data.Synchronizable)
	 */
	public abstract void entitySavedOrUpdated(Synchronizable entity);
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.core.widget.table.PaginatedTable#getContainerComposite()
	 */
	public abstract Composite getContainerComposite();
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.core.widget.table.PaginatedTable#setStartRow(int)
	 */
	public abstract void setStartRow(int row);
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.core.widget.table.PaginatedTable#getStartRow()
	 */
	public abstract int getStartRow();
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.core.widget.table.PaginatedTable#getMaxRows()
	 */
	public abstract int getMaxRows();
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.core.widget.table.PaginatedTable#getTotalNbOfRows()
	 */
	public abstract int getTotalNbOfRows();
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.rcp.core.widget.table.PaginatedTable#refreshTable()
	 */
	public abstract void refreshTable();
	
}
