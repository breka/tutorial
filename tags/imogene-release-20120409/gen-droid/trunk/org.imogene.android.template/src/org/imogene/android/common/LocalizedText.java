package org.imogene.android.common;

import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.database.sqlite.LocalizedTextCursor;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.util.content.ContentUrisUtils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

public class LocalizedText extends EntityImpl {
	

	public static class Columns implements Entity.Columns {
		public static final String PACKAGE = "LocalizedText";
		public static final String TABLE_NAME = "localizedtext";
		public static final String TYPE = "LTXT";
		public static final Uri CONTENT_URI = ContentUrisUtils.buildUriForFragment(TABLE_NAME);

		public static final String FIELD_ID = "fieldId";
		public static final String LOCALE = "locale";
		public static final String VALUE = "value";
		public static final String ORIGINAL_VALUE = "originalValue";
		public static final String POTENTIALY_WRONG = "potentialyWrong";
	}


	private String mFieldId = null;
	private String mLocale = null;
	private String mValue = null;
	private boolean mOriginalValue = false;
	private boolean mPotentialyWrong = false;

	public LocalizedText(Context context, Uri uri) {
		LocalizedTextCursor cursor = (LocalizedTextCursor) SQLiteWrapper.query(context, uri, null, null);
		init(cursor);
		cursor.close();
	}

	public LocalizedText(LocalizedTextCursor cursor) {
		init(cursor);
	}
	
	private void init(LocalizedTextCursor cursor) {
		super.init(cursor);
		mFieldId = cursor.getFieldId();
		mLocale = cursor.getLocale();
		mValue = cursor.getValue();
		mOriginalValue = cursor.getOriginalValue();
		mPotentialyWrong = cursor.getPotentialyWrong();
	}

	public LocalizedText(Bundle bundle) {
	}

	public LocalizedText() {
	}

	public final String getFieldId() {
		return mFieldId;
	}
	public final String getLocale() {
		return mLocale;
	}
	public final String getValue() {
		return mValue;
	}
	public final boolean getOriginalValue() {
		return mOriginalValue;
	}
	public final boolean getPotentialyWrong() {
		return mPotentialyWrong;
	}
	
	public final void setFieldId(String fieldId) {
		mFieldId = fieldId;
	}
	public final void setLocale(String locale) {
		mLocale = locale;
	}
	public final void setValue(String value) {
		mValue = value;
	}
	public final void setOriginalValue(boolean originalValue) {
		mOriginalValue = originalValue;
	}
	public final void setPotentialyWrong(boolean potentialyWrong) {
		mPotentialyWrong = potentialyWrong;
	}

	@Override
	protected final Uri getContentUri() {
		return Columns.CONTENT_URI;
	}

	@Override
	protected final String getBeanType() {
		return Columns.TYPE;
	}

	@Override
	protected final void addValues(Context context, ContentValues values) {
		values.put(Columns.FIELD_ID, mFieldId);
		values.put(Columns.LOCALE, mLocale);
		values.put(Columns.VALUE, mValue);
		values.put(Columns.ORIGINAL_VALUE, mOriginalValue ? 1 : 0);
		values.put(Columns.POTENTIALY_WRONG, mPotentialyWrong ? 1 : 0);
	}

	public final void reset() {
		mFieldId = null;
		mLocale = null;
		mValue = null;
		mOriginalValue = false;
		mPotentialyWrong = false;
	}
	
	@Override
	public void prepareForSave(Context context) {
		prepareForSave(context, Columns.TYPE);
	}
	
	@Override
	public Uri saveOrUpdate(Context context) {
		return saveOrUpdate(context, Columns.TABLE_NAME);
	}

}
