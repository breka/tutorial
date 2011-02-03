package org.imogene.android.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Paths;
import org.imogene.android.common.Binary;
import org.imogene.android.database.AbstractDatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

public abstract class AbstractProvider extends ContentProvider implements OpenableColumns {
	
	private static final String TAG = AbstractProvider.class.getName();

	protected abstract AbstractDatabase getHelper();
	
	protected abstract boolean matchBinary(Uri uri);
	
	protected final int deleteMulti(String tableName, String where, String[] whereArgs) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.delete(tableName, where, whereArgs);
	}

	protected final int deleteSingle(String tableName, String id, String where,
			String[] whereArgs) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.delete(tableName, "_id=" + id
				+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
				whereArgs);
	}

	protected final int deleteMultiBinary(String tableName, String where,
			String[] whereArgs) {
		Log.i(TAG, "delete multi binary");
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		Cursor cursor = sqlDB.query(tableName, new String[] { Keys.KEY_DATA }, where,
				whereArgs, null, null, null);
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			String path = cursor.getString(0);
			new File(path).delete();
		}
		cursor.close();
		return sqlDB.delete(tableName, where, whereArgs);
	}

	protected final int deleteSingleBinary(String tableName, String id, String where,
			String[] whereArgs) {
		Log.i(TAG, "delete binary");
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		Cursor cursor = sqlDB.query(tableName, new String[] { Keys.KEY_DATA }, "_id="
				+ id
				+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
				whereArgs, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			String path = cursor.getString(cursor.getColumnIndexOrThrow(Keys.KEY_DATA));
			new File(path).delete();
		}
		cursor.close();
		return sqlDB.delete(tableName, "_id=" + id
				+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
				whereArgs);
	}
	
	protected final Uri insertInTableBinary(ContentValues values) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		long rowId = sqlDB.insert(Binary.TABLE_NAME, "", values);
		if (rowId > 0) {
			Uri rowUri = ContentUris.withAppendedId(Binary.CONTENT_URI,	rowId);
			File directory = new File(Paths.PATH_BINARIES);
			directory.mkdirs();
			String filename = System.currentTimeMillis() + ".bin";
			File file = new File(directory, filename);
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
			String path = file.getAbsolutePath();
			values.put(Keys.KEY_DATA, path);
			sqlDB.update(Binary.TABLE_NAME, values, "_id=?", new String[]{"" + rowId});
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into binaries");
	}

	protected final Uri insertInTable(String table, Uri contentUri, ContentValues values) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		long rowId = sqlDB.insert(table, "", values);
		if (rowId > 0) {
			Uri rowUri = ContentUris.withAppendedId(contentUri, rowId);
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into " + table);
	}
	
	protected final int updateMulti(String tableName, ContentValues values,
			String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.update(tableName, values, selection, selectionArgs);
	}

	protected final int updateSingle(String tableName, String id, ContentValues values,
			String selection, String[] selectionArgs) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.update(tableName, values, "_id="
				+ id
				+ (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
						: ""), selectionArgs);
	}

	@Override
	public final ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		if (!matchBinary(uri)) {
			throw new IllegalArgumentException("openFile not supported for directories");
		}
		try {
			return this.openFileHelper(uri, mode);
		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found");
			throw new FileNotFoundException();
		}
	}
}
