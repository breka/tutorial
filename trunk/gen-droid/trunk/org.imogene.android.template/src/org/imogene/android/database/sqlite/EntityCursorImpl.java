package org.imogene.android.database.sqlite;

import java.util.ArrayList;

import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Sync;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.database.GpsTableUtils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.net.Uri;

public abstract class EntityCursorImpl extends SQLiteCursor implements EntityCursor {
	
	public EntityCursorImpl(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}
	
	public abstract String getMainDisplay(Context context);
	
	public abstract String getSecondaryDisplay(Context context);

	public final long getRowId() {
		return getLong(getColumnIndexOrThrow(Keys.KEY_ROWID));
	}

	public final String getId() {
		return getString(getColumnIndexOrThrow(Keys.KEY_ID));
	}

	public final long getModified() {
		return getLong(getColumnIndexOrThrow(Keys.KEY_MODIFIED));
	}

	public final String getModifiedBy() {
		return getString(getColumnIndexOrThrow(Keys.KEY_MODIFIEDBY));
	}

	public final String getModifiedFrom() {
		return getString(getColumnIndexOrThrow(Keys.KEY_MODIFIEDFROM));
	}

	public final long getUploadDate() {
		return getLong(getColumnIndexOrThrow(Keys.KEY_UPLOADDATE));
	}

	public final long getCreated() {
		return getLong(getColumnIndexOrThrow(Keys.KEY_CREATED));
	}

	public final String getCreatedBy() {
		return getString(getColumnIndexOrThrow(Keys.KEY_CREATEDBY));
	}

	public final boolean getUnread() {
		return getInt(getColumnIndexOrThrow(Keys.KEY_UNREAD)) != 0;
	}

	public final boolean getSynchronized() {
		return getInt(getColumnIndexOrThrow(Keys.KEY_SYNCHRONIZED)) != 0;
	}
	
	protected final Uri getEntity(Uri contentUri, String table, int columnIndex) {
		String id = getString(columnIndex);
		SQLiteStatement stat = getDatabase().compileStatement("select count(*) from " + table + " where id='" + id + "' and "+Keys.KEY_MODIFIEDFROM+"!='"+Sync.SYNC_SYSTEM+"'");
		long count = stat.simpleQueryForLong();
		stat.close();
		if (count != 1) {
			return null;
		} else {
			stat = getDatabase().compileStatement("select _id from " + table + " where id='" + id + "'");
			long rowId = stat.simpleQueryForLong();
			stat.close();
			return ContentUris.withAppendedId(contentUri, rowId);
		}
	}
	
	protected final Uri getEntity(Uri contentUri, String table, String key) {
		String id = getString(getColumnIndexOrThrow(Keys.KEY_ID));
		SQLiteStatement stat = getDatabase().compileStatement("select count(*) from " + table + " where " + key + "='" + id + "' and "+Keys.KEY_MODIFIEDFROM+"!='"+Sync.SYNC_SYSTEM+"'");
		long count = stat.simpleQueryForLong();
		stat.close();
		if (count != 1) {
			return null;
		} else {
			stat = getDatabase().compileStatement("select _id from " + table + " where " + key +"='" + id + "'");
			long rowId = stat.simpleQueryForLong();
			stat.close();
			return ContentUris.withAppendedId(contentUri, rowId);
		}
	}
	
	protected final ArrayList<Uri> getEntities(Uri contentUri, String table, String key) {
		ArrayList<Uri> result = new ArrayList<Uri>();
		String id = getString(getColumnIndexOrThrow(Keys.KEY_ID));
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendEq(key, id);
		builder.appendNotEq(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
		Cursor c = getDatabase().query(table, new String[] {Keys.KEY_ROWID}, builder.toSQL(), null, null, null, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			long rowId = c.getLong(0);
			if (rowId != -1)
				result.add(ContentUris.withAppendedId(contentUri, rowId));
		}
		c.close();
		return result;
	}
	
	protected final ArrayList<Uri> getEntities(Uri contentUri, String table, String relTable, String fromKey, String toKey) {
		ArrayList<Uri> result = new ArrayList<Uri>();
		String id = getString(getColumnIndexOrThrow(Keys.KEY_ID));
		
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendIn(Keys.KEY_ID, new SQLiteBuilder(relTable, toKey).appendEq(fromKey, id).create());
		builder.appendNotEq(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
		
		Cursor c = getDatabase().query(table, new String[] {Keys.KEY_ROWID}, builder.toSQL(), null, null, null, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			long rowId = c.getLong(0);
			if (rowId != -1)
				result.add(ContentUris.withAppendedId(contentUri, rowId));
		}
		c.close();
		
		return result;
	}
	
	protected final boolean[] getEnumMulti(String key, int size) {
		boolean[] result = new boolean[size];
		String value = getString(getColumnIndexOrThrow(key));
		if (value != null && value.length() != 0) {
			String[] sub = value.substring(1, value.length()-1).split(",");
			if (size != sub.length)
				return result;
			for (int i = 0; i < sub.length; i++)
				result[i] = Boolean.parseBoolean(sub[i].trim());
		}
		return result;
	}
	
	protected final Long getAsLong(int columnIndex) {
		return FormatHelper.toLong(getString(columnIndex));
	}
	
	protected final Integer getAsInteger(int columnIndex) {
		return FormatHelper.toInteger(getString(columnIndex));
	}
	
	protected final Float getAsFloat(int columnIndex) {
		return FormatHelper.toFloat(getString(columnIndex));
	}
	
	protected final Boolean getAsBoolean(int columnIndex) {
		return FormatHelper.toBoolean(getString(columnIndex));
	}
	
	protected final Location getAsLocation(int columnIndex) {
		Long rowId = getAsLong(columnIndex);
		if (rowId != null) {
			return GpsTableUtils.getLocation(getDatabase(), rowId.longValue());
		} else {
			return null;
		}
	}
}
