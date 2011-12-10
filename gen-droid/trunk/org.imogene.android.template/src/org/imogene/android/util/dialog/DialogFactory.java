package org.imogene.android.util.dialog;

import org.imogene.android.template.R;
import org.imogene.android.util.IamLost;

import android.app.AlertDialog;
import android.content.Context;

public class DialogFactory {

	public static AlertDialog createActivityNotFoundDialog(Context context) {
		return new AlertDialog.Builder(context)
		.setTitle(R.string.anf_title)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setMessage(R.string.anf)
		.setPositiveButton(android.R.string.ok, null)
		.setCancelable(false)
		.create();
	}

	public static AlertDialog createIamLostDialog(Context context) {
		return new AlertDialog.Builder(context)
		.setTitle(R.string.iamlost_title)
		.setItems(IamLost.getInstance().get(), null)
		.setPositiveButton(android.R.string.ok, null)
		.create();
	}

}
