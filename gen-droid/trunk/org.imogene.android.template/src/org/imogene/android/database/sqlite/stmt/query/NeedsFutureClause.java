package org.imogene.android.database.sqlite.stmt.query;

public interface NeedsFutureClause extends Clause {

	public void setMissingClause(Clause right);
}
