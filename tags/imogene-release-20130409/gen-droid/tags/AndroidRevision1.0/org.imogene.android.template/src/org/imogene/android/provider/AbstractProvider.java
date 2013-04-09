package org.imogene.android.provider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.imogene.android.Constants;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Paths;
import org.imogene.android.Constants.Tables;
import org.imogene.android.common.Binary;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.common.LocalizedText;
import org.imogene.android.common.SmsComm;
import org.imogene.android.database.sqlite.BinaryCursor;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.imogene.android.database.sqlite.LocalizedTextCursor;
import org.imogene.android.database.sqlite.SmsCommCursor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
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
	private static final int SMS_COMM = 7;
	private static final int SMS_COMM_ID = 8;
	
	protected static final int LAST_INDEX = SMS_COMM_ID;
	
	static {
		sURIMatcher.addURI(Constants.AUTHORITY, Binary.TABLE_NAME, BINARIES);
		sURIMatcher.addURI(Constants.AUTHORITY, Binary.TABLE_NAME + "/#", BINARIES_ID);
		sURIMatcher.addURI(Constants.AUTHORITY, ClientFilter.TABLE_NAME, CLIENT_FILTERS);
		sURIMatcher.addURI(Constants.AUTHORITY, ClientFilter.TABLE_NAME + "/#", CLIENT_FILTERS_ID);
		sURIMatcher.addURI(Constants.AUTHORITY, LocalizedText.TABLE_NAME, TRANSLATABLE_TEXT);
		sURIMatcher.addURI(Constants.AUTHORITY, LocalizedText.TABLE_NAME + "/#", TRANSLATABLE_TEXT_ID);
		sURIMatcher.addURI(Constants.AUTHORITY, SmsComm.TABLE_NAME, SMS_COMM);
		sURIMatcher.addURI(Constants.AUTHORITY, SmsComm.TABLE_NAME + "/#", SMS_COMM_ID);
	}

	protected abstract ImogDatabase getHelper();
	
	protected abstract String getVndDir();
	
	protected abstract String getVndItem();
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result = -1;
		switch (sURIMatcher.match(uri)) {
		case BINARIES:
			result = deleteMultiBinary(Binary.TABLE_NAME, selection, selectionArgs);
			break;
		case BINARIES_ID:
			String binaryId = uri.getPathSegments().get(1);
			result = deleteSingleBinary(Binary.TABLE_NAME, binaryId, selection, selectionArgs);
			break;
		case CLIENT_FILTERS:
			result = deleteMulti(ClientFilter.TABLE_NAME, selection, selectionArgs);
			break;
		case CLIENT_FILTERS_ID:
			String clientFilterId = uri.getPathSegments().get(1);
			result = deleteSingle(ClientFilter.TABLE_NAME, clientFilterId, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT:
			result = deleteMulti(LocalizedText.TABLE_NAME, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT_ID:
			String translatableTextId = uri.getPathSegments().get(1);
			result = deleteSingle(LocalizedText.TABLE_NAME, translatableTextId, selection, selectionArgs);
			break;
		case SMS_COMM:
			result = deleteMulti(SmsComm.TABLE_NAME, selection, selectionArgs);
			break;
		case SMS_COMM_ID:
			String smsCommId = uri.getPathSegments().get(1);
			result = deleteSingle(SmsComm.TABLE_NAME, smsCommId, selection, selectionArgs);
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
			return getVndDir() + Binary.TABLE_NAME;
		case BINARIES_ID:
			SQLiteDatabase sqlDB = getHelper().getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			qb.setTables(Binary.TABLE_NAME);
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
			Cursor c = qb.query(sqlDB, new String[] {Keys.KEY_CONTENT_TYPE}, null, null, null, null, null);
			String result = getVndItem() + "binaries";
			if (c.getCount() == 1) {
				c.moveToFirst();
				result = c.getString(0);
			}
			c.close();
			return result;
		case CLIENT_FILTERS:
			return getVndDir() + ClientFilter.TABLE_NAME;
		case CLIENT_FILTERS_ID:
			return getVndItem() + ClientFilter.TABLE_NAME;
		case TRANSLATABLE_TEXT:
			return getVndDir() + LocalizedText.TABLE_NAME;
		case TRANSLATABLE_TEXT_ID:
			return getVndItem() + LocalizedText.TABLE_NAME;
		case SMS_COMM:
			return getVndDir() + SmsComm.TABLE_NAME;
		case SMS_COMM_ID:
			return getVndItem() + SmsComm.TABLE_NAME;
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
			return insertInTable(ClientFilter.TABLE_NAME, ClientFilter.CONTENT_URI, values);
		case TRANSLATABLE_TEXT:
			return insertInTable(LocalizedText.TABLE_NAME, LocalizedText.CONTENT_URI, values);
		case SMS_COMM:
			return insertInTable(SmsComm.TABLE_NAME, SmsComm.CONTENT_URI, values);
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
			qb.setTables(Binary.TABLE_NAME);
			break;
		case BINARIES_ID:
			qb.setTables(Binary.TABLE_NAME);
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;
		case CLIENT_FILTERS:
			qb.setTables(ClientFilter.TABLE_NAME);
			break;
		case CLIENT_FILTERS_ID:
			qb.setTables(ClientFilter.TABLE_NAME);
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;
		case TRANSLATABLE_TEXT:
			qb.setTables(LocalizedText.TABLE_NAME);
			break;
		case TRANSLATABLE_TEXT_ID:
			qb.setTables(LocalizedText.TABLE_NAME);
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
			break;
		case SMS_COMM:
			qb.setTables(SmsComm.TABLE_NAME);
			break;
		case SMS_COMM_ID:
			qb.setTables(SmsComm.TABLE_NAME);
			qb.appendWhere("_id=" + uri.getPathSegments().get(1));
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
			result = updateMulti(Binary.TABLE_NAME, values, selection, selectionArgs);
			break;
		case BINARIES_ID:
			String binaryId = uri.getPathSegments().get(1);
			result = updateSingle(Binary.TABLE_NAME, binaryId, values, selection,
					selectionArgs);
			break;
		case CLIENT_FILTERS:
			result = updateMulti(ClientFilter.TABLE_NAME, values, selection, selectionArgs);
			break;
		case CLIENT_FILTERS_ID:
			String clientFilterId = uri.getPathSegments().get(1);
			result = updateSingle(ClientFilter.TABLE_NAME, clientFilterId, values, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT:
			result = updateMulti(LocalizedText.TABLE_NAME, values, selection, selectionArgs);
			break;
		case TRANSLATABLE_TEXT_ID:
			String translatableTextId = uri.getPathSegments().get(1);
			result = updateSingle(LocalizedText.TABLE_NAME, translatableTextId, values, selection, selectionArgs);
			break;
		case SMS_COMM:
			result = updateMulti(SmsComm.TABLE_NAME, values, selection, selectionArgs);
			break;
		case SMS_COMM_ID:
			String smsCommId = uri.getPathSegments().get(1);
			result = updateSingle(SmsComm.TABLE_NAME, smsCommId, values, selection, selectionArgs);
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
				+ ClientFilter.TABLE_NAME
				+ " ("
				+ Keys.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ Keys.KEY_ID
				+ " text not null, "
				+ Keys.KEY_MODIFIED
				+ " integer, "
				+ Keys.KEY_MODIFIEDBY
				+ " text, "
				+ Keys.KEY_MODIFIEDFROM
				+ " text, "
				+ Keys.KEY_UPLOADDATE
				+ " integer, "
				+ Keys.KEY_CREATED
				+ " integer, "
				+ Keys.KEY_CREATEDBY
				+ " text, "
				+ Keys.KEY_UNREAD
				+ " integer, "
				+ Keys.KEY_SYNCHRONIZED
				+ " integer, "
				+ Keys.KEY_USERID
				+ " text, "
				+ Keys.KEY_TERMINALID
				+ " text, "
				+ Keys.KEY_CARDENTITY
				+ " text, "
				+ Keys.KEY_ENTITYFIELD
				+ " text, "
				+ Keys.KEY_OPERATOR
				+ " text, "
				+ Keys.KEY_FIELDVALUE
				+ " text, "
				+ Keys.KEY_DISPLAY
				+ " text, "
				+ Keys.KEY_ISNEW
				+ " text);";
		private static final String DATABASE_CREATE_BINARIES = "create table if not exists "
				+ Binary.TABLE_NAME
				+ " ("
				+ Keys.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ Keys.KEY_ID
				+ " text not null, "
				+ Keys.KEY_MODIFIED
				+ " integer, "
				+ Keys.KEY_MODIFIEDBY
				+ " text, "
				+ Keys.KEY_MODIFIEDFROM
				+ " text, "
				+ Keys.KEY_UPLOADDATE
				+ " integer, "
				+ Keys.KEY_CREATED
				+ " integer, "
				+ Keys.KEY_CREATEDBY
				+ " text, "
				+ Keys.KEY_LENGTH
				+ " text, "
				+ Keys.KEY_DATA
				+ " text, "
				+ Keys.KEY_CONTENT_TYPE
				+ " text, "
				+ Keys.KEY_PARENT_ENTITY
				+ " text, "
				+ Keys.KEY_PARENT_KEY
				+ " text, "
				+ Keys.KEY_PARENT_FIELD_GETTER
				+ " text, "
				+ Keys.KEY_FILE_NAME
				+ " text);";
		private static final String DATABASE_CREATE_SYNCHISTORY = "create table if not exists "
				+ Tables.TABLE_SYNCHISTORY
				+ " ("
				+ Keys.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ Keys.KEY_ID
				+ " text not null, "
				+ Keys.KEY_DATE
				+ " integer not null, "
				+ Keys.KEY_STATUS
				+ " integer, "
				+ Keys.KEY_LEVEL
				+ " integer);";
		private static final String DATABASE_CREATE_GPSLOCATION = "create table if not exists "
				+ Tables.TABLE_GPSLOCATIONS
				+ " ("
				+ Keys.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ Keys.KEY_ACCURACY
				+ " real, "
				+ Keys.KEY_ALTITUDE
				+ " real, "
				+ Keys.KEY_BEARING
				+ " real, "
				+ Keys.KEY_LATITUDE
				+ " real, "
				+ Keys.KEY_LONGITUDE
				+ " real, "
				+ Keys.KEY_PROVIDER
				+ " text, "
				+ Keys.KEY_SPEED
				+ " real, "
				+ Keys.KEY_TIME
				+ " integer, "
				+ Keys.KEY_HASACCURACY
				+ " integer, "
				+ Keys.KEY_HASALTITUDE
				+ " integer, "
				+ Keys.KEY_HASBEARING
				+ " integer, "
				+ Keys.KEY_HASSPEED
				+ " integer);";
		private static final String DATABASE_CREATE_TRANSLATABLETEXT = "create table if not exists "
				+ LocalizedText.TABLE_NAME
				+ " ("
				+ Keys.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ Keys.KEY_MODIFIED
				+ " integer, "
				+ Keys.KEY_MODIFIEDBY
				+ " text, "
				+ Keys.KEY_MODIFIEDFROM
				+ " text, "
				+ Keys.KEY_UPLOADDATE
				+ " integer, "
				+ Keys.KEY_CREATED
				+ " integer, "
				+ Keys.KEY_CREATEDBY
				+ " text, "
				+ Keys.KEY_UNREAD
				+ " integer, "
				+ Keys.KEY_SYNCHRONIZED
				+ " integer, "
				+ Keys.KEY_FIELD_ID
				+ " text, "
				+ Keys.KEY_LOCALE
				+ " text, "
				+ Keys.KEY_VALUE
				+ " text, "
				+ Keys.KEY_ORIGINAL_VALUE
				+ " integer, "
				+ Keys.KEY_POTENTIALY_WRONG
				+ " integer, "
				+ Keys.KEY_ID
				+ " text not null);";
		private static final String DATABASE_CREATE_SMSCOMM = "create table if not exists "
				+ SmsComm.TABLE_NAME
				+ " ("
				+ Keys.KEY_ROWID
				+ " integer primary key autoincrement, "
				+ Keys.KEY_ENTITY_URI
				+ " text, "
				+ Keys.KEY_SENT_DATE
				+ " integer, "
				+ Keys.KEY_RESPONSE
				+ " text, "
				+ Keys.KEY_SMS_STATUS
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
			db.execSQL(DATABASE_CREATE_SMSCOMM);
		}
		
		public Cursor query(Uri uri, String where, String order) {
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			switch (sURIMatcher.match(uri)) {
			case BINARIES:
				qb.setCursorFactory(new BinaryCursor.Factory());
				qb.setTables(Binary.TABLE_NAME);
				break;
			case BINARIES_ID:
				qb.setCursorFactory(new BinaryCursor.Factory());
				qb.setTables(Binary.TABLE_NAME);
				qb.appendWhere("_id=" + uri.getPathSegments().get(1));
				break;
			case CLIENT_FILTERS:
				qb.setCursorFactory(new ClientFilterCursor.Factory());
				qb.setTables(ClientFilter.TABLE_NAME);
				break;
			case CLIENT_FILTERS_ID:
				qb.setCursorFactory(new ClientFilterCursor.Factory());
				qb.setTables(ClientFilter.TABLE_NAME);
				qb.appendWhere("_id=" + uri.getPathSegments().get(1));
				break;
			case TRANSLATABLE_TEXT:
				qb.setCursorFactory(new LocalizedTextCursor.Factory());
				qb.setTables(LocalizedText.TABLE_NAME);
				break;
			case TRANSLATABLE_TEXT_ID:
				qb.setCursorFactory(new LocalizedTextCursor.Factory());
				qb.setTables(LocalizedText.TABLE_NAME);
				qb.appendWhere("_id=" + uri.getPathSegments().get(1));
				break;
			case SMS_COMM:
				qb.setCursorFactory(new SmsCommCursor.Factory());
				qb.setTables(SmsComm.TABLE_NAME);
				break;
			case SMS_COMM_ID:
				qb.setCursorFactory(new SmsCommCursor.Factory());
				qb.setTables(SmsComm.TABLE_NAME);
				qb.appendWhere("_id=" + uri.getPathSegments().get(1));
				break;
			default:
				throw new IllegalArgumentException("Unknown URL " + uri);
			}
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = qb.query(db, null, where, null, null, null, order);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		}
		
		public String queryId(Uri uri) {
			if (uri == null)
				return null;
			switch (sURIMatcher.match(uri)) {
			case BINARIES_ID:
				return getEntityId(Binary.TABLE_NAME, uri);
			case CLIENT_FILTERS_ID:
				return getEntityId(ClientFilter.TABLE_NAME, uri);
			case TRANSLATABLE_TEXT_ID:
				return getEntityId(LocalizedText.TABLE_NAME, uri);
			}
			return null;
		}
		
		public long queryRowId(Uri uri, String id) {
			switch (sURIMatcher.match(uri)) {
			case BINARIES:
				return getEntityRowId(Binary.TABLE_NAME, id);
			case CLIENT_FILTERS:
				return getEntityRowId(ClientFilter.TABLE_NAME, id);
			case TRANSLATABLE_TEXT:
				return getEntityRowId(LocalizedText.TABLE_NAME, id);
			}
			return -1;
		}
		
		public abstract Uri findInDatabase(String id);

		protected final Context getContext() {
			return mContext;
		}

		protected final Cursor getEntityCursor(CursorFactory factory, String table,
				Uri uri) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor c = db.rawQueryWithFactory(factory, "select * from " + table
					+ " where _id=" + ContentUris.parseId(uri), null, null);
			c.setNotificationUri(mContext.getContentResolver(), uri);
			return c;
		}

		protected final String getEntityId(String table, Uri uri) {
			if (uri == null)
				return null;
			long rowId = ContentUris.parseId(uri);
			SQLiteDatabase db = getReadableDatabase();
			SQLiteStatement stat = db.compileStatement("select id from " + table
					+ " where _id=" + rowId);
			String result = stat.simpleQueryForString();
			stat.close();
			return result;
		}

		protected final long getEntityRowId(String table, String id) {
			SQLiteDatabase db = getReadableDatabase();
			SQLiteStatement stat = db.compileStatement("select _id from " + table
					+ " where id='" + id + "'");
			try {
				long rowId = stat.simpleQueryForLong();
				stat.close();
				return rowId;
			} catch (Exception e) {
				stat.close();
				return -1;
			}
		}
	}
}
