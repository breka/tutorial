package org.imogene.android.database.sqlite.stmt.query;

import java.util.List;

public interface Clause {

	public void appendSql(String tableName, StringBuilder sb, List<Object> argList);
}
