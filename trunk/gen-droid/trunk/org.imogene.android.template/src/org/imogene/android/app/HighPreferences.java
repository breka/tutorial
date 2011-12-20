package org.imogene.android.app;

import org.imogene.android.preference.MyPreferenceActivity;
import org.imogene.android.template.R;

import android.os.Bundle;

public class HighPreferences extends MyPreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.high_preferences);
	}

}
