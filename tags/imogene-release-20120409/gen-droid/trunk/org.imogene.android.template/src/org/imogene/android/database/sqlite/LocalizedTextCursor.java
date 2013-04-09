package org.imogene.android.database.sqlite;

import org.imogene.android.common.LocalizedText;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;

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
		return getString(getColumnIndexOrThrow(LocalizedText.Columns.FIELD_ID));
	}
	public final String getLocale() {
		return getString(getColumnIndexOrThrow(LocalizedText.Columns.LOCALE));
	}
	public final String getValue() {
		return getString(getColumnIndexOrThrow(LocalizedText.Columns.VALUE));
	}
	public final boolean getOriginalValue() {
		return getInt(getColumnIndexOrThrow(LocalizedText.Columns.ORIGINAL_VALUE)) != 0;
	}
	public final boolean getPotentialyWrong() {
		return getInt(getColumnIndexOrThrow(LocalizedText.Columns.POTENTIALY_WRONG)) != 0;
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
