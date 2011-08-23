package org.imogene.android.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Levels;
import org.imogene.android.Constants.Paths;
import org.imogene.android.Constants.Status;
import org.imogene.android.Constants.Tables;
import org.imogene.android.W;
import org.imogene.android.app.WakefulIntentService;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;
import org.imogene.android.sync.OptimizedSyncClient;
import org.imogene.android.sync.SynchronizationException;
import org.imogene.android.sync.http.OptimizedSyncClientHttp;
import org.imogene.android.util.ntp.SntpException;
import org.imogene.android.util.ntp.SntpProvider;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.util.Log;

public abstract class AbstractSyncService extends WakefulIntentService {

	public static final void actionReschedule(Context context) {
		sendWakefulWork(context, new Intent(Intents.ACTION_RESCHEDULE));
	}

	public static final void actionCancel(Context context) {
		sendWakefulWork(context, new Intent(Intents.ACTION_CANCEL));
	}

	public static final void startService(Context context) {
		sendWakefulWork(context, new Intent(Intents.ACTION_CHECK_SYNC));
	}

	private static final String TAG = AbstractSyncService.class.getName();

	private static final int SYNC_STATE_ID = 1;
	private static final int SYNC_RESUME_ID = 2;

	private NotificationManager mNotificationManager;

	private OptimizedSyncClient syncClient;
	private AbstractDatabase helper;

	private String login;
	private String password;
	private String hardwareId;
	private boolean debug;
	private boolean bidirectional;

	public AbstractSyncService(String name) {
		super(name);
	}

	protected abstract int getDataToSynchronize(FileOutputStream fos, String hardwareId)
			throws IllegalArgumentException, IllegalStateException, IOException;

	protected abstract int applyIncomingModifications(InputStream is)
			throws XmlPullParserException, IOException;

	protected abstract void markAsSentForSession(long time);

	protected abstract void markAsReadHidden();

	@Override
	protected void doWakefulWork(Intent intent) {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		helper = AbstractDatabase.getSuper(this);

		hardwareId = PreferenceHelper.getHardwareId(this);
		login = PreferenceHelper.getSyncLogin(this);
		password = PreferenceHelper.getSyncPassword(this);
		debug = PreferenceHelper.isDebugActive(this);

		String serverUrl = PreferenceHelper.getServerUrl(this);
		bidirectional = PreferenceHelper.getSynchronizationBidirectional(this);

		if (PreferenceHelper.isHttpAuthenticated(this)) {
			syncClient = new OptimizedSyncClientHttp(serverUrl, login, password);
		} else {
			syncClient = new OptimizedSyncClientHttp(serverUrl);
		}

		if (Intents.ACTION_CHECK_SYNC.equals(intent.getAction())) {
			int received = 0;
			try {
				onStart();

				File directory = new File(Paths.PATH_SYNCHRO);
				directory.mkdirs();

				updateTimeOffset();

				// look for synchronization ERROR.
				Cursor cursor = helper.getReadableDatabase().query(
						Tables.TABLE_SYNCHISTORY,
						new String[] { Keys.KEY_ROWID, Keys.KEY_ID,
								Keys.KEY_LEVEL },
						Keys.KEY_STATUS + " = " + Status.STATUS_ERROR, null,
						null, null, null);
				/*
				 * We resume the synchronization process
				 */
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					long rowId = cursor.getLong(0);
					UUID id = UUID.fromString(cursor.getString(1));
					int level = cursor.getInt(2);
					Log.i(TAG, "resume on error : " + id + ", level : " + level);
					cursor.close();
					received += resumeOnError(rowId, id, level);
				} else {
					cursor.close();
				}

				// 1 - initialize the session
				onInit();
				UUID sessionId = UUID.fromString(syncClient.initSession(login,
						password, hardwareId, "xml"));
				// 2 - send client modification
				onSend();

				long syncTime = PreferenceHelper.getRealTime(this);

				File outFile = new File(Paths.PATH_SYNCHRO, sessionId
						+ ".lmodif");
				FileOutputStream fos = new FileOutputStream(outFile);
				getDataToSynchronize(fos, hardwareId);
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

				Log.i(TAG, "number of server modifications applied: " + res);

				if (res > -1) {
					values.clear();
					values.put(Keys.KEY_LEVEL, Levels.LEVEL_RECEIVE);
					helper.getWritableDatabase().update(
							Tables.TABLE_SYNCHISTORY, values,
							Keys.KEY_ROWID + "=" + rowId, null);
				} else {
					throw new SynchronizationException(
							"Error sending data to the server.",
							SynchronizationException.ERROR_SEND);
				}

				// 3 - get server modifications
				onReceive();
				received += requestServerModification(sessionId);

				values.clear();
				values.put(Keys.KEY_STATUS, Status.STATUS_OK);
				values.put(Keys.KEY_LEVEL, Levels.LEVEL_RECEIVE);
				helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
						values, Keys.KEY_ROWID + "=" + rowId, null);

				// 4 - close the session
				onClose();
				syncClient.closeSession(sessionId, debug);
			} catch (FileNotFoundException e) {
				MetadataService.startMetadataService(this, e,
						PreferenceHelper.getRealTime(this));
			} catch (IOException e) {
				MetadataService.startMetadataService(this, e,
						PreferenceHelper.getRealTime(this));
			} catch (SynchronizationException e) {
				Log.e(TAG, "error during synchronization", e);
			} catch (Exception e) {
				Log.e(TAG, "error during synchronization", e);
			} finally {
				markAsReadHidden();
				onFinish(received);
			}
		} else if (Intents.ACTION_CANCEL.equals(intent.getAction())) {
			onCancel();
		} else if (Intents.ACTION_RESCHEDULE.equals(intent.getAction())) {
			onReschedule();
		}
	}

	private void updateTimeOffset() {
		try {
			long offset = SntpProvider.getTimeOffsetFromNtp(this);
			onUpdateOffset(offset);
		} catch (SntpException e) {
			Log.e(TAG, "update offset from ntp ->", e);
		}
	}

	private int resumeOnError(long rowId, UUID errorId, int errorLevel)
			throws SynchronizationException {
		ContentValues values = new ContentValues();

		/*
		 * we resume a sent, by re-sending local data an retrieving all the data
		 * from the server
		 */
		int received = 0;
		if (errorLevel == Levels.LEVEL_SEND) {
			Log.i(TAG, "Resuming the sent for the session " + errorId);
			try {
				/* 1 - initialize the resumed session */
				onInitResume();
				String result = syncClient.resumeSend(login, password,
						hardwareId, "xml", errorId);
				/* 2 - sending local modifications */
				onSendResume();
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
					Log.i(TAG,
							"Re-sending data from the file "
									+ outFile.getAbsolutePath() + " skipping "
									+ bytesReceived + " bytes");
					int res = syncClient.resumeSendModification(errorId, fis);
					fis.close();
					if (!debug) {
						outFile.delete();
					}

					Cursor c = helper.getReadableDatabase().query(
							Tables.TABLE_SYNCHISTORY,
							new String[] { Keys.KEY_DATE },
							Keys.KEY_ROWID + "=" + rowId, null, null, null,
							null);
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
				onReceiveResume();
				received = requestServerModification(errorId);

				values.clear();
				values.put(Keys.KEY_STATUS, Status.STATUS_OK);
				helper.getWritableDatabase().update(Tables.TABLE_SYNCHISTORY,
						values, Keys.KEY_ROWID + "=" + rowId, null);

				/* 4 - closing the session */
				onCloseResume();
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
				onInitResume();
				File inFile = new File(Paths.PATH_SYNCHRO, errorId + ".smodif");
				String result = syncClient.resumeReceive(login, password,
						hardwareId, "xml", errorId, inFile.length());
				/* 2 - receiving data */
				onReceiveResume();
				if (!result.equals("error")) {
					received = resumeRequestModification(errorId);
					values.clear();
					values.put(Keys.KEY_STATUS, Status.STATUS_OK);
					helper.getWritableDatabase().update(
							Tables.TABLE_SYNCHISTORY, values,
							Keys.KEY_ROWID + "=" + rowId, null);
					/* 3 - closing the session */
					onCloseResume();
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
		syncClient.resumeRequestModification(errorId, fos, inputFile.length());
		fos.close();

		FileInputStream fis = new FileInputStream(inputFile);
		int result = applyIncomingModifications(fis);
		fis.close();
		if (!debug) {
			inputFile.delete();
		}
		return result;
	}

	private void onCancel() {
		Log.i(TAG, "*** OptimizedSynchronizationService: cancel");
		cancel();
	}

	private void onReschedule() {
		Log.i(TAG, "*** OptimizedSynchronizationService: reschedule");
		reschedule();
	}

	private void onFinish(int received) {
		mNotificationManager.cancel(SYNC_STATE_ID);

		if (received > 0)
			notifySynchronizationResume(received);

		if (PreferenceHelper.getSynchronizationStatus(this)) {
			reschedule();
		}
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

		alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + period * 1000, pi);
	}

	protected abstract Intent getResumeIntent(int received);

	private void notifyState(String msg) {
		NotificationManager notifMgr = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notif = new Notification(W.drawable.logo_android_s, msg,
				System.currentTimeMillis());
		notif.flags = Notification.FLAG_NO_CLEAR;

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, null,
				0);
		notif.setLatestEventInfo(this, getString(W.string.synchronization),
				msg, contentIntent);

		notifMgr.notify(SYNC_STATE_ID, notif);
	}

	private void notifySynchronizationResume(int received) {
		CharSequence chr = getString(W.string.synchro_resume_title);
		Notification notif = new Notification(W.drawable.logo_android_s, chr,
				System.currentTimeMillis());

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
