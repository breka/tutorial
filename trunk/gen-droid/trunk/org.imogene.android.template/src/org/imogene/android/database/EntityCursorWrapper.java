package org.imogene.android.database;

import java.util.HashSet;

import org.imogene.android.database.interfaces.EntityCursor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

public class EntityCursorWrapper implements EntityCursor {
	
	public interface OnNotification {
		public EntityCursor changeCursor(EntityCursor old);
	}
	
	protected EntityCursor mCursor;
	
	private final HashSet<ContentObserver> mContentObservers = new HashSet<ContentObserver>();
	private final HashSet<DataSetObserver> mDataSetObservers = new HashSet<DataSetObserver>();
	
	private final OnNotification mOnNotification;
	
	public EntityCursorWrapper(Context context, OnNotification onNotification) {
		mOnNotification = onNotification;
	}
	
	public void setInternalCursor(EntityCursor cursor) {
		mCursor = cursor;
	}
	
	public long getCreated() {
		return mCursor.getCreated();
	}

	public String getCreatedBy() {
		return mCursor.getCreatedBy();
	}

	public String getId() {
		return mCursor.getId();
	}

	public long getModified() {
		return mCursor.getModified();
	}

	public String getModifiedBy() {
		return mCursor.getModifiedBy();
	}

	public long getUploadDate() {
		return mCursor.getUploadDate();
	}
	
	public String getModifiedFrom() {
		return mCursor.getModifiedFrom();
	}
	
	public long getRowId() {
		return mCursor.getRowId();
	}
	
	public boolean getUnread() {
		return mCursor.getUnread();
	}
	
	public boolean getSynchronized() {
		return mCursor.getSynchronized();
	}
	
	public String getMainDisplay(Context context) {
		return mCursor.getMainDisplay(context);
	}
	
	public String getSecondaryDisplay(Context context) {
		return mCursor.getSecondaryDisplay(context);
	}
	
//	public void fillWindow(int pos, CursorWindow winow) {
//		mCursor.fillWindow(pos, winow);
//	}
//
//	public CursorWindow getWindow() {
//		return mCursor.getWindow();
//	}
//
//	public boolean onMove(int oldPosition, int newPosition) {
//		return mCursor.onMove(oldPosition, newPosition);
//	}

	public void close() {
		mCursor.close();
	}

	public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {
		mCursor.copyStringToBuffer(columnIndex, buffer);
	}

	public void deactivate() {
		mCursor.deactivate();
	}

	public byte[] getBlob(int columnIndex) {
		return mCursor.getBlob(columnIndex);
	}

	public int getColumnCount() {
		return mCursor.getColumnCount();
	}

	public int getColumnIndex(String columnName) {
		return mCursor.getColumnIndex(columnName);
	}

	public int getColumnIndexOrThrow(String columnName)
			throws IllegalArgumentException {
		return mCursor.getColumnIndexOrThrow(columnName);
	}

	public String getColumnName(int columnIndex) {
		return mCursor.getColumnName(columnIndex);
	}

	public String[] getColumnNames() {
		return mCursor.getColumnNames();
	}

	public int getCount() {
		return mCursor.getCount();
	}

	public double getDouble(int columnIndex) {
		return mCursor.getDouble(columnIndex);
	}

	public Bundle getExtras() {
		return mCursor.getExtras();
	}

	public float getFloat(int columnIndex) {
		return mCursor.getFloat(columnIndex);
	}

	public int getInt(int columnIndex) {
		return mCursor.getInt(columnIndex);
	}

	public long getLong(int columnIndex) {
		return mCursor.getLong(columnIndex);
	}

	public int getPosition() {
		return mCursor.getPosition();
	}

	public short getShort(int columnIndex) {
		return mCursor.getShort(columnIndex);
	}

	public String getString(int columnIndex) {
		return mCursor.getString(columnIndex);
	}

	public boolean getWantsAllOnMoveCalls() {
		return mCursor.getWantsAllOnMoveCalls();
	}

	public boolean isAfterLast() {
		return mCursor.isAfterLast();
	}

	public boolean isBeforeFirst() {
		return mCursor.isBeforeFirst();
	}

	public boolean isClosed() {
		return mCursor.isClosed();
	}

	public boolean isFirst() {
		return mCursor.isFirst();
	}

	public boolean isLast() {
		return mCursor.isLast();
	}

	public boolean isNull(int columnIndex) {
		return mCursor.isNull(columnIndex);
	}

	public boolean move(int offset) {
		return mCursor.move(offset);
	}

	public boolean moveToFirst() {
		return mCursor.moveToFirst();
	}

	public boolean moveToLast() {
		return mCursor.moveToLast();
	}

	public boolean moveToNext() {
		return mCursor.moveToNext();
	}

	public boolean moveToPosition(int position) {
		return mCursor.moveToPosition(position);
	}

	public boolean moveToPrevious() {
		return mCursor.moveToPrevious();
	}

	public void registerContentObserver(ContentObserver observer) {
		mContentObservers.add(observer);
		mCursor.registerContentObserver(observer);
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mDataSetObservers.add(observer);
		mCursor.registerDataSetObserver(observer);
	}

	public boolean requery() {
		for (ContentObserver observer : mContentObservers)
			mCursor.unregisterContentObserver(observer);
		for (DataSetObserver observer : mDataSetObservers)
			mCursor.unregisterDataSetObserver(observer);
		
		mCursor = mOnNotification.changeCursor(mCursor);
		
		for (ContentObserver observer : mContentObservers)
			mCursor.registerContentObserver(observer);
		for (DataSetObserver observer : mDataSetObservers)
			mCursor.registerDataSetObserver(observer);
		return mCursor.requery();
	}

	public Bundle respond(Bundle extras) {
		return mCursor.respond(extras);
	}

	public void setNotificationUri(ContentResolver cr, Uri uri) {
		mCursor.setNotificationUri(cr, uri);
	}

	public void unregisterContentObserver(ContentObserver observer) {
		mContentObservers.remove(observer);
		mCursor.unregisterContentObserver(observer);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mDataSetObservers.remove(observer);
		mCursor.unregisterDataSetObserver(observer);
	}
}
