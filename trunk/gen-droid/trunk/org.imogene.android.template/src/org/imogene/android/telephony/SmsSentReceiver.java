package org.imogene.android.telephony;

import org.imogene.android.common.SmsComm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SmsSentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			SmsComm sms = new SmsComm(context, intent.getData());
			sms.setSentDate(System.currentTimeMillis());
			sms.commit(context);
			Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			Toast.makeText(context, "Null PDU", Toast.LENGTH_SHORT).show();
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
