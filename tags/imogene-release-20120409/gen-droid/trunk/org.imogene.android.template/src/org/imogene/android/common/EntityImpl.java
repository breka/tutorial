package org.imogene.android.common;

import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.BeanKeyGenerator;
import org.imogene.android.util.content.ContentUrisUtils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;


public abstract class EntityImpl implements Entity {
	
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
	
	protected void prepareForSave(Context context, String beanType) {
		if (mId == null) {
			mId = BeanKeyGenerator.getNewId(getBeanType());
		}
		if (mCreated == 0) {
			mCreated = PreferenceHelper.getRealTime(context);
		}
		if (mUploadDate == 0) {
			mUploadDate = mCreated;
		}
		mModified = 0;
		String login = PreferenceHelper.getCurrentLogin(context);
		mModifiedBy = login;
		if (mCreatedBy == null) {
			mCreatedBy = login;
		}
		mModifiedFrom = PreferenceHelper.getHardwareId(context);
		mSynchronized = false;
	}
	
	protected abstract String getBeanType();
	
	protected abstract Uri getContentUri();
	
	protected void preCommit(Context context) {/* nothing to do */}
	
	protected void postCommit(Context context) { /* nothing to do */ }
	
	protected abstract void addValues(Context context, ContentValues values);
	
	protected Uri saveOrUpdate(Context context, String table) {
		preCommit(context);
		
		ContentValues values = new ContentValues();
		values.put(Columns._ID, mId);
		values.put(Columns.MODIFIED, mModified);
		values.put(Columns.MODIFIEDBY, mModifiedBy);
		values.put(Columns.MODIFIEDFROM, mModifiedFrom);
		values.put(Columns.UPLOADDATE, mUploadDate);
		values.put(Columns.CREATED, mCreated);
		values.put(Columns.CREATEDBY, mCreatedBy);
		values.put(Columns.UNREAD, mUnread ? 1 : 0);
		values.put(Columns.SYNCHRONIZED, mSynchronized ? 1 : 0);
		
		addValues(context, values);
		
		boolean exists = SQLiteWrapper.exist(context, table, mId);
		
		if (mModified == 0) {
			mModified = PreferenceHelper.getRealTime(context);
			values.put(Columns.MODIFIED, mModified);
		}
		
		Uri uri;
		if (exists) {
			uri = ContentUrisUtils.withAppendedId(getContentUri(), mId);
			context.getContentResolver().update(uri, values, null, null);
		} else {
			uri = context.getContentResolver().insert(getContentUri(), values);
		}
		
		postCommit(context);
		
		return uri;
	}
	
}
