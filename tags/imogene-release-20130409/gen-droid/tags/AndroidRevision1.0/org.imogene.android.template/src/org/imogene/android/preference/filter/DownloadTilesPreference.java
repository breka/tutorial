package org.imogene.android.preference.filter;

import org.imogene.android.Constants.Intents;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

public class DownloadTilesPreference extends LocationFilterPreference {

	public DownloadTilesPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onClick() {
		notifyFilter();
		Intent intent = new Intent(Intents.ACTION_DOWNLOAD_TILES);
		onPrepareBoxesEditorIntent(intent);
		getContext().startActivity(intent);
	}
	
	@Override
	protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
		// Don't need to be attached no result is expected
	}
}
