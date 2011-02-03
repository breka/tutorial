package org.imogene.android.preference;

import org.imogene.android.service.AbstractSyncService;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class SyncOneShotPreference extends Preference {

	public SyncOneShotPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onClick() {
		super.onClick();
		AbstractSyncService.startService(getContext());
	}

}
