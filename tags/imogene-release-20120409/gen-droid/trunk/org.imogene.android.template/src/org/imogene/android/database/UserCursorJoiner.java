package org.imogene.android.database;

import java.util.ArrayList;

import org.imogene.android.database.interfaces.UserCursor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

public class UserCursorJoiner implements UserCursor {

	private final ArrayList<Join> mCursors = new ArrayList<Join>();

	private int current = 0;
	
	public boolean newCursor(UserCursor cursor, Drawable drawable, Uri uri) {
		return mCursors.add(new Join(cursor, uri, drawable));
	}
	
	public Drawable getDrawable() {
		return mCursors.get(current).drawable;
	}
	
	public Uri getUri() {
		return mCursors.get(current).uri;
	}

	public String getLogin() {
		return mCursors.get(current).cursor.getLogin();
	}

	public byte[] getPassword() {
		return mCursors.get(current).cursor.getPassword();
	}

	public String getRoles() {
		return mCursors.get(current).cursor.getRoles();
	}

	public String getUserDisplay(Context context) {
		return mCursors.get(current).cursor.getUserDisplay(context);
	}

	public long getCreated() {
		return mCursors.get(current).cursor.getCreated();
	}

	public String getCreatedBy() {
		return mCursors.get(current).cursor.getCreatedBy();
	}

	public String getId() {
		return mCursors.get(current).cursor.getId();
	}

	public String getMainDisplay(Context context) {
		return mCursors.get(current).cursor.getMainDisplay(context);
	}

	public long getModified() {
		return mCursors.get(current).cursor.getModified();
	}

	public String getModifiedBy() {
		return mCursors.get(current).cursor.getModifiedBy();
	}

	public String getModifiedFrom() {
		return mCursors.get(current).cursor.getModifiedFrom();
	}

	public String getSecondaryDisplay(Context context) {
		return mCursors.get(current).cursor.getSecondaryDisplay(context);
	}

	public boolean getSynchronized() {
		return mCursors.get(current).cursor.getSynchronized();
	}

	public boolean getUnread() {
		return mCursors.get(current).cursor.getUnread();
	}

	public long getUploadDate() {
		return mCursors.get(current).cursor.getUploadDate();
	}

	public void close() {
		for (Join join : mCursors)
			join.cursor.close();
	}

	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		mCursors.get(current).cursor.copyStringToBuffer(columnIndex, buffer);
	}

	public void deactivate() {
		for (Join join : mCursors)
			join.cursor.deactivate();
	}

	public byte[] getBlob(int columnIndex) {
		return mCursors.get(current).cursor.getBlob(columnIndex);
	}

	public int getColumnCount() {
		return mCursors.get(current).cursor.getColumnCount();
	}

	public int getColumnIndex(String columnName) {
		return mCursors.get(current).cursor.getColumnIndex(columnName);
	}

	public int getColumnIndexOrThrow(String columnName)
			throws IllegalArgumentException {
		return mCursors.get(current).cursor.getColumnIndexOrThrow(columnName);
	}

	public String getColumnName(int columnIndex) {
		return mCursors.get(current).cursor.getColumnName(columnIndex);
	}

	public String[] getColumnNames() {
		return mCursors.get(current).cursor.getColumnNames();
	}

	public int getCount() {
		int result = 0;
		for (Join join : mCursors)
			result += join.cursor.getCount();
		return result;
	}

	public double getDouble(int columnIndex) {
		return mCursors.get(current).cursor.getDouble(columnIndex);
	}

	public Bundle getExtras() {
		return mCursors.get(current).cursor.getExtras();
	}

	public float getFloat(int columnIndex) {
		return mCursors.get(current).cursor.getFloat(columnIndex);
	}

	public int getInt(int columnIndex) {
		return mCursors.get(current).cursor.getInt(columnIndex);
	}

	public long getLong(int columnIndex) {
		return mCursors.get(current).cursor.getLong(columnIndex);
	}

	public int getPosition() {
		int result = 0;
		for (int i = 0; i < current; i++)
			result += mCursors.get(i).cursor.getCount();
		return result + mCursors.get(current).cursor.getPosition();
	}

	public short getShort(int columnIndex) {
		return mCursors.get(current).cursor.getShort(columnIndex);
	}

	public String getString(int columnIndex) {
		return mCursors.get(current).cursor.getString(columnIndex);
	}

	public boolean getWantsAllOnMoveCalls() {
		return mCursors.get(current).cursor.getWantsAllOnMoveCalls();
	}

	public boolean isAfterLast() {
		return getCount() == 0 ? true : getPosition() == getCount();
	}

	public boolean isBeforeFirst() {
		return getCount() == 0 ? true : getPosition() == -1;
	}

	public boolean isClosed() {
		boolean result = true;
		for (Join join : mCursors)
			result &= join.cursor.isClosed();
		return result;
	}

	public boolean isFirst() {
		return getCount() != 0 && getPosition() == 0;
	}

	public boolean isLast() {
		return getCount() != 0 && getPosition() == getCount() - 1;
	}

	public boolean isNull(int columnIndex) {
		return mCursors.get(current).cursor.isNull(columnIndex);
	}

	public boolean move(int offset) {
		return moveToPosition(getPosition() + offset);
	}

	public boolean moveToFirst() {
		return moveToPosition(0);
	}

	public boolean moveToLast() {
		return moveToPosition(getCount() - 1);
	}

	public boolean moveToNext() {
		return moveToPosition(getPosition() + 1);
	}

	public boolean moveToPosition(int position) {
		if (position < 0 || position >= getCount())
			return false;
		if (position == getPosition())
			return true;

		int relative = position;
		for (int i = 0; i < mCursors.size(); i++) {
			if (relative < mCursors.get(i).cursor.getCount()) {
				current = i;
				return mCursors.get(i).cursor.moveToPosition(relative);
			} else {
				relative -= mCursors.get(i).cursor.getCount();
			}
		}
		return false;
	}

	public boolean moveToPrevious() {
		return moveToPosition(getPosition() - 1);
	}

	public void registerContentObserver(ContentObserver observer) {
		for (Join join : mCursors)
			join.cursor.registerContentObserver(observer);
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		for (Join join : mCursors)
			join.cursor.registerDataSetObserver(observer);
	}

	public boolean requery() {
		boolean result = true;
		for (Join join : mCursors)
			result &= join.cursor.requery();
		return result;
	}

	public Bundle respond(Bundle extras) {
		return mCursors.get(current).cursor.respond(extras);
	}

	public void setNotificationUri(ContentResolver cr, Uri uri) {
		for (Join join : mCursors)
			join.cursor.setNotificationUri(cr, uri);
	}

	public void unregisterContentObserver(ContentObserver observer) {
		for (Join join : mCursors)
			join.cursor.unregisterContentObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		for (Join join : mCursors)
			join.cursor.unregisterDataSetObserver(observer);
	}
	
	private static class Join {
		
		private final UserCursor cursor;
		private final Drawable drawable;
		private final Uri uri;
		
		public Join(UserCursor cursor, Uri uri, Drawable drawable) {
			this.cursor = cursor;
			this.drawable = drawable;
			this.uri = uri;
		}
	}

}
