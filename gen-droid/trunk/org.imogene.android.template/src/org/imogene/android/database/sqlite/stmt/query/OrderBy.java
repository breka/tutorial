package org.imogene.android.database.sqlite.stmt.query;


public class OrderBy {

	private final String columnName;
	private final boolean ascending;

	public OrderBy(String columnName, boolean ascending) {
		this.columnName = columnName;
		this.ascending = ascending;
	}

	public String getColumnName() {
		return columnName;
	}

	public boolean isAscending() {
		return ascending;
	}
}
