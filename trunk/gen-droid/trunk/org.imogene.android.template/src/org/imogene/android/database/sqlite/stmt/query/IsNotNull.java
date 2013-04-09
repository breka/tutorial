package org.imogene.android.database.sqlite.stmt.query;

import java.util.List;

public class IsNotNull extends BaseComparison {

	public IsNotNull(String columnName) {
		super(columnName, null, true);
	}

	@Override
	public void appendOperation(StringBuilder sb) {
		sb.append("IS NOT NULL ");
	}

	@Override
	public void appendValue(StringBuilder sb, List<Object> argList) {
		// there is no value
	}
}
