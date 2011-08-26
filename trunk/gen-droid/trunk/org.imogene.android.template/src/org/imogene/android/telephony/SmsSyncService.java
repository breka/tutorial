package org.imogene.android.telephony;

import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Keys;
import org.imogene.android.app.WakefulIntentService;
import org.imogene.android.common.SmsComm;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.database.sqlite.SmsCommCursor;
import org.imogene.android.preference.PreferenceHelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class SmsSyncService extends WakefulIntentService {

	public static void startService(Context context) {
		sendWakefulWork(context, new Intent(Intents.ACTION_SMS_SYNC));
	}

	public static void actionReschedule(Context context) {
		sendWakefulWork(context, new Intent(Intents.ACTION_SMS_RESCHEDULE));
	}

	public static void actionCancel(Context context) {
		sendWakefulWork(context, new Intent(Intents.ACTION_SMS_CANCEL));
	}
	
	private static final int PERIOD_MS = 60000;
	private static final String WHERE;
	
	static {
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendEq(Keys.KEY_SMS_STATUS, SmsComm.STATUS_TO_SEND);
		WHERE = builder.toSQL();
	}
	
	public SmsSyncService(String name) {
		super(name);
	}
	
	private void terminate() {
		if (PreferenceHelper.getSynchronizationStatus(this)) {
			reschedule();
		}
	}
	
	private void reschedule() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(Intents.ACTION_SMS_SYNC);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + PERIOD_MS, pi);
	}
	
	private void cancel() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(Intents.ACTION_SMS_SYNC);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmMgr.cancel(pi);
	}

	@Override
	protected void doWakefulWork(Intent intent) {
		if (Intents.ACTION_SMS_SYNC.equals(intent.getAction())) {
			SmsCommCursor c = (SmsCommCursor) SQLiteWrapper.query(this, SmsComm.CONTENT_URI, WHERE, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				// String message = ... - Build message
				// SmsHelper.sendSms(this, message); - Send message
			}
			c.close();
			terminate();
		} else if (Intents.ACTION_SMS_RESCHEDULE.equals(intent.getAction())) {
			reschedule();
		} else if (Intents.ACTION_SMS_CANCEL.equals(intent.getAction())) {
			cancel();
		}
	}
}
