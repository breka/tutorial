package org.imogene.android.app;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.W;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.service.AbstractSyncService;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.widget.ThemaExpandableListAdapter.EntityChild;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

public abstract class AbstractThemaListing extends ExpandableListActivity
		implements OnSharedPreferenceChangeListener {
	
	private static final String EXTRA_SCANNED = "AbstractThemaListing_scanned";
	
	private static final int DIALOG_SEARCH_SERVER_ID = 1;
	
	private static final int ACTIVITY_BARCODE_ID = 1;
	private static final int ACTIVITY_SEARCH_ID = 2;
	
	private SharedPreferences mPreferences;
	
	private String mScanned;
	
	protected abstract void fillData();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fillData();

		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		mPreferences.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(EXTRA_SCANNED, mScanned);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		mScanned = state.getString(EXTRA_SCANNED);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mObserver);
		mPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public final void startActivity(Intent intent) {
		try {
			super.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			IntentUtils.treatException(e, this, intent);
		}
	}
	
	@Override
	public final void startActivityForResult(Intent intent, int requestCode) {
		try {
			super.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException e) {
			IntentUtils.treatException(e, this, intent);
		}
	}
	
	public final void restart() {
		startActivity(getIntent());
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(W.menu.menu_thema_listing, menu);
		menu.findItem(W.id.menu_preferences).setIntent(new Intent(this, Preferences.class));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case W.id.menu_sync :
			AbstractSyncService.startService(this);
			return true;
		case W.id.menu_search :
			startActivityForResult(new Intent(Intents.ACTION_SCAN), ACTIVITY_BARCODE_ID);
			return true;
		default :
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		EntityChild entity = (EntityChild) getExpandableListAdapter().getChild(groupPosition, childPosition);
		startActivity(new Intent(Intent.ACTION_VIEW, entity.getContentUri()));
		return true;
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (getString(W.string.sync_server_url_key).equals(key))
			finish();
		else if (getString(W.string.current_roles_key).equals(key))
			restart();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SEARCH_SERVER_ID:
			return new AlertDialog.Builder(this)
				.setTitle(W.string.scanned_notfound)
				.setMessage(getString(W.string.scanned_notfoundinapp, mScanned))
				.setPositiveButton(android.R.string.ok, new OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						startActivityForResult(new Intent(
							Intents.ACTION_SEARCH_ENTITY)
							.putExtra(Extras.EXTRA_SEARCH, mScanned),
							ACTIVITY_SEARCH_ID);
					}
				})
				.setNegativeButton(android.R.string.no, null)
				.create();
		default:
			return super.onCreateDialog(id);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == ACTIVITY_BARCODE_ID) {
			mScanned = data.getStringExtra("SCAN_RESULT");
			Toast.makeText(this, getString(W.string.scanned, mScanned), Toast.LENGTH_LONG).show();
			Uri uri = SQLiteWrapper.findInDatabase(this, mScanned);
			if (uri != null) {
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			} else {
				showDialog(DIALOG_SEARCH_SERVER_ID);
			}
		} else if (resultCode == RESULT_OK && requestCode == ACTIVITY_SEARCH_ID) {
			startActivity(new Intent(Intent.ACTION_VIEW, data.getData()));
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private final Handler mHandler = new Handler();
	
	protected final ContentObserver mObserver = new ContentObserver(mHandler) {
	
		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}
	
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			((BaseExpandableListAdapter) getExpandableListAdapter()).notifyDataSetChanged();
		}
	};
}
