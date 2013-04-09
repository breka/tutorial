package org.imogene.android.database.sqlite;

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
	
	public static final String queryId(Context context, Uri uri) {
		return ImogDatabase.getInstance(context).queryId(uri);
	}
	
	public static final long queryRowId(Context context, Uri uri, String id) {
		return ImogDatabase.getInstance(context).queryRowId(uri, id);
	}
	
	public static final Uri findInDatabase(Context context, String id) {
		return ImogDatabase.getInstance(context).findInDatabase(id);
	}
	
	public static final long queryForLong(Context context, String sql) {
		SQLiteStatement s = ImogDatabase.getInstance(context).getReadableDatabase().compileStatement(sql);
		long result = s.simpleQueryForLong();
		s.close();
		return result;
	}
	
	public static final String queryForString(Context context, String sql) {
		SQLiteStatement s = ImogDatabase.getInstance(context).getReadableDatabase().compileStatement(sql);
		String result = s.simpleQueryForString();
		s.close();
		return result;
	}

}