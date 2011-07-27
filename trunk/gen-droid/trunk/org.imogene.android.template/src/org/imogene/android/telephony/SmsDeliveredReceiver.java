package org.imogene.android.telephony;

import org.imogene.android.common.SmsComm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class SmsDeliveredReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		switch (getResultCode()) {
		case Activity.RESULT_OK:
			SmsComm sms = new SmsComm(context, intent.getData());
			sms.setDeliveredDate(System.currentTimeMillis());
			sms.commit(context);
			Toast.makeText(context, "SMS delivered", Toast.LENGTH_SHORT).show();
			break;
		case Activity.RESULT_CANCELED:
			Toast.makeText(context, "SMS not delivered", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
