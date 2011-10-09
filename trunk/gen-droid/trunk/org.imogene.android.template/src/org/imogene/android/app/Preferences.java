package org.imogene.android.app;

import org.imogene.android.W;
import org.imogene.android.app.setup.AccountSetupBasics;
import org.imogene.android.preference.MyPreferenceActivity;
import org.imogene.android.preference.PreferenceHelper;

import android.os.Bundle;

public class Preferences extends MyPreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(W.layout.preference_list_content);
		addPreferencesFromResource(W.xml.preferences);
	}

	@Override
	public void finish() {
		if (PreferenceHelper.getShortPassword(this) == null) {
			AccountSetupBasics.actionModifyAccount(this);
		}
		super.finish();
	}
}
