package org.imogene.android.widget.field;

import org.imogene.android.database.sqlite.SQLiteBuilder;

public interface ConstraintBuilder {
	
	public boolean onCreateConstraint(String column, SQLiteBuilder builder);

	public void registerConstraintDependent(BaseField<?> dependent);
}
