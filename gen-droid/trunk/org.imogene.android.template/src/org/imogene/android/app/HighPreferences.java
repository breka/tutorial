package org.imogene.android.app;

import org.imogene.android.W;
import org.imogene.android.preference.MyPreferenceActivity;

import android.os.Bundle;

public class HighPreferences extends MyPreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(W.layout.preference_list_content);
		addPreferencesFromResource(W.xml.high_preferences);
	}

}
