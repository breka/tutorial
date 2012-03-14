package org.imogene.android.database.sqlite;

import java.util.List;

class SQLiteWhere implements SQLiteRequest {

	public static enum Separator {
		AND(" and "), OR(" or ");

		private final String mSeparator;

		private Separator(String separator) {
			mSeparator = separator;
		}

		public String get() {
			return mSeparator;
		}
	}

	public final String where;

	public SQLiteWhere(List<String> clauses, Separator separator) {
		StringBuilder str = new StringBuilder();
		boolean first = true;
		for (String clause : clauses) {
			if (first)
				first = false;
			else
				str.append(separator.get());
			str.append(clause);
		}
		where = str.toString();
	}

	public final String toSQL() {
		return where;
	}
}
