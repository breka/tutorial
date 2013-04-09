package org.imogene.android.database.sqlite.stmt.query;

import java.util.List;

interface Comparison extends Clause {

	public String getColumnName();

	public void appendOperation(StringBuilder sb);

	public void appendValue(StringBuilder sb, List<Object> argList);
}
