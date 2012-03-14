package org.imogene.android.database.sqlite;

import java.util.ArrayList;
import java.util.HashMap;

import org.imogene.android.common.GpsLocation;
import org.imogene.android.common.LocalizedText;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.LocalizedTextList;
import org.imogene.android.util.content.ContentUrisUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;

public abstract class EntityCursorImpl extends SQLiteCursor implements EntityCursor {
	
	private HashMap<Uri, String> mBuffer;

	public EntityCursorImpl(SQLiteDatabase db, SQLiteCursorDriver driver,
			String editTable, SQLiteQuery query) {
		super(db, driver, editTable, query);
	}
	
	@Override
	public void close() {
		super.close();
		if (mBuffer != null) {
			mBuffer.clear();
			mBuffer = null;
		}
	}
	
	public final String getId() {
		return getString(getColumnIndexOrThrow(Entity.Columns._ID));
	}

	public final long getModified() {
		return getLong(getColumnIndexOrThrow(Entity.Columns.MODIFIED));
	}

	public final String getModifiedBy() {
		return getString(getColumnIndexOrThrow(Entity.Columns.MODIFIEDBY));
	}

	public final String getModifiedFrom() {
		return getString(getColumnIndexOrThrow(Entity.Columns.MODIFIEDFROM));
	}

	public final long getUploadDate() {
		return getLong(getColumnIndexOrThrow(Entity.Columns.UPLOADDATE));
	}

	public final long getCreated() {
		return getLong(getColumnIndexOrThrow(Entity.Columns.CREATED));
	}

	public final String getCreatedBy() {
		return getString(getColumnIndexOrThrow(Entity.Columns.CREATEDBY));
	}

	public final boolean getUnread() {
		return getInt(getColumnIndexOrThrow(Entity.Columns.UNREAD)) != 0;
	}

	public final boolean getSynchronized() {
		return getInt(getColumnIndexOrThrow(Entity.Columns.SYNCHRONIZED)) != 0;
	}
	
	protected final Uri getEntity(Uri contentUri, String table, int columnIndex) {
		String id = getString(columnIndex);
		SQLiteBuilder builder = new SQLiteBuilder(table, "count(*)");
		builder.appendEq(Entity.Columns._ID, id);
		builder.appendNotEq(Entity.Columns.MODIFIEDFROM, Entity.Columns.SYNC_SYSTEM);
		SQLiteStatement stat = getDatabase().compileStatement(builder.toSQL());
		long count = stat.simpleQueryForLong();
		stat.close();
		if (count != 1) {
			return null;
		} else {
			builder.setSelect(Entity.Columns._ID);
			stat = getDatabase().compileStatement(builder.toSQL());
			String sId = stat.simpleQueryForString();
			stat.close();
			return ContentUrisUtils.withAppendedId(contentUri, sId);
		}
	}
	
	protected final Uri getEntity(Uri contentUri, String table, String key) {
		String id = getString(getColumnIndexOrThrow(Entity.Columns._ID));
		SQLiteBuilder builder = new SQLiteBuilder(table, "count(*)");
		builder.appendEq(key, id);
		builder.appendNotEq(Entity.Columns.MODIFIEDFROM, Entity.Columns.SYNC_SYSTEM);
		SQLiteStatement stat = getDatabase().compileStatement(builder.toSQL());
		long count = stat.simpleQueryForLong();
		stat.close();
		if (count != 1) {
			return null;
		} else {
			builder.setSelect(Entity.Columns._ID);
			stat = getDatabase().compileStatement(builder.toSQL());
			String sId = stat.simpleQueryForString();
			stat.close();
			return ContentUrisUtils.withAppendedId(contentUri, sId);
		}
	}
	
	protected final ArrayList<Uri> getEntities(Uri contentUri, String table, String key) {
		ArrayList<Uri> result = new ArrayList<Uri>();
		String id = getString(getColumnIndexOrThrow(Entity.Columns._ID));
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendEq(key, id);
		builder.appendNotEq(Entity.Columns.MODIFIEDFROM, Entity.Columns.SYNC_SYSTEM);
		Cursor c = getDatabase().query(table, new String[] {Entity.Columns._ID}, builder.toSQL(), null, null, null, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			String sId = c.getString(0);
			if (!TextUtils.isEmpty(sId))
				result.add(ContentUrisUtils.withAppendedId(contentUri, sId));
		}
		c.close();
		return result;
	}
	
	protected final ArrayList<Uri> getEntities(Uri contentUri, String table, String relTable, String fromKey, String toKey) {
		ArrayList<Uri> result = new ArrayList<Uri>();
		String id = getString(getColumnIndexOrThrow(Entity.Columns._ID));
		
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendIn(Entity.Columns._ID, new SQLiteBuilder(relTable, toKey).appendEq(fromKey, id).create());
		builder.appendNotEq(Entity.Columns.MODIFIEDFROM, Entity.Columns.SYNC_SYSTEM);
		
		Cursor c = getDatabase().query(table, new String[] {Entity.Columns._ID}, builder.toSQL(), null, null, null, null);
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			String sId = c.getString(0);
			if (!TextUtils.isEmpty(sId))
				result.add(ContentUrisUtils.withAppendedId(contentUri, sId));
		}
		c.close();
		
		return result;
	}
	
	protected final LocalizedTextList getLocalizedText(int columnIndex) {
		String key = getString(columnIndex);
		LocalizedTextList result = null;
		
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendEq(LocalizedText.Columns.FIELD_ID, key);
		builder.appendNotEq(LocalizedText.Columns.MODIFIEDFROM, LocalizedText.Columns.SYNC_SYSTEM);
		
		LocalizedTextCursor c = (LocalizedTextCursor) SQLiteWrapper.query(null, LocalizedText.Columns.CONTENT_URI, builder.toSQL(), null);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if (result == null) {
				result = new LocalizedTextList(key);
			}
			result.add(new LocalizedText(c));
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
			return GpsLocation.getLocation(null, rowId.longValue());
		} else {
			return null;
		}
	}
	
	protected final void buildRelationDisplay(Context context, StringBuilder builder, Uri uri) {
		if (uri != null) {
			if (mBuffer == null) {
				mBuffer = new HashMap<Uri, String>();
			}
			if (mBuffer.containsKey(uri)) {
				builder.append(mBuffer.get(uri)).append(" ");
			} else {
				EntityCursor c = (EntityCursor) SQLiteWrapper.query(context, uri, null, null);
				c.moveToFirst();
				String main = c.getMainDisplay(context);
				c.close();
				builder.append(main).append(" ");
				mBuffer.put(uri, main);
			}
		}
	}
}
