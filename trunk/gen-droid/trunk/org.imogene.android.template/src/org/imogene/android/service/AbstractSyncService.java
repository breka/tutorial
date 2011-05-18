package org.imogene.android.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.imogene.android.W;
import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Levels;
import org.imogene.android.Constants.Paths;
import org.imogene.android.Constants.Status;
import org.imogene.android.Constants.Tables;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;
import org.imogene.android.sync.OptimizedSyncClient;
import org.imogene.android.sync.SynchronizationException;
import org.imogene.android.sync.http.OptimizedSyncClientHttp;
import org.imogene.android.util.WakeLockSingleton;
import org.imogene.android.util.ntp.SntpException;
import org.imogene.android.util.ntp.SntpProvider;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public abstract class AbstractSyncService extends Service {
	
	public static final void actionReschedule(Context context) {
		WakeLockSingleton.getInstance(context).acquire();
		Intent i = new Intent(Intents.ACTION_RESCHEDULE);
		context.startService(i);
	}

	public static final void actionCancel(Context context) {
		WakeLockSingleton.getInstance(context).acquire();
		Intent i = new Intent(Intents.ACTION_CANCEL);
		context.startService(i);
	}

	public static final void startService(Context context) {
		WakeLockSingleton.getInstance(context).acquire();
		Intent i = new Intent(Intents.ACTION_CHECK_SYNC);
		context.startService(i);
	}

	private static final String TAG = AbstractSyncService.class.getName();

	private static final int SYNC_STATE_ID = 1;
	private static final int SYNC_RESUME_ID = 2;

	private NotificationManager mNotificationManager;

	private int mStartId = -1;

	private Thread syncThread = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		mStartId = startId;

		if (syncThread == null || !syncThread.isAlive()) {
			syncThread = new Thread(newSynchronizer(this, intent.getAction()));
			syncThread.setPriority(Thread.NORM_PRIORITY - 1);
			syncThread.start();
		}
	}

	private void onCancel() {
		Log.i(TAG, "*** OptimizedSynchronizationService: cancel");
		cancel();
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}

	private void onReschedule() {
		Log.i(TAG, "*** OptimizedSynchronizationService: reschedule");
		reschedule();
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}

	private void onFinish(int received) {
		mNotificationManager.cancel(SYNC_STATE_ID);

		if (received > 0)
			notifySynchronizationResume(received);

		if (PreferenceHelper.getSynchronizationStatus(this)) {
			reschedule();
		}
		stopSelf(mStartId);
		WakeLockSingleton.getInstance(this).release();
	}

	private void onStart() {
		Log.i(TAG, "Starting");
		notifyState(getString(W.string.sync_start));
	}

	private void onInit() {
		Log.i(TAG, "Initializing");
		notifyState(getString(W.string.sync_init));
	}

	private void onInitResume() {
		Log.i(TAG, "Initializing a resumed session");
		notifyState(getString(W.string.sync_init_resume));
	}

	private void onSend() {
		Log.i(TAG, "Sending");
		notifyState(getString(W.string.sync_send));
	}

	private void onSendResume() {
		Log.i(TAG, "Sending from a resumed session");
		notifyState(getString(W.string.sync_send_resume));
	}

	private void onReceive() {
		Log.i(TAG, "Receiving");
		notifyState(getString(W.string.sync_receive));
	}

	private void onReceiveResume() {
		Log.i(TAG, "Receiving from a resumed session");
		notifyState(getString(W.string.sync_receive_resume));
	}

	private void onClose() {
		Log.i(TAG, "Closing");
		notifyState(getString(W.string.sync_close));
	}

	private void onCloseResume() {
		Log.i(TAG, "Closing a resumed session");
		notifyState(getString(W.string.sync_close_resume));
	}

	private void onUpdateOffset(long offset) {
		SntpProvider.updateTimeOffset(this, offset);
	}

	private void cancel() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(Intents.ACTION_CHECK_SYNC);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		alarmMgr.cancel(pi);
	}

	private void reschedule() {
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(Intents.ACTION_CHECK_SYNC);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

		long period = PreferenceHelper.getSynchronizationPeriod(this);

		if (period < 15) {
			period = 15;
		}

		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock
				.elapsedRealtime()
				+ period * 1000, pi);
	}

	protected abstract class AbstractSynchronizer implements Runnable {

		protected final Context mContext;
		protected final String hardwareId;
		private final AbstractDatabase helper;
		private final String login;
		private final String password;
		private final String serverUrl;
		private final boolean bidirectional;
		private final boolean debug;

		private final OptimizedSyncClient syncClient;

		private final String mAction;

		public AbstractSynchronizer(Context context, String action) {
			mContext = context;

			helper = AbstractDatabase.getSuper(context);

			mAction = action;
			hardwareId = PreferenceHelper.getHardwareId(context);
			serverUrl = PreferenceHelper.getServerUrl(context);
			login = PreferenceHelper.getSyncLogin(context);
			password = PreferenceHelper.getSyncPassword(context);
			bidirectional = PreferenceHelper
					.getSynchronizationBidirectional(context);
			debug = PreferenceHelper.isDebugActive(context);

			if(PreferenceHelper.isHttpAuthenticated(context)){
				syncClient = new OptimizedSyncClientHttp(serverUrl, login, password);
			}else{
				syncClient = new OptimizedSyncClientHttp(serverUrl);
			}
		}

		public void run() {
			if (Intents.ACTION_CHECK_SYNC.equals(mAction)) {
				int received = 0;
				try {
					reportStart();

					File directory = new File(Paths.PATH_SYNCHRO);
					directory.mkdirs();

					updateTimeOffset();

					// look for synchronization ERROR.
					Cursor cursor = helper.getReadableDatabase().query(
							Tables.TABLE_SYNCHISTORY,
							new String[] { Keys.KEY_ROWID, Keys.KEY_ID, Keys.KEY_LEVEL },
							Keys.KEY_STATUS + " = " + Status.STATUS_ERROR, null, null,
							null, null);
					/*
					 * We resume the synchronization process
					 */
					if (cursor.getCount() > 0) {
						cursor.moveToFirst();
						long rowId = cursor.getLong(0);
						UUID id = UUID.fromString(cursor.getString(1));
						int level = cursor.getInt(2);
						Log.i(TAG, "resume on error : " + id + ", level : "
								+ level);
						cursor.close();
						received += resumeOnError(rowId, id, level);
					} else {
						cursor.close();
					}

					// 1 - initialize the session
					reportInit();
					UUID sessionId = UUID.fromString(syncClient.initSession(login, password, hardwareId, "xml"));
					// 2 - send client modification
					reportSend();

					long syncTime = PreferenceHelper.getRealTime(mContext);

					File outFile = new File(Paths.PATH_SYNCHRO, sessionId + ".lmodif");
					FileOutputStream fos = new FileOutputStream(outFile);
					getDataToSynchronize(fos);
					fos.close();

					ContentValues values = new ContentValues();
					values.put(Keys.KEY_ID, sessionId.toString());
					values.put(Keys.KEY_DATE, syncTime);
					values.put(Keys.KEY_STATUS, Status.STATUS_ERROR);
					values.put(Keys.KEY_LEVEL, Levels.LEVEL_SEND);
					long rowId = helper.getWritableDatabase().insert(
							Tables.TABLE_SYNCHISTORY, "", values);

					FileInputStream fis = new FileInputStream(outFile);
					int res = syncClient.sendClientModification(sessionId, fis);
					fis.close();
					if (!debug) {
						outFile.delete();
					}
					
					markAsSentForSession(syncTime);

					Log
							.i(TAG, "number of server modifications applied: "
									+ res);

					if (res > -1) {
						values.clear();
						values.put(Keys.KEY_LEVEL, Levels.LEVEL_RECEIVE);
						helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
								values, Keys.KEY_ROWID + "=" + rowId, null);
					} else {
						throw new SynchronizationException(
								"Error sending data to the server.",
								SynchronizationException.ERROR_SEND);
					}

					// 3 - get server modifications
					reportReceive();
					received += requestServerModification(sessionId);

					values.clear();
					values.put(Keys.KEY_STATUS, Status.STATUS_OK);
					values.put(Keys.KEY_LEVEL, Levels.LEVEL_RECEIVE);
					helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
							values, Keys.KEY_ROWID + "=" + rowId, null);

					// 4 - close the session
					reportClose();
					syncClient.closeSession(sessionId, debug);
				} catch (FileNotFoundException e) {
					MetadataService.startMetadataService(mContext, e,
							PreferenceHelper.getRealTime(mContext));
				} catch (IOException e) {
					MetadataService.startMetadataService(mContext, e,
							PreferenceHelper.getRealTime(mContext));
				} catch (SynchronizationException e) {
					Log.e(TAG, "error during synchronization", e);
				} catch (Exception e) {
					Log.e(TAG, "error during synchronization", e);
				} finally {
					markAsReadHidden();
					reportFinish(received);
				}
			} else if (Intents.ACTION_CANCEL.equals(mAction)) {
				reportCancel();
			} else if (Intents.ACTION_RESCHEDULE.equals(mAction)) {
				reportReschedule();
			}
		}

		private void reportCancel() {
			mHandler.sendEmptyMessage(MSG_CANCEL);
		}

		private void reportReschedule() {
			mHandler.sendEmptyMessage(MSG_RESCHEDULE);
		}

		private void reportFinish(int received) {
			mHandler
					.sendMessage(Message.obtain(mHandler, MSG_FINISH, received));
		}

		private void reportStart() {
			mHandler.sendEmptyMessage(MSG_START);
		}

		private void reportInit() {
			mHandler.sendEmptyMessage(MSG_INIT);
		}

		private void reportInitResume() {
			mHandler.sendEmptyMessage(MSG_INIT_RESUME);
		}

		private void reportSend() {
			mHandler.sendEmptyMessage(MSG_SEND);
		}

		private void reportSendResume() {
			mHandler.sendEmptyMessage(MSG_SEND_RESUME);
		}

		private void reportReceive() {
			mHandler.sendEmptyMessage(MSG_RECEIVE);
		}

		private void reportReceiveResume() {
			mHandler.sendEmptyMessage(MSG_RECEIVE_RESUME);
		}

		private void reportClose() {
			mHandler.sendEmptyMessage(MSG_CLOSE);
		}

		private void reportCloseResume() {
			mHandler.sendEmptyMessage(MSG_CLOSE_RESUME);
		}

		private void reportUpdateTimeOffset(long offset) {
			mHandler.sendMessage(Message.obtain(mHandler, MSG_UPDATE_OFFSET,
					offset));
		}

		private void updateTimeOffset() {
			try {
				long offset = SntpProvider.getTimeOffsetFromNtp(mContext);
				reportUpdateTimeOffset(offset);
			} catch (SntpException e) {
				Log.e(TAG, "update offset from ntp ->", e);
			}
		}

		private int resumeOnError(long rowId, UUID errorId, int errorLevel)
				throws SynchronizationException {
			ContentValues values = new ContentValues();

			/*
			 * we resume a sent, by re-sending local data an retrieving all the
			 * data from the server
			 */
			int received = 0;
			if (errorLevel == Levels.LEVEL_SEND) {
				Log.i(TAG, "Resuming the sent for the session " + errorId);
				try {
					/* 1 - initialize the resumed session */
					reportInitResume();
					String result = syncClient.resumeSend(login, password, hardwareId, "xml", errorId);
					/* 2 - sending local modifications */
					reportSendResume();
					if (result.equals("error")) {
						throw new SynchronizationException(
								"Error resuming the session, the server return an error code",
								SynchronizationException.ERROR_SEND);
					} else {
						int bytesReceived = Integer.parseInt(result);
						File outFile = new File(Paths.PATH_SYNCHRO, errorId
								+ ".lmodif");
						FileInputStream fis = new FileInputStream(outFile);
						fis.skip(bytesReceived);
						Log.i(TAG, "Re-sending data from the file "
								+ outFile.getAbsolutePath() + " skipping "
								+ bytesReceived + " bytes");
						int res = syncClient.resumeSendModification(errorId,
								fis);
						fis.close();
						if (!debug) {
							outFile.delete();
						}

						Cursor c = helper.getReadableDatabase()
								.query(Tables.TABLE_SYNCHISTORY,
										new String[] { Keys.KEY_DATE },
										Keys.KEY_ROWID + "=" + rowId, null, null,
										null, null);
						if (c.getCount() > 0) {
							c.moveToFirst();
							markAsSentForSession(c.getLong(0));
						}
						c.close();

						Log.i(TAG,
								"number of server modifications applied on resume: "
										+ res);

					}
					values.clear();
					values.put(Keys.KEY_LEVEL, Levels.LEVEL_RECEIVE);
					helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
							values, Keys.KEY_ROWID + "=" + rowId, null);

					/* 3 - receiving the server modifications */
					reportReceiveResume();
					received = requestServerModification(errorId);

					values.clear();
					values.put(Keys.KEY_STATUS, Status.STATUS_OK);
					helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
							values, Keys.KEY_ROWID + "=" + rowId, null);

					/* 4 - closing the session */
					reportCloseResume();
					syncClient.closeSession(errorId, debug);
				} catch (Exception ex) {
					SynchronizationException syx = new SynchronizationException(
							"Error resuming a sent", ex,
							SynchronizationException.DEFAULT_ERROR);
					if (ex instanceof SynchronizationException)
						syx.setCode(((SynchronizationException) ex).getCode());
					throw syx;
				}
			}
			/*
			 * we resume a reception, by re-receiving the server data
			 */
			if (errorLevel == Levels.LEVEL_RECEIVE) {
				Log.i(TAG, "Resuming the receive operation for the session "
						+ errorId);
				try {
					/* clear the sent file */
					if (!debug) {
						File tmp = new File(Paths.PATH_SYNCHRO, errorId + ".lmodif");
						if (tmp.exists())
							tmp.delete();
					}
					/* 1 - initialize the resumed session */
					reportInitResume();
					File inFile = new File(Paths.PATH_SYNCHRO, errorId + ".smodif");
					String result = syncClient.resumeReceive(login, password, hardwareId, "xml", errorId, inFile.length());
					/* 2 - receiving data */
					reportReceiveResume();
					if (!result.equals("error")) {
						received = resumeRequestModification(errorId);
						values.clear();
						values.put(Keys.KEY_STATUS, Status.STATUS_OK);
						helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
								values, Keys.KEY_ROWID + "=" + rowId, null);
						/* 3 - closing the session */
						reportCloseResume();
						syncClient.closeSession(errorId, debug);
					} else {
						throw new SynchronizationException(
								"The server return an error code",
								SynchronizationException.ERROR_RECEIVE);
					}
				} catch (Exception ex) {
					SynchronizationException syx = new SynchronizationException(
							"Error resuming a receive operation", ex,
							SynchronizationException.ERROR_RECEIVE);
					if (ex instanceof SynchronizationException)
						syx.setCode(((SynchronizationException) ex).getCode());
					throw syx;
				}
			}
			return received;
		}

		/**
		 * Request the server modification in normal mode
		 * 
		 * @param sessionId
		 *            the session Id
		 * @throws SynchronizationException
		 * @throws IOException
		 * @throws XmlPullParserException
		 */
		private int requestServerModification(UUID sessionId)
				throws SynchronizationException, IOException,
				XmlPullParserException {
			if (!bidirectional)
				return 0;
			File inFile = new File(Paths.PATH_SYNCHRO, sessionId + ".smodif");
			FileOutputStream sFos = new FileOutputStream(inFile);
			syncClient.requestServerModifications(sessionId, sFos);
			sFos.close();

			FileInputStream sFis = new FileInputStream(inFile);
			int result = applyIncomingModifications(sFis);
			sFis.close();
			if (!debug) {
				inFile.delete();
			}
			return result;
		}

		private int resumeRequestModification(UUID errorId)
				throws SynchronizationException, IOException,
				XmlPullParserException {
			if (!bidirectional)
				return 0;
			File inputFile = new File(Paths.PATH_SYNCHRO, errorId + ".smodif");
			FileOutputStream fos = new FileOutputStream(inputFile, true);
			syncClient.resumeRequestModification(errorId, fos, inputFile
					.length());
			fos.close();

			FileInputStream fis = new FileInputStream(inputFile);
			int result = applyIncomingModifications(fis);
			fis.close();
			if (!debug) {
				inputFile.delete();
			}
			return result;
		}

		protected abstract int getDataToSynchronize(FileOutputStream fos)
				throws IllegalArgumentException, IllegalStateException,
				IOException;

		protected abstract int applyIncomingModifications(InputStream is)
				throws XmlPullParserException, IOException;

		protected abstract void markAsSentForSession(long time);
		
		protected abstract void markAsReadHidden();
	}


	protected abstract AbstractSynchronizer newSynchronizer(Context context,
			String action);

	protected abstract Intent getResumeIntent(int received);

	private static final int MSG_RESCHEDULE = 0;
	private static final int MSG_CANCEL = 1;
	private static final int MSG_FINISH = 2;
	private static final int MSG_START = 3;
	private static final int MSG_INIT = 5;
	private static final int MSG_INIT_RESUME = 6;
	private static final int MSG_SEND = 7;
	private static final int MSG_SEND_RESUME = 8;
	private static final int MSG_RECEIVE = 9;
	private static final int MSG_RECEIVE_RESUME = 10;
	private static final int MSG_CLOSE = 11;
	private static final int MSG_CLOSE_RESUME = 12;
	private static final int MSG_UPDATE_OFFSET = 13;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESCHEDULE:
				onReschedule();
				break;
			case MSG_CANCEL:
				onCancel();
				break;
			case MSG_FINISH:
				onFinish((Integer) msg.obj);
				break;
			case MSG_START:
				onStart();
				break;
			case MSG_INIT:
				onInit();
				break;
			case MSG_INIT_RESUME:
				onInitResume();
				break;
			case MSG_SEND:
				onSend();
				break;
			case MSG_SEND_RESUME:
				onSendResume();
				break;
			case MSG_RECEIVE:
				onReceive();
				break;
			case MSG_RECEIVE_RESUME:
				onReceiveResume();
				break;
			case MSG_CLOSE:
				onClose();
				break;
			case MSG_CLOSE_RESUME:
				onCloseResume();
				break;
			case MSG_UPDATE_OFFSET:
				onUpdateOffset((Long) msg.obj);
				break;
			default:
				throw new IllegalArgumentException("Unknown message id "
						+ msg.what);
			}
		}
	};

	private void notifyState(String msg) {
		NotificationManager notifMgr = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notif = new Notification(W.drawable.logo_android_s, msg, System
				.currentTimeMillis());
		notif.flags = Notification.FLAG_NO_CLEAR;

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null,
				0);
		notif.setLatestEventInfo(this, getString(W.string.synchronization),
				msg, contentIntent);

		notifMgr.notify(SYNC_STATE_ID, notif);
	}

	private void notifySynchronizationResume(int received) {
		CharSequence chr = getString(W.string.synchro_resume_title);
		Notification notif = new Notification(W.drawable.logo_android_s, chr, System
				.currentTimeMillis());

		Intent intent = getResumeIntent(received);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		CharSequence chrLong = getString(W.string.synchro_resume_detail,
				received);
		notif.setLatestEventInfo(this, chr, chrLong, contentIntent);

		notif.flags = Notification.FLAG_AUTO_CANCEL
				| Notification.FLAG_SHOW_LIGHTS;
		notif.defaults = Notification.DEFAULT_ALL;
		notif.ledARGB = 0xff00ff00;
		notif.ledOnMS = 500;
		notif.ledOffMS = 2000;

		mNotificationManager.notify(SYNC_RESUME_ID, notif);
	}
}
