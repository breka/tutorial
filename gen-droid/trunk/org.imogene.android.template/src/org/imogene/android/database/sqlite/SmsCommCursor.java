package org.imogene.android.database.sqlite;

import org.imogene.android.Constants.Keys;
import org.imogene.android.util.FormatHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;

public class SmsCommCursor extends SQLiteCursor {

	public SmsCommCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}
	
	public static class Factory implements CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery, String editTable, SQLiteQuery query) {
			return new SmsCommCursor(db, masterQuery, editTable, query);
		}
	}
	
	public long getRowId() {
		return getLong(getColumnIndexOrThrow(Keys.KEY_ROWID));
	}
	
	public String getEntityId() {
		return getString(getColumnIndexOrThrow(Keys.KEY_ENTITY_ID));
	}
	
	public String getDestination() {
		return getString(getColumnIndexOrThrow(Keys.KEY_DESTINATION));
	}
	
	public String getMessage() {
		return getString(getColumnIndexOrThrow(Keys.KEY_MESSAGE));
	}
	
	public Long getSentDate() {
		return getAsLong(getColumnIndexOrThrow(Keys.KEY_SENT_DATE)); 
	}
	
	public Long getDeliveredDate() {
		return getAsLong(getColumnIndexOrThrow(Keys.KEY_DELIVERED_DATE));
	}
	
	public Long getResponseDate() {
		return getAsLong(getColumnIndexOrThrow(Keys.KEY_RESPONSE_DATE));
	}
	
	public String getResponse() {
		return getString(getColumnIndexOrThrow(Keys.KEY_RESPONSE));
	}
	
	public boolean isAck() {
		return getInt(getColumnIndexOrThrow(Keys.KEY_ACK)) != 0;
	}
	
	protected final Long getAsLong(int columnIndex) {
		return FormatHelper.toLong(getString(columnIndex));
	}

}
