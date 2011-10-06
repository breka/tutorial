package org.imogene.android.telephony;

import org.imogene.android.common.SmsComm;
import org.imogene.android.preference.PreferenceHelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsHelper {
	
	/**
	 * Read the PDUs out of an {@link #SMS_RECEIVED_ACTION} or a
	 * {@link #DATA_SMS_RECEIVED_ACTION} intent.
	 * 
	 * @param intent the intent to read from
	 * @return an array of SmsMessages for the PDUs
	 */
	public static final SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}
	
	public static final void sendSms(Context context, String message) {
		SmsManager.getDefault().sendTextMessage(PreferenceHelper.getSmsServerPhone(context), null, message, null, null);
	}
	
	public static final void registerSms4Sending(Context context, Uri uri) {
		SmsComm sms = new SmsComm();
		sms.setEntityUri(uri);
		sms.setStatus(SmsComm.STATUS_TO_SEND);
		sms.commit(context);
		SmsSyncService.startService(context);
	}
	
	public static final void deleteSMSFromInbox(Context context, SmsMessage mesg) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("address='" + mesg.getOriginatingAddress() + "' AND ");
			sb.append("body='" + mesg.getMessageBody() + "'");
			// sb.append("time='" + mesg.getTimestamp() + "'"); //doesn't seem
			// to be supported
			Cursor c = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, sb.toString(), null, null);
			c.moveToFirst();
			// String id = c.getString(0);
			int thread_id = c.getInt(1);
			context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id), null, null);
			c.close();
		} catch (Exception ex) {
			// deletions don't work most of the time since the timing of the
			// receipt and saving to the inbox
			// makes it difficult to match up perfectly. the SMS might not be in
			// the inbox yet when this receiver triggers!
			Log.d("SmsReceiver", "Error deleting sms from inbox: " + ex.getMessage());
		}
	}

}
