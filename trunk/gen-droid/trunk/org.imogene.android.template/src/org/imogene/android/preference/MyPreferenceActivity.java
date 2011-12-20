package org.imogene.android.preference;


import greendroid.app.GDPreferenceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceGroup;

public class MyPreferenceActivity extends GDPreferenceActivity {
	
	private MyPreferenceManager mManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mManager = new MyPreferenceManager(this, 0);
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		registerManager(getPreferenceScreen());
	}
	
	private void registerManager(Preference pref) {
		if (pref instanceof PreferenceGroup) {
			PreferenceGroup group = (PreferenceGroup) pref;
			int count = group.getPreferenceCount();
			for (int i = 0; i < count; i++) {
				registerManager(group.getPreference(i));
			}
		} else if (pref instanceof MyPreference) {
			((MyPreference) pref).onAttachToHierarchy(mManager);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
        mManager.dispatchActivityResult(requestCode, resultCode, data);
	}
	


}
