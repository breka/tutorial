package org.imogene.android.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.imogene.android.W;
import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Paths;
import org.imogene.android.database.AbstractDatabase;
import org.imogene.android.preference.PreferenceHelper;
import org.imogene.android.sync.OptimizedSyncClient;
import org.imogene.android.sync.http.OptimizedSyncClientHttp;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public abstract class AbstractEntityDownloader extends Activity {

	private static final int DIALOG_DOWNLOAD_ID = 1;
	private static final int DIALOG_FAILED_ID = 2;

	private String mSearched;
	
	private Thread mDownloadThread;

	protected abstract AbstractDownloader newDownloader(String searched);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSearched = getIntent().getStringExtra(Extras.EXTRA_SEARCH);

		mDownloadThread = new Thread(newDownloader(mSearched), "Downloader");
		mDownloadThread.setPriority(Thread.NORM_PRIORITY - 1);
		mDownloadThread.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDownloadThread != null) {
			mDownloadThread.interrupt();
			try {
				mDownloadThread.join();
			} catch (Exception e) {
				// Don't care
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_ID:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(W.string.scanned_searching);
			dialog.setIndeterminate(true);
			dialog.setButton(
				DialogInterface.BUTTON_NEGATIVE, 
				getString(android.R.string.cancel), 
				new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (mDownloadThread != null) {
							mDownloadThread.interrupt();
						}
						setResult(RESULT_CANCELED);
						finish();
					}
				});
			return dialog;
		case DIALOG_FAILED_ID:
			return new AlertDialog.Builder(this)
			.setTitle(W.string.scanned_notfound)
			.setMessage(getString(W.string.scanned_notfoundonserver, mSearched))
			.setPositiveButton(android.R.string.ok, new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					setResult(RESULT_CANCELED);
					finish();
				}
			})
			.setCancelable(false)
			.create();
		default:
			return super.onCreateDialog(id);
		}
	}

	private void onSearch() {
		showDialog(DIALOG_DOWNLOAD_ID);
	}

	private void onSuccess(String searched) {
		dismissDialog(DIALOG_DOWNLOAD_ID);
		Uri uri = AbstractDatabase.getSuper(this).findInDatabase(searched);
		if (uri != null) {
			setResult(RESULT_OK, new Intent().setData(uri));
			finish();
		} else {
			showDialog(DIALOG_FAILED_ID);
		}
	}

	private void onFailed() {
		dismissDialog(DIALOG_DOWNLOAD_ID);
		showDialog(DIALOG_FAILED_ID);
	}

	protected abstract class AbstractDownloader implements Runnable {

		private final Context mContext;
		
		private final OptimizedSyncClient mSyncClient;

		private final File mFile;
		private final String mLogin;
		private final String mPassword;
		private final String mSearched;

		public AbstractDownloader(Context context, String searched) {
			File dir = new File(Paths.PATH_SYNCHRO);
			dir.mkdirs();
			mContext = context;
			mFile = new File(dir, "searched_" + searched + ".ssearch");
			
			mLogin = PreferenceHelper.getSyncLogin(context);
			mPassword = PreferenceHelper.getSyncPassword(context);			
			
			mSyncClient = new OptimizedSyncClientHttp(PreferenceHelper.getServerUrl(context));
			
			mSearched = searched;
		}

		public void run() {
			reportSearching();
			
			try {
				FileOutputStream fos = new FileOutputStream(mFile);
				mSyncClient.searchEntity(mLogin, mPassword, mSearched, fos);
				fos.close();
				parse(mContext, mFile);
				reportSuccess();
			} catch (Exception e) {
				reportFailed();
			}
		}

		protected abstract void parse(Context context, File file)
				throws FileNotFoundException, XmlPullParserException,
				IOException;

		private void reportSearching() {
			mHandler.sendEmptyMessage(MSG_SEARCHING);
		}

		private void reportSuccess() {
			mHandler.sendMessage(Message.obtain(mHandler, MSG_SUCCESS,
					mSearched));
		}

		private void reportFailed() {
			mHandler.sendEmptyMessage(MSG_FAILED);
		}
	}

	private static final int MSG_SEARCHING = 1;
	private static final int MSG_SUCCESS = 4;
	private static final int MSG_FAILED = 5;

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SEARCHING:
				onSearch();
				break;
			case MSG_SUCCESS:
				onSuccess((String) msg.obj);
				break;
			case MSG_FAILED:
				onFailed();
				break;
			}
			super.handleMessage(msg);
		}
	};

}
