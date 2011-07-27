package org.imogene.android.telephony;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.SmsComm;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;
import org.imogene.android.util.base26.Base26;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

public class SmsResponseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SmsMessage[] messages = SmsHelper.getMessagesFromIntent(intent);
		for (SmsMessage message : messages) {
			if (PhoneNumberUtils.compare(message.getOriginatingAddress(), PreferenceHelper.getSmsServerPhone(context))) {
				String body = message.getMessageBody();
				String[] bodyArray = body.split(" ");
				if (bodyArray.length >= 2) {
					long rowId = Base26.decode(bodyArray[1]);
					SmsComm sms = new SmsComm(context, ContentUris.withAppendedId(SmsComm.CONTENT_URI, rowId));
					sms.setResponseDate(System.currentTimeMillis());
					sms.setAck("OK".equals(bodyArray[0]));
					if (bodyArray.length > 2) {
						sms.setResponse(bodyArray[2]);
					}
					sms.commit(context);

					if (sms.isAck()) {
						Uri uri = AbstractDatabase.getSuper(context).findInDatabase(sms.getEntityId());
						if (uri != null) {
							ContentValues values = new ContentValues();
							values.put(Keys.KEY_SYNCHRONIZED, 1);
							context.getContentResolver().update(uri, values, null, null);
						}
					}

				}
			}
		}
	}

}
