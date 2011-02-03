package org.imogene.android.app;

import org.imogene.android.W;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class HighPreferences extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(W.xml.high_preferences);
	}

}
