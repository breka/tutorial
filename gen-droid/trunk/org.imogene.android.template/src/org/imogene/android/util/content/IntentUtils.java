package org.imogene.android.util.content;

import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Packages;
import org.imogene.android.util.dialog.DialogFactory;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class IntentUtils {
	
	public static final void treatException(ActivityNotFoundException exception, Context context, Intent intent) {
		if (Intents.ACTION_SCAN.equals(intent.getAction())) {
			Uri uri = Uri.parse("market://search?q=pname:" + Packages.PACKAGE_ZXING);
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(i);
		} else {
			Log.e(IntentUtils.class.getName(), "error launching an activity", exception);
			DialogFactory.createActivityNotFoundDialog(context).show();
		}
	}
}
