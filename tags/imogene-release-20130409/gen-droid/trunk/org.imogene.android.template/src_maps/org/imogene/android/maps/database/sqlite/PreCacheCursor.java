package org.imogene.android.maps.database.sqlite;

import org.imogene.android.maps.database.PreCache;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class PreCacheCursor extends SQLiteCursor {

	public PreCacheCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}
	
	public static class Factory implements CursorFactory {
		@Override
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new PreCacheCursor(db, masterQuery, editTable, query);
		}
	}
	
	public long getId() {
		return getLong(getColumnIndexOrThrow(PreCache.Columns._ID));
	}
	
	public String getProvider() {
		return getString(getColumnIndexOrThrow(PreCache.Columns.PROVIDER));
	}
	
	public double getNorth() {
		return getDouble(getColumnIndexOrThrow(PreCache.Columns.NORTH));
	}
	
	public double getEast() {
		return getDouble(getColumnIndexOrThrow(PreCache.Columns.EAST));
	}
	
	public double getSouth() {
		return getDouble(getColumnIndexOrThrow(PreCache.Columns.SOUTH));
	}
	
	public double getWest() {
		return getDouble(getColumnIndexOrThrow(PreCache.Columns.WEST));
	}
	
	public int getZoomMin() {
		return getInt(getColumnIndexOrThrow(PreCache.Columns.ZOOM_MIN));
	}
	
	public int getZoomMax() {
		return getInt(getColumnIndexOrThrow(PreCache.Columns.ZOOM_MAX));
	}

}
