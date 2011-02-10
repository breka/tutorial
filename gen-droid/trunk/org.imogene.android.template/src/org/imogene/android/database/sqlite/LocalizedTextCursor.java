package org.imogene.android.database.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class LocalizedTextCursor extends EntityCursorImpl {

	public LocalizedTextCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements CursorFactory {
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new LocalizedTextCursor(db, masterQuery, editTable, query);
		}
	}

	public final String getFieldId() {
		return getString(getColumnIndexOrThrow("fieldId"));
	}
	public final String getLocale() {
		return getString(getColumnIndexOrThrow("locale"));
	}
	public final String getValue() {
		return getString(getColumnIndexOrThrow("value"));
	}

	@Override
	public String getMainDisplay(Context context) {
		StringBuilder str = new StringBuilder();
		return str.toString();
	}

	@Override
	public String getSecondaryDisplay(Context context) {
		StringBuilder str = new StringBuilder();
		return str.toString();
	}

}
