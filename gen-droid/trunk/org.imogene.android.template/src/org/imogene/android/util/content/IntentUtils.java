package org.imogene.android.util.content;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Packages;
import org.imogene.android.util.BoundingBox;
import org.imogene.android.util.dialog.DialogFactory;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

public class IntentUtils {
	
	public static Intent canLaunchWithoutChoose(Context context, Intent[] intents) {
		final PackageManager pm = context.getPackageManager();
		
		int count = 0;
		int position = 0;
		for (int i = 0; i < intents.length; i++) {
			if (intents[i].resolveActivityInfo(pm, 0) != null) {
				count++;
				position = i;
			}
		}
		if (count == 1) {
			return intents[position];
		} else {
			return null;
		}
	}
	
	public static final Intent createShowOnMapIntent(Location location) {
		if (location != null) {
			Intent intent = new Intent(Intents.ACTION_SHOW_ON_MAP);
			intent.putExtra(Extras.EXTRA_LATITUDE, location.getLatitude());
			intent.putExtra(Extras.EXTRA_LONGITUDE, location.getLongitude());
			return intent;
		} else {
			return null;
		}
	}
	
	public static final Intent createShowOnMapIntent(BoundingBox box) {
		if (box != null) {
			Intent intent = new Intent(Intents.ACTION_VIEW_RECT);
			intent.putExtra(Extras.EXTRA_EAST, box.getEast());
			intent.putExtra(Extras.EXTRA_NORTH, box.getNorth());
			intent.putExtra(Extras.EXTRA_SOUTH, box.getSouth());
			intent.putExtra(Extras.EXTRA_WEST, box.getWest());
			return intent;
		} else {
			return null;
		}
	}
	
	public static final Intent createShowRadarIntent(Location location) {
		if (location != null) {
			Intent intent = new Intent(Intents.ACTION_SHOW_RADAR);
			intent.putExtra(Extras.EXTRA_LATITUDE, location.getLatitude());
			intent.putExtra(Extras.EXTRA_LONGITUDE, location.getLongitude());
			return intent;
		} else {
			return null;
		}
	}
	
	public static final Intent createNavigateToIntent(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "," + lon));
			return intent;
		} else {
			return null;
		}
	}

	public static final void treatException(
			ActivityNotFoundException exception, Context context, Intent intent) {
		if (Intents.ACTION_SHOW_ON_MAP.equals(intent.getAction())
				|| (Intent.ACTION_GET_CONTENT.equals(intent.getAction()) && "gps/*"
						.equals(intent.getType()))) {
			Uri uri = Uri.parse("market://search?q=pname:"
					+ Packages.PACKAGE_MAP);
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(i);
		} else if (Intents.ACTION_SHOW_RADAR.equals(intent.getAction())) {
			Uri uri = Uri.parse("market://search?q=pname:"
					+ Packages.PACKAGE_RADAR);
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(i);
		} else if (Intents.ACTION_SCAN.equals(intent.getAction())) {
			Uri uri = Uri.parse("market://search?q=pname:"
					+ Packages.PACKAGE_ZXING);
			Intent i = new Intent(Intent.ACTION_VIEW, uri);
			context.startActivity(i);
		} else {
			Log.e(IntentUtils.class.getName(), "error launching an activity", exception);
			DialogFactory.createActivityNotFoundDialog(context).show();
		}
	}
}
