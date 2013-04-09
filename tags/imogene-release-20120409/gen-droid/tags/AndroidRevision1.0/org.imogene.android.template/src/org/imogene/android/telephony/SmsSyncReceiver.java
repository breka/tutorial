package org.imogene.android.telephony;

import org.imogene.android.Constants.Intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsSyncReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intents.ACTION_SMS_SYNC.equals(intent.getAction())) {
			SmsSyncService.startService(context);
		} else if (Intents.ACTION_SMS_RESCHEDULE.equals(intent.getAction())) {
			SmsSyncService.actionReschedule(context);
		} else if (Intents.ACTION_SMS_CANCEL.equals(intent.getAction())) {
			SmsSyncService.actionCancel(context);
		}
	}

}
