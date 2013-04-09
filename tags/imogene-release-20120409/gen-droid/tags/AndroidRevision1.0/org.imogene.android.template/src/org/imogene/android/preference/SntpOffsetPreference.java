package org.imogene.android.preference;

import org.imogene.android.app.OffsetActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.Preference;
import android.util.AttributeSet;

public class SntpOffsetPreference extends Preference implements OnSharedPreferenceChangeListener {

	public SntpOffsetPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onAttachedToActivity() {
		super.onAttachedToActivity();
		getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPrepareForRemoval() {
		super.onPrepareForRemoval();
		getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public CharSequence getSummary() {
		return Long.toString(getPersistedLong(0));
	}
	
	@Override
	protected void onClick() {
		super.onClick();
		Context context = getContext();
		context.startActivity(new Intent(context, OffsetActivity.class));
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(getKey()))
			notifyChanged();
	}

}
