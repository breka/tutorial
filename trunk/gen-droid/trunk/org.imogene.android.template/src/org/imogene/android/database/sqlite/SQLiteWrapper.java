package org.imogene.android.database.sqlite;

import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.provider.AbstractProvider.ImogDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

public final class SQLiteWrapper {

	public static final int delete(Context context, String table, String whereClause, String[] whereArgs) {
		return ImogDatabase.getInstance(context).getWritableDatabase().delete(table, whereClause, whereArgs);
	}
	
	public static final long insert(Context context, String table, String nullColumnHack, ContentValues values) {
		return ImogDatabase.getInstance(context).getWritableDatabase().insert(table, nullColumnHack, values);
	}
	
	public static final int update(Context context, String table, ContentValues values, String whereClause, String[] whereArgs) {
		return ImogDatabase.getInstance(context).getWritableDatabase().update(table, values, whereClause, whereArgs);
	}
	
	public static final Cursor query(Context context, String table, String[] select, String where) {
		return ImogDatabase.getInstance(context).getReadableDatabase().query(table, select, where, null, null, null, null);
	}
	
	public static final Cursor query(Context context, Uri uri, String where, String order) {
		return ImogDatabase.getInstance(context).query(uri, where, order);
	}
	
	public static final Uri findInDatabase(Context context, String id) {
		return ImogDatabase.getInstance(context).findInDatabase(id);
	}
	
	public static final long queryForLong(Context context, String sql) {
		SQLiteStatement s = ImogDatabase.getInstance(context).getReadableDatabase().compileStatement(sql);
		try {
			return s.simpleQueryForLong();
		} finally {
			s.close();
		}
	}
	
	public static final String queryForString(Context context, String sql) {
		SQLiteStatement s = ImogDatabase.getInstance(context).getReadableDatabase().compileStatement(sql);
		try {
			return s.simpleQueryForString();
		} finally {
			s.close();
		}
	}
	
	public static final boolean exist(Context context, String table, String id) {
		String w = new SQLiteBuilder(table, "count(*)").appendEq(Entity.Columns._ID, id).toSQL();
		return queryForLong(context, w) != 0;
	}

}