package org.imogene.android.database.sqlite;

class SQLiteSelect implements SQLiteRequest {

	private final String where;
	
	public SQLiteSelect(String table, SQLiteWhere where, String... select) {
		StringBuilder str = new StringBuilder();
		str.append("select ");
		boolean first = true;
		for (int i = 0; i < select.length; i++) {
			if (first) {
				first = false;
			} else {
				str.append(',');
			}
			str.append(select[i]);
		}
		str.append(" from "+table+" where ");
		str.append(where.toSQL());
		this.where = str.toString();
	}

	public final String toSQL() {
		return where;
	}
}
