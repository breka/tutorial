package org.imogene.android.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.imogene.android.Constants;
import org.imogene.android.Constants.Paths;
import org.imogene.android.common.Binary;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.common.GpsLocation;
import org.imogene.android.common.LocalizedText;
import org.imogene.android.common.SyncHistory;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.BinaryCursor;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.imogene.android.database.sqlite.LocalizedTextCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.util.content.ContentUrisUtils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;

public abstract class AbstractProvider extends ContentProvider implements OpenableColumns {
	
	private static final String TAG = AbstractProvider.class.getName();
	
	protected static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static final int BINARIES = 1;
	private static final int BINARIES_ID = 2;
	private static final int CLIENT_FILTERS = 3;
	private static final int CLIENT_FILTERS_ID = 4;
	private static final int TRANSLATABLE_TEXT = 5;
	private static final int TRANSLATABLE_TEXT_ID = 6;
	
	protected static final int LAST_INDEX = TRANSLATABLE_TEXT_ID;
	
	static {
		sURIMatcher.addURI(Constants.AUTHORITY, Binary.Columns.TABLE_NAME, BINARIES);
		sURIMatcher.addURI(Constants.AUTHORITY, Binary.Columns.TABLE_NAME + "/*", BINARIES_ID);
		sURIMatcher.addURI(Constants.AUTHORITY, ClientFilter.Columns.TABLE_NAME, CLIENT_FILTERS);
		sURIMatcher.addURI(Constants.AUTHORITY, ClientFilter.Columns.TABLE_NAME + "/*", CLIENT_FILTERS_ID);
		sURIMatcher.addURI(Constants.AUTHORITY, LocalizedText.Columns.TABLE_NAME, TRANSLATABLE_TEXT);
		sURIMatcher.addURI(Constants.AUTHORITY, LocalizedText.Columns.TABLE_NAME + "/*", TRANSLATABLE_TEXT_ID);
	}

	protected abstract ImogDatabase getHelper();
	
	protected abstract String getVndDir();
	
	protected abstract String getVndItem();
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = -1;
		switch (sURIMatcher.match(uri)) {
		case BINARIES:
			result = deleteMultiBinary(Binary.Columns.TABLE_NAME, selection, selectionArgs);
			break;
		case BINARIES_ID:
			String binaryId = uri.getLastPathSegment();
			result = deleteSingleBinary(Binary.Columns.TABLE_NAME, binaryId, selection, selectionArgs);
			break;
		case CLIENT_FILTERS:
			result = deleteMulti(ClientFilter.Columns.TABLE_NAME, selection, selectionArgs);
			break;
		case CLIENT_FILTERS_ID:
			String clientFilterId = uri.getLastPathSegment();
			result = deleteSingle(ClientFilter.Columns.TABLE_NAME, clientFilterId, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT:
			result = deleteMulti(LocalizedText.Columns.TABLE_NAME, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT_ID:
			String translatableTextId = uri.getLastPathSegment();
			result = deleteSingle(LocalizedText.Columns.TABLE_NAME, translatableTextId, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return result;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (sURIMatcher.match(uri)) {
		case BINARIES:
			return getVndDir() + Binary.Columns.TABLE_NAME;
		case BINARIES_ID:
			SQLiteDatabase sqlDB = getHelper().getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(Binary.Columns.TABLE_NAME);
			qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
			Cursor c = qb.query(sqlDB, new String[] {Binary.Columns.CONTENT_TYPE}, null, null, null, null, null);
			String result = getVndItem() + "binaries";
			if (c.getCount() == 1) {
				c.moveToFirst();
				result = c.getString(0);
			}
			c.close();
			return result;
		case CLIENT_FILTERS:
			return getVndDir() + ClientFilter.Columns.TABLE_NAME;
		case CLIENT_FILTERS_ID:
			return getVndItem() + ClientFilter.Columns.TABLE_NAME;
		case TRANSLATABLE_TEXT:
			return getVndDir() + LocalizedText.Columns.TABLE_NAME;
		case TRANSLATABLE_TEXT_ID:
			return getVndItem() + LocalizedText.Columns.TABLE_NAME;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (sURIMatcher.match(uri)) {
		case BINARIES:
			return insertInTableBinary(values);
		case CLIENT_FILTERS:
			return insertInTable(ClientFilter.Columns.TABLE_NAME, ClientFilter.Columns.CONTENT_URI, values);
		case TRANSLATABLE_TEXT:
			return insertInTable(LocalizedText.Columns.TABLE_NAME, LocalizedText.Columns.CONTENT_URI, values);
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (sURIMatcher.match(uri)) {
		case BINARIES:
			qb.setTables(Binary.Columns.TABLE_NAME);
			break;
		case BINARIES_ID:
			qb.setTables(Binary.Columns.TABLE_NAME);
			qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
			break;
		case CLIENT_FILTERS:
			qb.setTables(ClientFilter.Columns.TABLE_NAME);
			break;
		case CLIENT_FILTERS_ID:
			qb.setTables(ClientFilter.Columns.TABLE_NAME);
			qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
			break;
		case TRANSLATABLE_TEXT:
			qb.setTables(LocalizedText.Columns.TABLE_NAME);
			break;
		case TRANSLATABLE_TEXT_ID:
			qb.setTables(LocalizedText.Columns.TABLE_NAME);
			qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		Cursor c = qb.query(sqlDB, projection, selection, selectionArgs, null,
				null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int result = -1;
		switch (sURIMatcher.match(uri)) {
		case BINARIES:
			result = updateMulti(Binary.Columns.TABLE_NAME, values, selection, selectionArgs);
			break;
		case BINARIES_ID:
			String binaryId = uri.getLastPathSegment();
			result = updateSingle(Binary.Columns.TABLE_NAME, binaryId, values, selection, selectionArgs);
			break;
		case CLIENT_FILTERS:
			result = updateMulti(ClientFilter.Columns.TABLE_NAME, values, selection, selectionArgs);
			break;
		case CLIENT_FILTERS_ID:
			String clientFilterId = uri.getLastPathSegment();
			result = updateSingle(ClientFilter.Columns.TABLE_NAME, clientFilterId, values, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT:
			result = updateMulti(LocalizedText.Columns.TABLE_NAME, values, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT_ID:
			String translatableTextId = uri.getLastPathSegment();
			result = updateSingle(LocalizedText.Columns.TABLE_NAME, translatableTextId, values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return result;
	}
	
	protected final int deleteMulti(String tableName, String where, String[] whereArgs) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.delete(tableName, where, whereArgs);
	}

	protected final int deleteSingle(String tableName, String id, String where, String[] whereArgs) {
		String w = Entity.Columns._ID + "='" + id + "'" + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.delete(tableName, w, whereArgs);
	}

	protected final int deleteMultiBinary(String tableName, String where, String[] whereArgs) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		Cursor cursor = sqlDB.query(tableName, new String[] { Binary.Columns.DATA }, where, whereArgs, null, null, null);
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			String path = cursor.getString(0);
			new File(path).delete();
		}
		cursor.close();
		return sqlDB.delete(tableName, where, whereArgs);
	}

	protected final int deleteSingleBinary(String tableName, String id, String where, String[] whereArgs) {
		String w = Entity.Columns._ID + "='" + id + "'" + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : "");
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		Cursor cursor = sqlDB.query(tableName, new String[] { Binary.Columns.DATA }, w,	whereArgs, null, null, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			String path = cursor.getString(cursor.getColumnIndexOrThrow(Binary.Columns.DATA));
			new File(path).delete();
		}
		cursor.close();
		return sqlDB.delete(tableName, w, whereArgs);
	}
	
	protected final Uri insertInTableBinary(ContentValues values) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		if (sqlDB.insert(Binary.Columns.TABLE_NAME, "", values) > 0) {
			String id = values.getAsString(Entity.Columns._ID);
			Uri rowUri = ContentUrisUtils.withAppendedId(Binary.Columns.CONTENT_URI, id);
			
			Paths.PATH_BINARIES.mkdirs();

			String filename = System.currentTimeMillis() + ".bin";
			File file = new File(Paths.PATH_BINARIES, filename);
			try {
				file.createNewFile();
			} catch (IOException e) {
				return null;
			}
			String path = file.getAbsolutePath();
			values.put(Binary.Columns.DATA, path);
			sqlDB.update(Binary.Columns.TABLE_NAME, values, Entity.Columns._ID + "='" + id +"'", null);
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		throw new SQLException("Failed to insert row into binaries");
	}

	protected final Uri insertInTable(String table, Uri contentUri, ContentValues values) {
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		long rowId = sqlDB.insert(table, "", values);
		if (rowId > 0) {
			String id = values.getAsString(Entity.Columns._ID);
			Uri rowUri = ContentUrisUtils.withAppendedId(contentUri, id);
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

	protected final int updateSingle(String tableName, String id, ContentValues values, String selection, String[] selectionArgs) {
		String where = Entity.Columns._ID + "='" + id + "'" + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
		SQLiteDatabase sqlDB = getHelper().getWritableDatabase();
		return sqlDB.update(tableName, values, where, selectionArgs);
	}

	@Override
	public final ParcelFileDescriptor openFile(Uri uri, String mode)
			throws FileNotFoundException {
		if (sURIMatcher.match(uri) != BINARIES_ID) {
			throw new IllegalArgumentException("openFile not supported for directories");
		}
		try {
			return this.openFileHelper(uri, mode);
		} catch (FileNotFoundException e) {
			Log.i(TAG, "File not found");
			throw new FileNotFoundException();
		}
	}
	
	public static abstract class ImogDatabase extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE_CLIENTFILTER = "create table if not exists "
				+ ClientFilter.Columns.TABLE_NAME
				+ " ("
				+ ClientFilter.Columns._ID
				+ " text primary key, "
				+ ClientFilter.Columns.MODIFIED
				+ " integer, "
				+ ClientFilter.Columns.MODIFIEDBY
				+ " text, "
				+ ClientFilter.Columns.MODIFIEDFROM
				+ " text, "
				+ ClientFilter.Columns.UPLOADDATE
				+ " integer, "
				+ ClientFilter.Columns.CREATED
				+ " integer, "
				+ ClientFilter.Columns.CREATEDBY
				+ " text, "
				+ ClientFilter.Columns.UNREAD
				+ " integer, "
				+ ClientFilter.Columns.SYNCHRONIZED
				+ " integer, "
				+ ClientFilter.Columns.USERID
				+ " text, "
				+ ClientFilter.Columns.TERMINALID
				+ " text, "
				+ ClientFilter.Columns.CARDENTITY
				+ " text, "
				+ ClientFilter.Columns.ENTITYFIELD
				+ " text, "
				+ ClientFilter.Columns.OPERATOR
				+ " text, "
				+ ClientFilter.Columns.FIELDVALUE
				+ " text, "
				+ ClientFilter.Columns.DISPLAY
				+ " text, "
				+ ClientFilter.Columns.ISNEW
				+ " text);";
		private static final String DATABASE_CREATE_BINARIES = "create table if not exists "
				+ Binary.Columns.TABLE_NAME
				+ " ("
				+ Binary.Columns._ID
				+ " text primary key, "
				+ Binary.Columns.MODIFIED
				+ " integer, "
				+ Binary.Columns.MODIFIEDBY
				+ " text, "
				+ Binary.Columns.MODIFIEDFROM
				+ " text, "
				+ Binary.Columns.UPLOADDATE
				+ " integer, "
				+ Binary.Columns.CREATED
				+ " integer, "
				+ Binary.Columns.CREATEDBY
				+ " text, "
				+ Binary.Columns.UNREAD
				+ " integer, "
				+ Binary.Columns.SYNCHRONIZED
				+ " integer, "
				+ Binary.Columns.LENGTH
				+ " text, "
				+ Binary.Columns.DATA
				+ " text, "
				+ Binary.Columns.CONTENT_TYPE
				+ " text, "
				+ Binary.Columns.PARENT_ENTITY
				+ " text, "
				+ Binary.Columns.PARENT_KEY
				+ " text, "
				+ Binary.Columns.PARENT_FIELD_GETTER
				+ " text, "
				+ Binary.Columns.FILE_NAME
				+ " text);";
		private static final String DATABASE_CREATE_SYNCHISTORY = "create table if not exists "
				+ SyncHistory.Columns.TABLE_NAME
				+ " ("
				+ SyncHistory.Columns._ID
				+ " integer primary key autoincrement, "
				+ SyncHistory.Columns.ID
				+ " text not null, "
				+ SyncHistory.Columns.DATE
				+ " integer not null, "
				+ SyncHistory.Columns.STATUS
				+ " integer, "
				+ SyncHistory.Columns.LEVEL
				+ " integer);";
		private static final String DATABASE_CREATE_GPSLOCATION = "create table if not exists "
				+ GpsLocation.Columns.TABLE_NAME
				+ " ("
				+ GpsLocation.Columns._ID
				+ " integer primary key autoincrement, "
				+ GpsLocation.Columns.ACCURACY
				+ " real, "
				+ GpsLocation.Columns.ALTITUDE
				+ " real, "
				+ GpsLocation.Columns.BEARING
				+ " real, "
				+ GpsLocation.Columns.LATITUDE
				+ " real, "
				+ GpsLocation.Columns.LONGITUDE
				+ " real, "
				+ GpsLocation.Columns.PROVIDER
				+ " text, "
				+ GpsLocation.Columns.SPEED
				+ " real, "
				+ GpsLocation.Columns.TIME
				+ " integer, "
				+ GpsLocation.Columns.HASACCURACY
				+ " integer, "
				+ GpsLocation.Columns.HASALTITUDE
				+ " integer, "
				+ GpsLocation.Columns.HASBEARING
				+ " integer, "
				+ GpsLocation.Columns.HASSPEED
				+ " integer);";
		private static final String DATABASE_CREATE_TRANSLATABLETEXT = "create table if not exists "
				+ LocalizedText.Columns.TABLE_NAME
				+ " ("
				+ LocalizedText.Columns._ID
				+ " text primary key, "
				+ LocalizedText.Columns.MODIFIED
				+ " integer, "
				+ LocalizedText.Columns.MODIFIEDBY
				+ " text, "
				+ LocalizedText.Columns.MODIFIEDFROM
				+ " text, "
				+ LocalizedText.Columns.UPLOADDATE
				+ " integer, "
				+ LocalizedText.Columns.CREATED
				+ " integer, "
				+ LocalizedText.Columns.CREATEDBY
				+ " text, "
				+ LocalizedText.Columns.UNREAD
				+ " integer, "
				+ LocalizedText.Columns.SYNCHRONIZED
				+ " integer, "
				+ LocalizedText.Columns.FIELD_ID
				+ " text, "
				+ LocalizedText.Columns.LOCALE
				+ " text, "
				+ LocalizedText.Columns.VALUE
				+ " text, "
				+ LocalizedText.Columns.ORIGINAL_VALUE
				+ " integer, "
				+ LocalizedText.Columns.POTENTIALY_WRONG
				+ " integer);";
		protected interface Creator<T extends ImogDatabase> {
			public T getDatabase(Context context);
		}

		protected static Creator<? extends ImogDatabase> CREATOR;
		protected static ImogDatabase sSingleton = null;
		
		private final Context mContext;
		
		public synchronized static final ImogDatabase getInstance(Context context) {
			if (sSingleton == null)
				sSingleton = CREATOR.getDatabase(context);
			return sSingleton;
		}
		
		public ImogDatabase(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			mContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_CLIENTFILTER);
			db.execSQL(DATABASE_CREATE_BINARIES);
			db.execSQL(DATABASE_CREATE_SYNCHISTORY);
			db.execSQL(DATABASE_CREATE_GPSLOCATION);
			db.execSQL(DATABASE_CREATE_TRANSLATABLETEXT);
		}
		
		public Cursor query(Uri uri, String where, String order) {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			switch (sURIMatcher.match(uri)) {
			case BINARIES:
				qb.setCursorFactory(new BinaryCursor.Factory());
				qb.setTables(Binary.Columns.TABLE_NAME);
				break;
			case BINARIES_ID:
				qb.setCursorFactory(new BinaryCursor.Factory());
				qb.setTables(Binary.Columns.TABLE_NAME);
				qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
				break;
			case CLIENT_FILTERS:
				qb.setCursorFactory(new ClientFilterCursor.Factory());
				qb.setTables(ClientFilter.Columns.TABLE_NAME);
				break;
			case CLIENT_FILTERS_ID:
				qb.setCursorFactory(new ClientFilterCursor.Factory());
				qb.setTables(ClientFilter.Columns.TABLE_NAME);
				qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
				break;
			case TRANSLATABLE_TEXT:
				qb.setCursorFactory(new LocalizedTextCursor.Factory());
				qb.setTables(LocalizedText.Columns.TABLE_NAME);
				break;
			case TRANSLATABLE_TEXT_ID:
				qb.setCursorFactory(new LocalizedTextCursor.Factory());
				qb.setTables(LocalizedText.Columns.TABLE_NAME);
				qb.appendWhere(Entity.Columns._ID + "='" + uri.getLastPathSegment() + "'");
				break;
			default:
				throw new IllegalArgumentException("Unknown URL " + uri);
			}
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = qb.query(db, null, where, null, null, null, order);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		
		public abstract Uri findInDatabase(String id);

		protected final Context getContext() {
			return mContext;
		}

		protected final Cursor getEntityCursor(CursorFactory factory, String table, Uri uri) {
			SQLiteDatabase db = getReadableDatabase();
			String where = new SQLiteBuilder(table, "*").appendEq(Entity.Columns._ID, uri.getLastPathSegment()).toSQL(); 
			Cursor c = db.rawQueryWithFactory(factory, where, null, null);
			c.setNotificationUri(mContext.getContentResolver(), uri);
			return c;
		}

	}
}
