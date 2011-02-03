package org.imogene.android.database;

import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Tables;
import org.imogene.android.common.Binary;
import org.imogene.android.database.interfaces.DatabaseHelper;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;

public abstract class AbstractDatabase extends SQLiteOpenHelper implements DatabaseHelper {

	private static final String DATABASE_CREATE_CLIENTFILTER = "create table if not exists clientfilter ("
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
			+ " text, "
			+ Keys.KEY_MODIFIEDBY
			+ " text, "
			+ Keys.KEY_MODIFIEDFROM
			+ " text, "
			+ Keys.KEY_UPLOADDATE
			+ " text, "
			+ Keys.KEY_CREATED
			+ " text, "
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
			+ " text, " + Keys.KEY_FILE_NAME + " text);";
	private static final String DATABASE_CREATE_SYNCHISTORY = "create table if not exists "
			+ Tables.TABLE_SYNCHISTORY
			+ " ("
			+ Keys.KEY_ROWID
			+ " integer primary key autoincrement, "
			+ Keys.KEY_ID
			+ " text not null, "
			+ Keys.KEY_DATE
			+ " text not null, "
			+ Keys.KEY_STATUS + " text, " + Keys.KEY_LEVEL + " text);";
	private static final String DATABASE_CREATE_GPSLOCATION = "create table if not exists "
			+ Tables.TABLE_GPSLOCATIONS
			+ " ("
			+ Keys.KEY_ROWID
			+ " integer primary key autoincrement, "
			+ Keys.KEY_ACCURACY
			+ " text, "
			+ Keys.KEY_ALTITUDE
			+ " text, "
			+ Keys.KEY_BEARING
			+ " text, "
			+ Keys.KEY_LATITUDE
			+ " text, "
			+ Keys.KEY_LONGITUDE
			+ " text, "
			+ Keys.KEY_PROVIDER
			+ " text, "
			+ Keys.KEY_SPEED
			+ " text, "
			+ Keys.KEY_TIME
			+ " text, "
			+ Keys.KEY_HASACCURACY
			+ " text, "
			+ Keys.KEY_HASALTITUDE
			+ " text, "
			+ Keys.KEY_HASBEARING + " text, " + Keys.KEY_HASSPEED + " text);";
	
	protected interface Creator<T extends DatabaseHelper> {
		public T getDatabase(Context context);
	}

	protected static Creator<? extends AbstractDatabase> CREATOR;
	protected static AbstractDatabase sSingleton = null;
	
	private final Context mContext;
	
	public synchronized static final AbstractDatabase getSuper(Context context) {
		if (sSingleton == null)
			sSingleton = CREATOR.getDatabase(context);
		return sSingleton;
	}

	public AbstractDatabase(Context context, String name,
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
	}

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
