package org.imogene.android.app;

import org.imogene.android.W;
import org.imogene.android.util.IamLost;
import org.imogene.android.util.content.IntentUtils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Window;

public abstract class AbstractEntityWizard extends FieldFlipperActivity implements OnClickListener {
	
	private static final int DIALOG_UNCOMPLETE_ID = 1;
	
	private final int mLeftIcon;
	
	public AbstractEntityWizard(final int leftIcon) {
		mLeftIcon = leftIcon;
	}
	
	protected abstract void save(boolean temporary);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (mLeftIcon != 0) requestWindowFeature(Window.FEATURE_LEFT_ICON);
		super.onCreate(savedInstanceState);
		IamLost.getInstance().add(getTitle().toString());
	}
	
	@Override
	protected void onPostCreate(Bundle icicle) {
		super.onPostCreate(icicle);
		if (mLeftIcon != 0) setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, mLeftIcon);
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
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_UNCOMPLETE_ID:
			return new AlertDialog.Builder(this)
			.setTitle(android.R.string.dialog_alert_title)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setMessage(W.string.uncomplete_dialog_msg)
			.setPositiveButton(android.R.string.yes, this)
			.setNeutralButton(android.R.string.cancel, null)
			.setCancelable(false)
			.create();
		default:
			return super.onCreateDialog(id);
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		if (which == Dialog.BUTTON_POSITIVE) {
			setResult(RESULT_CANCELED);
			super.finish();
		}
	}
	
	@Override
	public void onFinishClick() {
		save(false);
	}
	
	protected final void forceFinish() {
		super.finish();
	}
	
	@Override
	public void finish() {
		showDialog(DIALOG_UNCOMPLETE_ID);
	}
}
