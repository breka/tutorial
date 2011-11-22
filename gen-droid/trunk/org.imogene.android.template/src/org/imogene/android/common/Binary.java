package org.imogene.android.common;

import org.imogene.android.Constants;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.BinaryCursor;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.BeanKeyGenerator;
import org.imogene.android.util.content.ContentUrisUtils;
import org.imogene.android.util.file.FileUtils;
import org.imogene.android.util.file.MimeTypeManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

public final class Binary extends EntityImpl {
	
	public static class Columns implements Entity.Columns {
		public static final String TABLE_NAME = "binaries";
		public static final String PACKAGE = "org.imogene.data.Binary";
		public static final String TYPE = "BIN";

		public static final Uri CONTENT_URI = ContentUrisUtils.buildUriForFragment(TABLE_NAME);
		
		public static final String LENGTH = "length";
		public static final String CONTENT_TYPE = "contentType";
		public static final String FILE_NAME = "fileName";
		public static final String DATA = "_data";
		public static final String PARENT_ENTITY = "parentEntity";
		public static final String PARENT_KEY = "parentKey";
		public static final String PARENT_FIELD_GETTER = "parentFieldGetter";
		
	}

	

	private String mParentEntity = null;
	private String mParentKey = null;
	private String mParentFieldGetter = null;
	private String mFileName = null;
	private String mContentType = null;
	private long mLength = 0;
	private Uri mData = null;

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
		return Columns.CONTENT_URI;
	}

	@Override
	protected final String getBeanType() {
		return Columns.TYPE;
	}
	
	@Override
	public void prepareForSave(Context context) {
		prepareForSave(context, Columns.TYPE);
	}
	
	public Uri saveOrUpdate(Context context) {
		return saveOrUpdate(context, Columns.TABLE_NAME);
	}
	
	@Override
	protected void addValues(Context context, ContentValues values) {
		values.put(Columns.CONTENT_TYPE, mContentType);
		values.put(Columns.FILE_NAME, mFileName);
		values.put(Columns.LENGTH, mLength);
		values.put(Columns.PARENT_ENTITY, mParentEntity);
		values.put(Columns.PARENT_FIELD_GETTER, mParentFieldGetter);
		values.put(Columns.PARENT_KEY, mParentKey);
	}
	
	private static boolean isBinary(Uri uri) {
		return uri != null && Constants.AUTHORITY.equals(uri.getAuthority());
	}
	
	public static Uri toBinary(Context context, Uri data, String beanType, String parentFieldGetter, String parentId) {
		if (data == null || isBinary(data)) {
			return data;
		}
		
		String login = PreferenceHelper.getCurrentLogin(context);
		
		String id = BeanKeyGenerator.getNewId(Columns.TYPE);
		
		ContentResolver r = context.getContentResolver();
		
		MimeTypeManager mgr = MimeTypeManager.getInstance(context);
		String contentType = r.getType(data);
		if (contentType == null) {
			contentType = mgr.getMimeType(data.toString());
		}
		
		String extension = mgr.getExtension(contentType);
		String fileName = id + (extension == null ? ".bin" : extension);

		Binary binary = new Binary();
		binary.setId(id);
		binary.setCreated(PreferenceHelper.getRealTime(context));
		binary.setCreatedBy(login);
		binary.setModified(-1);
		binary.setModifiedBy(login);
		binary.setModifiedFrom(PreferenceHelper.getHardwareId(context));
		binary.setSynchronized(false);

		binary.setParentEntity(beanType);
		binary.setParentFieldGetter(parentFieldGetter);
		binary.setParentKey(parentId);
		binary.setContentType(contentType);
		binary.setFileName(fileName);
		
		Uri uri = binary.saveOrUpdate(context);
		
		try {
			FileUtils.appendFile(context.getContentResolver(), data, uri);

			ParcelFileDescriptor fd = r.openFileDescriptor(uri, "r");
			binary.setLength(fd.getStatSize());
			fd.close();

			binary.setModified(0);
			return binary.saveOrUpdate(context);
		} catch (Exception e) {
			if (uri != null)
				r.delete(uri, null, null);
			return null;
		}
	}

}
