package org.imogene.android.database.sqlite.stmt.query;

import java.util.List;

public class IsNull extends BaseComparison {

	public IsNull(String columnName) {
		super(columnName, null, true);
	}

	@Override
	public void appendOperation(StringBuilder sb) {
		sb.append("IS NULL ");
	}

	@Override
	public void appendValue(StringBuilder sb, List<Object> argList) {
		// there is no value
	}
}
