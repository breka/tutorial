package org.imogene.android.app;

import org.imogene.android.template.R;
import org.imogene.android.preference.MyPreferenceActivity;

import android.os.Bundle;

public class HighPreferences extends MyPreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preference_list_content);
		addPreferencesFromResource(R.xml.high_preferences);
	}

}
