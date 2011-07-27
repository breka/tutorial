package org.imogene.android.telephony;

import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Keys;
import org.imogene.android.common.SmsComm;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SmsCommCursor;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;
import org.imogene.android.util.WakeLockSingleton;
import org.imogene.android.util.base26.Base26;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.SmsManager;

public class SmsSyncService extends Service implements Runnable {

	public static void startService(Context context) {
		WakeLockSingleton.getInstance(context).acquire();
		context.startService(new Intent(Intents.ACTION_SMS_SYNC));
	}

	public static void actionReschedule(Context context) {
		WakeLockSingleton.getInstance(context).acquire();
		context.startService(new Intent(Intents.ACTION_SMS_RESCHEDULE));
	}

	public static void actionCancel(Context context) {
		WakeLockSingleton.getInstance(context).acquire();
		context.startService(new Intent(Intents.ACTION_SMS_CANCEL));
	}
	
	private static final String WHERE;
	
	static {
		SQLiteBuilder builder = new SQLiteBuilder();
		builder.appendEq(Keys.KEY_ACK, 0);
		builder.appendWhere(new SQLiteBuilder()
			.setOr(true)
			.appendIsNull(Keys.KEY_SENT_DATE)
			.appendIsNotNull(Keys.KEY_DELIVERED_DATE)
			.toSQL());
		WHERE = builder.toSQL();
	}
	
	private String mAction;
	private Thread mThread;
	private int mStartId = -1;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		mAction = intent.getAction();
		mStartId = startId;
		
		if (mThread == null || !mThread.isAlive()) {
			mThread = new Thread(this);
			mThread.setPriority(Thread.NORM_PRIORITY - 1);
			mThread.start();
		}
		
	}
	
	private void onStart() {
		
	}
	
	private void onFinish() {
		if (PreferenceHelper.getSynchronizationStatus(this)) {
			reschedule();
		}
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}
	
	private void onReschedule() {
		reschedule();
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}
	
	private void onCancel() {
		cancel();
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}
	
	private void reschedule() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(Intents.ACTION_SMS_SYNC);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

		long period = PreferenceHelper.getSynchronizationPeriod(this);

		if (period < 15) {
			period = 15;
		}

		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + period * 1000, pi);
	}
	
	private void cancel() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(Intents.ACTION_SMS_SYNC);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		alarmMgr.cancel(pi);
	}

	@Override
	public void run() {
		if (Intents.ACTION_SMS_SYNC.equals(mAction)) {
			reportStart();
			SmsCommCursor c = (SmsCommCursor) AbstractDatabase.getSuper(this).query(SmsComm.CONTENT_URI, WHERE, null);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Uri uri = ContentUris.withAppendedId(SmsComm.CONTENT_URI, c.getRowId());
				PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(Intents.ACTION_SMS_SENT).setData(uri), 0);
				PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(Intents.ACTION_SMS_DELIVERED).setData(uri), 0);

				String message = c.getMessage().replace("%SMS_ID%", Base26.encode(c.getRowId()));
				SmsManager.getDefault().sendTextMessage(c.getDestination(), null, message, sentPI, deliveredPI);
			}
			reportFinish();
		} else if (Intents.ACTION_SMS_RESCHEDULE.equals(mAction)) {
			reportReschedule();
		} else if (Intents.ACTION_SMS_CANCEL.equals(mAction)) {
			reportCancel();
		}
	}
	
	private void reportStart() {
		mHandler.sendEmptyMessage(MSG_START);
	}
	
	
	private void reportFinish() {
		mHandler.sendEmptyMessage(MSG_FINISH);
	}
	
	private void reportReschedule() {
		mHandler.sendEmptyMessage(MSG_RESCHEDULE);
	}
	
	private void reportCancel() {
		mHandler.sendEmptyMessage(MSG_CANCEL);
	}
	
	private static final int MSG_START = 0;
	private static final int MSG_FINISH = 1;
	private static final int MSG_RESCHEDULE = 2;
	private static final int MSG_CANCEL = 3;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_START:
				onStart();
				break;
			case MSG_FINISH:
				onFinish();
				break;
			case MSG_RESCHEDULE:
				onReschedule();
				break;
			case MSG_CANCEL:
				onCancel();
				break;
			}
		};
	};
}
