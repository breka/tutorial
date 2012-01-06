package org.imogene.android.app;

import org.imogene.android.template.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class HighPreferences extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.high_preferences);
	}

}
