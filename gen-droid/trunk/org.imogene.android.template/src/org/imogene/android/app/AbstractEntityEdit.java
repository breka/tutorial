package org.imogene.android.app;

import java.util.ArrayList;
import java.util.HashMap;

import org.imogene.android.W;
import org.imogene.android.util.IamLost;
import org.imogene.android.util.content.IntentUtils;
import org.imogene.android.util.database.DatabaseUtils;
import org.imogene.android.util.dialog.DialogFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

public abstract class AbstractEntityEdit extends ScrollingTabActivity implements OnClickListener {
	
	protected static final String ER_TAG = "tag";
	protected static final String ER_LAY = "layout";
	protected static final String ER_MSG = "msg";
	protected static final String ER_DES = "des";
	
	private static final int DIALOG_UNSAVED_ID = 1;
	private static final int DIALOG_ERROR_ID = 2;
	private static final int DIALOG_IAMLOST_ID = 3;
	
	private final ArrayList<HashMap<String, String>> mErrors = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter mAdapter;
	
	protected abstract void save(boolean temporary);
	
	protected abstract ArrayList<HashMap<String, String>> getErrors();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!Intent.ACTION_INSERT.equals(getIntent().getAction()))
			DatabaseUtils.markAs(getContentResolver(), getIntent().getData(), false);
		IamLost.getInstance().add(getTitle().toString());
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		IamLost.getInstance().remove();
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
	
	protected final void showErrorDialog() {
		showDialog(DIALOG_ERROR_ID);
	}
	
	protected final void forceFinish() {
		super.finish();
	}

	@Override
	public void finish() {
		showDialog(DIALOG_UNSAVED_ID);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(W.menu.menu_entity_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case W.id.menu_save :
				save(false);
				return true;
			case W.id.menu_discard :
				setResult(RESULT_CANCELED);
				finish();
				return true;
			case W.id.menu_iamlost :
				showDialog(DIALOG_IAMLOST_ID);
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_UNSAVED_ID :
				return new AlertDialog.Builder(this)
				.setTitle(android.R.string.dialog_alert_title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(W.string.unsaved_dialog_msg)
				.setPositiveButton(android.R.string.yes, this)
				.setNegativeButton(W.string.no, this)
				.setNeutralButton(android.R.string.cancel, null)
				.setCancelable(false)
				.create();
			case DIALOG_ERROR_ID :
				return new AlertDialog.Builder(this)
				.setTitle(android.R.string.dialog_alert_title)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setAdapter(mAdapter =
					new SimpleAdapter(this, mErrors, W.layout.dialog_list_item,
						new String[] { ER_DES, ER_MSG }, new int[] {
							W.id.dialog_item_title,
							W.id.dialog_item_message }), this)
				.setPositiveButton(android.R.string.ok, this)
				.setCancelable(false)
				.create();
			case DIALOG_IAMLOST_ID :
				return DialogFactory.createIamLostDialog(this);
			default :
				return super.onCreateDialog(id);
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		super.onPrepareDialog(id, dialog);
		switch(id) {
		case DIALOG_ERROR_ID:
			if (mAdapter != null) {
				mErrors.clear();
				mErrors.addAll(getErrors());
				mAdapter.notifyDataSetChanged();
			}
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case Dialog.BUTTON_POSITIVE:
			save(false);
			break;
		case Dialog.BUTTON_NEGATIVE:
			forceFinish();
			break;
		default:
			if (which >= 0) {
				HashMap<String, String> map = (HashMap<String, String>) ((AlertDialog) dialog).getListView().getAdapter().getItem(which);
				getTabHost().setCurrentTabByTag(map.get(ER_TAG));
				findViewById(Integer.parseInt(map.get(ER_LAY))).requestFocusFromTouch();
			}
			break;
		}
	}

}
