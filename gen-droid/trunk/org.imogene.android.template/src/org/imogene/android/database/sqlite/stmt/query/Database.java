package org.imogene.android.database.sqlite.stmt.query;

public class Database {

	public static void appendEscapedEntityName(StringBuilder sb, String name) {
		sb.append('`').append(name).append('`');
	}

	public static void appendLimitValue(StringBuilder sb, long limit, Long offset) {
		sb.append("LIMIT ").append(limit).append(' ');
	}

	public static void appendOffsetValue(StringBuilder sb, long offset) {
		sb.append("OFFSET ").append(offset).append(' ');
	}
	
	public static void appendMaxQuery(StringBuilder sb, String column) {
		sb.append("MAX(").append(column).append(") "); 
	}
}
