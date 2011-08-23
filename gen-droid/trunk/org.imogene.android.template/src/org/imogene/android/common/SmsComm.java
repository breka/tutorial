package org.imogene.android.common;

import org.imogene.android.Constants.Keys;
import org.imogene.android.database.sqlite.SmsCommCursor;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;
import org.imogene.android.util.FormatHelper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class SmsComm {
	
	public static final int STATUS_TO_SEND = 0;
	public static final int STATUS_INTERNAL_ERROR = 1;
	public static final int STATUS_EXTERNAL_ERROR = 2;
	public static final int STATUS_SENT = 3;
	
	public static final String TABLE_NAME = "smscomm";
	public static final Uri CONTENT_URI = FormatHelper.buildUriForFragment(TABLE_NAME);
	
	private long rowId = -1;
	private Uri entityUri;
	private Long sentDate;
	private String response;
	private int status;
	
	public SmsComm() {
		
	}
	
	public SmsComm(Context context, Uri uri) {
		SmsCommCursor cursor = (SmsCommCursor) AbstractDatabase.getSuper(context).query(uri, null, null);
		cursor.moveToFirst();
		init(cursor);
		cursor.close();
	}
	
	public SmsComm(SmsCommCursor cursor) {
		init(cursor);
	}
	
	private void init(SmsCommCursor cursor) {
		rowId = cursor.getRowId();
		entityUri = cursor.getEntityUri();
		sentDate = cursor.getSentDate();
		response = cursor.getResponse();
		status = cursor.getStatus();
	}
	
	public long getRowId() {
		return rowId;
	}
	
	public void setRowId(long rowId) {
		this.rowId = rowId;
	}
	
	public Uri getEntityUri() {
		return entityUri;
	}
	
	public void setEntityUri(Uri entityUri) {
		this.entityUri = entityUri;
	}
	
	public Long getSentDate() {
		return sentDate;
	}
	
	public void setSentDate(Long sentDate) {
		this.sentDate = sentDate;
	}

	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public Uri commit(Context context) {
		ContentResolver res = context.getContentResolver();
		
		ContentValues values = new ContentValues();
		values.put(Keys.KEY_ENTITY_URI, entityUri.toString());
		values.put(Keys.KEY_SENT_DATE, sentDate);
		values.put(Keys.KEY_RESPONSE, response);
		values.put(Keys.KEY_SMS_STATUS, status);

		Uri uri;
		if (rowId != -1) {
			uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			res.update(uri, values, null, null);
		} else {
			uri = res.insert(CONTENT_URI, values);
			rowId = ContentUris.parseId(uri);
		}
		return uri;
	}

}
