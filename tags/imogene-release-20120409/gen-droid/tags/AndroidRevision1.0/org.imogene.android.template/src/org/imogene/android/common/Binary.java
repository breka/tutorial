package org.imogene.android.common;

import java.io.InputStream;
import java.io.OutputStream;

import org.imogene.android.Constants;
import org.imogene.android.Constants.Keys;
import org.imogene.android.database.sqlite.BinaryCursor;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.BeanKeyGenerator;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.file.MimeTypeManager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public final class Binary extends EntityImpl {

	public static final String TABLE_NAME = "binaries";
	public static final String PACKAGE = "org.imogene.data.Binary";
	public static final Uri CONTENT_URI = FormatHelper.buildUriForFragment(TABLE_NAME);
	
	private static final String TYPE = "BIN";

	private String mParentEntity = null;
	private String mParentKey = null;
	private String mParentFieldGetter = null;
	private String mFileName = null;
	private String mContentType = null;
	private long mLength = 0;
	private Uri mData = null;

	public static final boolean isBinary(Uri uri) {
		return uri != null && Constants.AUTHORITY.equals(uri.getAuthority());
	}
	
	public Binary() {
	}

	@SuppressWarnings("unused")
	private Binary(BinaryCursor c) {
		init(c);
		mParentEntity = c.getParentEntity();
		mParentFieldGetter = c.getParentFieldGetter();
		mParentKey = c.getParentKey();
		mFileName = c.getFileName();
		mLength = c.getLength();
		mData = c.getData();
	}

	public final String getParentEntity() {
		return mParentEntity;
	}

	public final void setParentEntity(String parentEntity) {
		mParentEntity = parentEntity;
	}

	public final String getParentFieldGetter() {
		return mParentFieldGetter;
	}

	public final void setParentFieldGetter(String parentFieldGetter) {
		mParentFieldGetter = parentFieldGetter;
	}

	public final String getParentKey() {
		return mParentKey;
	}

	public final void setParentKey(String parentKey) {
		mParentKey = parentKey;
	}

	public final String getFileName() {
		return mFileName;
	}

	public final void setFileName(String fileName) {
		mFileName = fileName;
	}

	public final String getContentType() {
		return mContentType;
	}

	public final void setContentType(String contentType) {
		mContentType = contentType;
	}

	public final long getLength() {
		return mLength;
	}

	public final void setLength(long length) {
		mLength = length;
	}

	public final Uri getData() {
		return mData;
	}

	public final void setData(Uri uri) {
		mData = uri;
	}

	@Override
	protected final  Uri getContentUri() {
		return CONTENT_URI;
	}

	@Override
	protected final String getBeanType() {
		return TYPE;
	}
	
	@Override
	public Uri commit(Context context, boolean local, boolean temporary) {
		if (getData() == null || getRowId() != -1)
			return null;

		ContentResolver res = context.getContentResolver();

		if (local) {
			String login = PreferenceHelper.getCurrentLogin(context);
			setModifiedBy(login);
			setCreatedBy(login);
			setModifiedFrom(PreferenceHelper.getHardwareId(context));
			setId(BeanKeyGenerator.getNewId("BIN"));
			
			MimeTypeManager mgr = MimeTypeManager.getInstance(context);
			mContentType = res.getType(mData);
			if (mContentType == null) {
				mContentType = mgr.getMimeType(mData.toString());
			}
			String extension = mgr.getExtension(mContentType);
			mFileName = getId() + (extension == null ? ".bin" : extension);
		}

		ContentValues values = new ContentValues();
		values.put(Keys.KEY_ID, getId());
		values.put(Keys.KEY_MODIFIED, getModified());
		values.put(Keys.KEY_MODIFIEDBY, getModifiedBy());
		values.put(Keys.KEY_MODIFIEDFROM, getModifiedFrom());
		values.put(Keys.KEY_UPLOADDATE, getUploadDate());
		values.put(Keys.KEY_CREATED, getCreated());
		values.put(Keys.KEY_CREATEDBY, getCreatedBy());
		values.put(Keys.KEY_CONTENT_TYPE, mContentType);
		values.put(Keys.KEY_FILE_NAME, mFileName);
		values.put(Keys.KEY_LENGTH, mLength);
		values.put(Keys.KEY_PARENT_ENTITY, mParentEntity);
		values.put(Keys.KEY_PARENT_FIELD_GETTER, mParentFieldGetter);
		values.put(Keys.KEY_PARENT_KEY, mParentKey);

		Uri uri;

		Cursor c = res.query(CONTENT_URI, new String[] { Keys.KEY_ROWID },
				Keys.KEY_ID + "='" + getId() + "'", null, null);
		if (c.getCount() == 1) {
			c.moveToFirst();
			setRowId(c.getLong(0));
			c.close();
			uri = ContentUris.withAppendedId(CONTENT_URI, getRowId());
			res.update(uri, values, null, null);
		} else {
			c.close();
			uri = res.insert(CONTENT_URI, values);
			setRowId(ContentUris.parseId(uri));
		}

		try {
			OutputStream os = res.openOutputStream(uri);
			InputStream is = res.openInputStream(mData);
			byte[] b = new byte[4096];
			while (is.read(b) != -1) {
				os.write(b);
			}
			os.flush();
			os.close();
			is.close();

			ParcelFileDescriptor fd = res.openFileDescriptor(uri, "r");
			values.clear();
			values.put(Keys.KEY_LENGTH, fd.getStatSize());
			fd.close();

			if (local) {
				long time = PreferenceHelper.getRealTime(context);
				setCreated(time);
				setModified(time);
				values.put(Keys.KEY_CREATED, time);
				values.put(Keys.KEY_MODIFIED, time);
			}

			res.update(uri, values, null, null);

			return uri;
		} catch (Exception e) {
			if (uri != null)
				res.delete(uri, null, null);
			return null;
		}
	}

}
