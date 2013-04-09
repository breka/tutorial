package org.imogene.android.database.sqlite.stmt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class StatementBuilder {

	protected final String tableName;
	protected boolean addTableName;

	protected Where where = null;

	public StatementBuilder(String tableName) {
		this.tableName = tableName;
	}

	public Where where() {
		where = new Where();
		return where;
	}

	public void setWhere(Where where) {
		this.where = where;
	}

	public String prepareStatementString() throws SQLException {
		List<Object> argList = new ArrayList<Object>();
		return buildStatementString(argList);
	}

	public void clear() {
		where = null;
	}

	public String buildStatementString(List<Object> argList) {
		StringBuilder sb = new StringBuilder(128);
		appendStatementString(sb, argList);
		String statement = sb.toString();
		return statement;
	}

	protected void appendStatementString(StringBuilder sb, List<Object> argList) {
		appendStatementStart(sb, argList);
		appendWhereStatement(sb, argList, true);
		appendStatementEnd(sb, argList);
	}

	protected abstract void appendStatementStart(StringBuilder sb, List<Object> argList);

	protected void appendWhereStatement(StringBuilder sb, List<Object> argList, boolean first) {
		if (where == null) {
			return;
		}
		if (first) {
			sb.append("WHERE ");
		} else {
			sb.append("AND (");
		}
		where.appendSql((addTableName ? tableName : null), sb, argList);
		if (!first) {
			sb.append(") ");
		}
	}

	protected abstract void appendStatementEnd(StringBuilder sb, List<Object> argList);

	protected boolean shouldPrependTableNameToColumns() {
		return false;
	}

}
