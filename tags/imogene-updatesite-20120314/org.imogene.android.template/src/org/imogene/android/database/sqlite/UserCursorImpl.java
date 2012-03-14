package org.imogene.android.database.sqlite;

import org.imogene.android.common.interfaces.User;
import org.imogene.android.database.interfaces.UserCursor;

import android.content.Context;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;

public abstract class UserCursorImpl extends EntityCursorImpl implements UserCursor {
	
	public UserCursorImpl(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}
	
	public final String getLogin() {
		return getString(getColumnIndexOrThrow(User.Columns.LOGIN));
	}
	
	public final byte[] getPassword() {
		return getBlob(getColumnIndexOrThrow(User.Columns.PASSWORD));
	}
	
	public final String getRoles() {
		return getString(getColumnIndexOrThrow(User.Columns.ROLES));
	}

	public abstract String getUserDisplay(Context context);

}
