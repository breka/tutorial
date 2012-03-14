package org.imogene.android.database.sqlite;

import org.imogene.android.common.ClientFilter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;

public class ClientFilterCursor extends EntityCursorImpl {

	public ClientFilterCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements CursorFactory {
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new ClientFilterCursor(db, masterQuery, editTable, query);
		}
	}

	public final String getUserId() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.USERID));
	}
	public final String getTerminalId() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.TERMINALID));
	}
	public final String getCardEntity() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.CARDENTITY));
	}
	public final String getEntityField() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.ENTITYFIELD));
	}
	public final String getOperator() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.OPERATOR));
	}
	public final String getFieldValue() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.FIELDVALUE));
	}
	public final String getDisplay() {
		return getString(getColumnIndexOrThrow(ClientFilter.Columns.DISPLAY));
	}
	public final Boolean getIsNew() {
		return getAsBoolean(getColumnIndexOrThrow(ClientFilter.Columns.ISNEW));
	}

	@Override
	public String getMainDisplay(Context context) {
		StringBuilder str = new StringBuilder();
		if (getUserId() != null) {
			str.append(getUserId());
			str.append(" ");
		}
		if (getCardEntity() != null) {
			str.append(getCardEntity());
			str.append(" ");
		}
		return str.toString();
	}

	@Override
	public String getSecondaryDisplay(Context context) {
		StringBuilder str = new StringBuilder();
		return str.toString();
	}

}
