package org.imogene.android.database.sqlite;

public class SQLiteSelect implements SQLiteRequest {

	private final String where;
	
	public SQLiteSelect(String table, String select, SQLiteWhere where) {
		StringBuilder str = new StringBuilder();
		str.append("select "+select+" from "+table+" where ");
		str.append(where.toSQL());
		this.where = str.toString();
	}

	public final String toSQL() {
		return where;
	}
}
