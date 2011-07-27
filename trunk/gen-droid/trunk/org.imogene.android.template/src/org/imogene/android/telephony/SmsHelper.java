package org.imogene.android.telephony;

import android.content.Intent;
import android.telephony.SmsMessage;

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

}
