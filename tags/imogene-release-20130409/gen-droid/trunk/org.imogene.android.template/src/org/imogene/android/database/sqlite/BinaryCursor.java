package org.imogene.android.database.sqlite;

import java.io.File;

import org.imogene.android.common.Binary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;
import android.net.Uri;

public class BinaryCursor extends EntityCursorImpl {
	
	public BinaryCursor(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}

	public static class Factory implements CursorFactory {
		public Cursor newCursor(SQLiteDatabase db,
				SQLiteCursorDriver masterQuery, String editTable,
				SQLiteQuery query) {
			return new BinaryCursor(db, masterQuery, editTable, query);
		}
	}
	
	public final String getParentEntity() {
		return getString(getColumnIndexOrThrow(Binary.Columns.PARENT_ENTITY));
	}
	
	public final String getParentKey() {
		return getString(getColumnIndexOrThrow(Binary.Columns.PARENT_KEY));
	}
	
	public final String getParentFieldGetter() {
		return getString(getColumnIndexOrThrow(Binary.Columns.PARENT_FIELD_GETTER));
	}
	
	public final String getFileName() {
		return getString(getColumnIndexOrThrow(Binary.Columns.FILE_NAME));
	}
	
	public final String getContentType() {
		return getString(getColumnIndexOrThrow(Binary.Columns.CONTENT_TYPE));
	}
	
	public final long getLength() {
		return getLong(getColumnIndexOrThrow(Binary.Columns.LENGTH));
	}
	
	public final Uri getData() {
		File file = new File(getString(getColumnIndexOrThrow(Binary.Columns.DATA)));
		return file.exists()?Uri.fromFile(file):null;
	}

	@Override
	public String getMainDisplay(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSecondaryDisplay(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
}