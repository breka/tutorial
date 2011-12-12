package org.imogene.android.app;

import org.imogene.android.template.R;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.util.ntp.SntpException;
import org.imogene.android.util.ntp.SntpProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class OffsetActivity extends Activity implements OnClickListener {
	
	private static final int DIALOG_SNTPING_ID = 1;
	private static final int DIALOG_SNTP_FAILED_ID = 2;

	private Thread mSntpThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		launchSntp();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SNTPING_ID:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(getString(R.string.ig_obtaining_time_offset));
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			return dialog;
		case DIALOG_SNTP_FAILED_ID:
			return new AlertDialog.Builder(this)
			.setTitle(android.R.string.dialog_alert_title)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(R.string.ig_obtaining_time_offset_failed)
			.setCancelable(false)
			.setPositiveButton(android.R.string.ok, this)
			.setNegativeButton(android.R.string.no, this)
			.create();
		default:
			return super.onCreateDialog(id);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mSntpThread != null) {
			mSntpThread.interrupt();
			try {
				mSntpThread.join();
			} catch (InterruptedException e) {
				// Don't care
			}
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			launchSntp();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
	
	private void launchSntp() {
		mSntpThread = new Thread(new SntpRunnable(), "SntpThread");
		mSntpThread.setPriority(Thread.MAX_PRIORITY);
		mSntpThread.start();
	}
	
	private void onSntpRunning() {
		showDialog(DIALOG_SNTPING_ID);
	}
	
	private void onSntpSucceed(long offset) {
		PreferenceHelper.getSharedPreferences(this)
		.edit()
		.putLong(getString(R.string.ig_ntp_offset_key), offset)
		.commit();
		
		removeDialog(DIALOG_SNTPING_ID);
		setResult(RESULT_OK);
		finish();
	}
	
	private void onSntpFailed() {
		removeDialog(DIALOG_SNTPING_ID);
		showDialog(DIALOG_SNTP_FAILED_ID);
	}
	
	private class SntpRunnable implements Runnable {
		
		public void run() {
			reportSntpRunning();
			try {
				reportSntpSucceed(SntpProvider.getTimeOffsetFromNtp(OffsetActivity.this));
			} catch (SntpException e) {
				reportSntpFailed();
			}

		}

		private void reportSntpRunning() {
			mHandler.sendEmptyMessage(MSG_SNTPING);
		}
		
		private void reportSntpSucceed(long offset) {
			mHandler.sendMessage(Message.obtain(mHandler, MSG_SNTP_SUCCESS, offset));
		}
		
		private void reportSntpFailed() {
			mHandler.sendEmptyMessage(MSG_SNTP_FAILED);
		}
	}
	
	private static final int MSG_SNTPING = 1;
	private static final int MSG_SNTP_SUCCESS = 2;
	private static final int MSG_SNTP_FAILED = 3;
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_SNTPING:
				onSntpRunning();
				break;
			case MSG_SNTP_SUCCESS:
				onSntpSucceed((Long) msg.obj);
				break;
			case MSG_SNTP_FAILED:
				onSntpFailed();
				break;
			}
			super.handleMessage(msg);
		};
	};
}
