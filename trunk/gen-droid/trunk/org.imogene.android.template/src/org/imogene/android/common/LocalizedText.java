package org.imogene.android.common;

import org.imogene.android.database.sqlite.LocalizedTextCursor;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;
import org.imogene.android.util.FormatHelper;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

public class LocalizedText extends EntityImpl {

	public static final String PACKAGE = "org.imogene.translatable.entity.LocalizedText";
	public static final String TABLE_NAME = "localizedtext";
	public static final Uri CONTENT_URI = FormatHelper.buildUriForFragment(TABLE_NAME);

	public static final String TYPE = "TRSN";

	private String mFieldId = null;
	private String mLocale = null;
	private String mValue = null;

	public LocalizedText(Context context, Uri uri) {
		LocalizedTextCursor cursor = (LocalizedTextCursor) AbstractDatabase.getSuper(context).query(uri, null, null);
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

	public final void setFieldId(String fieldId) {
		mFieldId = fieldId;
	}
	public final void setLocale(String locale) {
		mLocale = locale;
	}
	public final void setValue(String value) {
		mValue = value;
	}

	@Override
	protected final Uri getContentUri() {
		return CONTENT_URI;
	}

	@Override
	protected final String getBeanType() {
		return TYPE;
	}

	@Override
	protected final void addValues(Context context, ContentValues values) {
		super.addValues(context, values);
		values.put("fieldId", mFieldId);
		values.put("locale", mLocale);
		values.put("value", mValue);
	}

	@Override
	protected final void postCommit(Context context) {
	}

	public final void reset() {
		mFieldId = null;
		mLocale = null;
		mValue = null;
	}

}
