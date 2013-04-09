package org.imogene.android.common;

import java.util.UUID;

import org.imogene.android.database.sqlite.SQLiteWrapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

public class SyncHistory {
	
	public static class Columns implements BaseColumns {
		public static final String TABLE_NAME = "synchistory";

		public static final String ID = "id";
		public static final String DATE = "date";
		public static final String STATUS = "status";
		public static final String LEVEL = "level";
		
		public static final int STATUS_OK = 0;
		public static final int STATUS_ERROR = 1;

		public static final int LEVEL_SEND = 0;
		public static final int LEVEL_RECEIVE = 1;
		
		public static final String WHERE_ERROR = STATUS + "=" + STATUS_ERROR;
	}
	
	public static SyncHistory getLastErrorSyncHistory(Context context) {
		Cursor cursor = SQLiteWrapper.query(context, Columns.TABLE_NAME, null, Columns.WHERE_ERROR);
		try {
			if (cursor.moveToFirst()) {
				return new SyncHistory(cursor);
			}
		} finally {
			cursor.close();
		}
		return null;
	}
	
	public SyncHistory() {
		
	}
	
	protected SyncHistory(Cursor c) {
		_id = c.getLong(c.getColumnIndexOrThrow(Columns._ID));
		id = UUID.fromString(c.getString(c.getColumnIndexOrThrow(Columns.ID)));
		date = c.getLong(c.getColumnIndexOrThrow(Columns.DATE));
		status = c.getInt(c.getColumnIndexOrThrow(Columns.STATUS));
		level = c.getInt(c.getColumnIndexOrThrow(Columns.LEVEL));
	}
	
	public long _id = -1;
	public UUID id;
	public long date;
	public int status;
	public int level;
	
	public void saveOrUpdate(Context context) {
		ContentValues values = new ContentValues();
		values.put(Columns.ID, id.toString());
		values.put(Columns.DATE, date);
		values.put(Columns.STATUS, status);
		values.put(Columns.LEVEL, level);
		
		if (_id != -1) {
			SQLiteWrapper.update(context, Columns.TABLE_NAME, values, Columns._ID + "=" + _id, null);
		} else {
			_id = SQLiteWrapper.insert(context, Columns.TABLE_NAME, null, values);
		}
	}

}
