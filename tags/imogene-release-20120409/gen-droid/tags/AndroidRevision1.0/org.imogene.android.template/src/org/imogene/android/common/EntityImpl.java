package org.imogene.android.common;

import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Sync;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.BeanKeyGenerator;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;


public abstract class EntityImpl implements Entity {
	
	private long mRowId = -1;
	private String mId = null;
	private long mModified = 0;
	private String mModifiedBy = null;
	private String mModifiedFrom = null;
	private long mUploadDate = 0;
	private long mCreated = 0;
	private String mCreatedBy = null;
	private boolean mUnread = false;
	private boolean mSynchronized = false;
	
	protected void init(EntityCursor cursor) {
		mRowId = cursor.getRowId();
		mId = cursor.getId();
		mModified = cursor.getModified();
		mModifiedBy = cursor.getModifiedBy();
		mModifiedFrom = cursor.getModifiedFrom();
		mUploadDate = cursor.getUploadDate();
		mCreated = cursor.getCreated();
		mCreatedBy = cursor.getCreatedBy();
		mUnread = cursor.getUnread();
		mSynchronized = cursor.getSynchronized();
	}
	
	public final long getRowId() {
		return mRowId;
	}
	
	public final void setRowId(long rowId) {
		mRowId = rowId;
	}
	
	public final String getId() {
		return mId;
	}
	
	public final void setId(String id) {
		mId = id;
	}

	public final long getModified() {
		return mModified;
	}

	public final void setModified(long modified) {
		mModified = modified;
	}

	public final String getModifiedBy() {
		return mModifiedBy;
	}

	public final void setModifiedBy(String modifiedBy) {
		mModifiedBy = modifiedBy;
	}

	public final String getModifiedFrom() {
		return mModifiedFrom;
	}

	public final void setModifiedFrom(String modifiedFrom) {
		mModifiedFrom = modifiedFrom;
	}

	public final long getUploadDate() {
		return mUploadDate;
	}

	public final void setUploadDate(long uploadDate) {
		mUploadDate = uploadDate;
	}

	public final long getCreated() {
		return mCreated;
	}

	public final void setCreated(long created) {
		mCreated = created;
	}

	public final String getCreatedBy() {
		return mCreatedBy;
	}

	public final void setCreatedBy(String createdBy) {
		mCreatedBy = createdBy;
	}

	public final boolean getUnread() {
		return mUnread;
	}

	public final void setUnread(boolean unread) {
		mUnread = unread;
	}

	public final boolean getSynchronized() {
		return mSynchronized;
	}

	public final void setSynchronized(boolean isSynchronized) {
		mSynchronized = isSynchronized;
	}
	
	protected final long getRowIdFromId(Context context, String id) {
		return SQLiteWrapper.queryRowId(context, getContentUri(), id);
	}
	
	protected abstract String getBeanType();
	
	protected abstract Uri getContentUri();
	
	protected void preCommit(Context context, boolean local, boolean temporary) { /* nothing to do */ }
	
	protected void postCommit(Context context) { /* nothing to do */ }
	
	protected void addValues(Context context, ContentValues values) { /* nothing to do */ }
	
	public Uri commit(Context context, boolean local, boolean temporary) {
		preCommit(context, local, temporary);

		ContentResolver res = context.getContentResolver();
		
		if (local) {
			String login = PreferenceHelper.getCurrentLogin(context);
			if (mId == null)
				mId = BeanKeyGenerator.getNewId(getBeanType());
			if (mRowId != -1) {
				mModifiedBy = login;
				if (temporary)
					mModifiedFrom = Sync.SYNC_SYSTEM;
				else
					mModifiedFrom = PreferenceHelper.getHardwareId(context);
			} else {
				mCreatedBy = mModifiedBy = login;
				if (temporary)
					mModifiedFrom = Sync.SYNC_SYSTEM;
				else
					mModifiedFrom = PreferenceHelper.getHardwareId(context);
			}
			mSynchronized = false;
		} else {
			if (mId == null)
				return null;
			mRowId = getRowIdFromId(context, mId);
		}

		ContentValues values = new ContentValues();
		values.put(Keys.KEY_ID, mId);
		values.put(Keys.KEY_MODIFIEDBY, mModifiedBy);
		values.put(Keys.KEY_MODIFIEDFROM, mModifiedFrom);
		values.put(Keys.KEY_UPLOADDATE, mUploadDate);
		values.put(Keys.KEY_CREATEDBY, mCreatedBy);
		values.put(Keys.KEY_UNREAD, mUnread ? 1 : 0);
		values.put(Keys.KEY_SYNCHRONIZED, mSynchronized ? 1 : 0);

		addValues(context, values);

		if (local) {
			if (mRowId != -1) {
				mModified = PreferenceHelper.getRealTime(context);
			} else {
				mCreated = mModified = PreferenceHelper.getRealTime(context);
			}
		}

		values.put(Keys.KEY_CREATED, mCreated);
		values.put(Keys.KEY_MODIFIED, mModified);

		Uri uri;
		if (mRowId != -1) {
			uri = ContentUris.withAppendedId(getContentUri(), mRowId);
			res.update(uri, values, null, null);
		} else {
			uri = res.insert(getContentUri(), values);
			mRowId = ContentUris.parseId(uri);
		}
		postCommit(context);
		return uri;
	}

}
