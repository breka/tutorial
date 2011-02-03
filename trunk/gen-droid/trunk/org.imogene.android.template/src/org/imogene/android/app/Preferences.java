package org.imogene.android.app;

import org.imogene.android.W;
import org.imogene.android.app.setup.AccountSetupBasics;
import org.imogene.android.preference.PreferenceHelper;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
