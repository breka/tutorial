package org.imogene.android.telephony;

import org.imogene.android.preference.PreferenceHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

public class SmsResponseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SmsMessage[] messages = SmsHelper.getMessagesFromIntent(intent);
		for (SmsMessage message : messages) {
			if (PhoneNumberUtils.compare(message.getOriginatingAddress(), PreferenceHelper.getSmsServerPhone(context))) {
				//if (.doSomethingWithMessage(context, message.getMessageBody())) {
				//	abortBroadcast();
				//}
			}
		}
	}

}
