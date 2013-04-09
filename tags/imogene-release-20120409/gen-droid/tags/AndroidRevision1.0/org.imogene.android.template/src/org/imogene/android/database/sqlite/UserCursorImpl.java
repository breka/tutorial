package org.imogene.android.database.sqlite;

import org.imogene.android.Constants.Keys;
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
		return getString(getColumnIndexOrThrow(Keys.KEY_LOGIN));
	}
	
	public final byte[] getPassword() {
		return getBlob(getColumnIndexOrThrow(Keys.KEY_PASSWORD));
	}
	
	public final String getRoles() {
		return getString(getColumnIndexOrThrow(Keys.KEY_ROLES));
	}

	public abstract String getUserDisplay(Context context);

}
