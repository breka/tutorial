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
	
	public static final String TABLE_NAME = "smscomm";
	public static final Uri CONTENT_URI = FormatHelper.buildUriForFragment(TABLE_NAME);
	
	private long rowId = -1;
	private String destination;
	private String message;
	private String entityId;
	private Long sentDate;
	private Long deliveredDate;
	private Long responseDate;
	private String response;
	private boolean isAck;
	
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
		entityId = cursor.getEntityId();
		destination = cursor.getDestination();
		message = cursor.getMessage();
		sentDate = cursor.getSentDate();
		deliveredDate = cursor.getDeliveredDate();
		responseDate = cursor.getResponseDate();
		response = cursor.getResponse();
		isAck = cursor.isAck();
	}
	
	public long getRowId() {
		return rowId;
	}
	
	public void setRowId(long rowId) {
		this.rowId = rowId;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Long getSentDate() {
		return sentDate;
	}
	
	public void setSentDate(Long sentDate) {
		this.sentDate = sentDate;
	}

	public Long getDeliveredDate() {
		return deliveredDate;
	}
	
	public void setDeliveredDate(Long deliveredDate) {
		this.deliveredDate = deliveredDate;
	}
	
	public Long getResponseDate() {
		return responseDate;
	}
	
	public void setResponseDate(Long responseDate) {
		this.responseDate = responseDate;
	}
	
	public String getResponse() {
		return response;
	}
	
	public void setResponse(String response) {
		this.response = response;
	}
	
	public boolean isAck() {
		return isAck;
	}
	
	public void setAck(boolean isAck) {
		this.isAck = isAck;
	}
	
	public Uri commit(Context context) {
		ContentResolver res = context.getContentResolver();
		
		ContentValues values = new ContentValues();
		values.put(Keys.KEY_ENTITY_ID, entityId);
		values.put(Keys.KEY_DESTINATION, destination);
		values.put(Keys.KEY_MESSAGE, message);
		values.put(Keys.KEY_SENT_DATE, sentDate);
		values.put(Keys.KEY_DELIVERED_DATE, deliveredDate);
		values.put(Keys.KEY_RESPONSE_DATE, responseDate);
		values.put(Keys.KEY_RESPONSE, response);
		values.put(Keys.KEY_ACK, isAck ? 1 : 0);

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
