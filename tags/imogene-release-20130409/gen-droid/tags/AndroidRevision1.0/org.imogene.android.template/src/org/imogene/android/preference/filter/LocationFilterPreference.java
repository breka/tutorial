package org.imogene.android.preference.filter;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.common.filter.LocationFilter;
import org.imogene.android.preference.MyPreference;
import org.imogene.android.preference.MyPreferenceManager;
import org.imogene.android.preference.MyPreferenceManager.OnActivityResultListener;
import org.imogene.android.util.BoundingBox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;

public class LocationFilterPreference extends FilterPreference<LocationFilter> implements MyPreference, OnActivityResultListener {

	private MyPreferenceManager mManager;
	
	private int mRequestCode;
	
	public LocationFilterPreference(Context context, AttributeSet attrs) {
		super(context, attrs, LocationFilter.FILTER_CREATOR);
	}
	
	@Override
	public CharSequence getSummary() {
		return super.getSummary();
	}
	
	@Override
	protected void onClick() {
		Intent intent = new Intent(Intents.ACTION_MANAGE_RECT);
		onPrepareBoxesEditorIntent(intent);
		mManager.getActivity().startActivityForResult(intent, mRequestCode);
	}
	
	protected void onPrepareBoxesEditorIntent(Intent intent) {
		Bundle bundle = BoundingBox.toBundle(getFilter().getBoxes());
		if (bundle != null) {
			intent.putExtra(Extras.EXTRA_BOXES_BUNDLE, bundle);
		}
	}
	
	public void onAttachToHierarchy(MyPreferenceManager manager) {
		mManager = manager;
		manager.registerOnActivityResultListener(this);
		mRequestCode = manager.getNextRequestCode();
	}
	
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mRequestCode) {
			if (data != null && data.hasExtra(Extras.EXTRA_BOXES_BUNDLE)) {
				getFilter().setBoxes(BoundingBox.fromBundle(data.getBundleExtra(Extras.EXTRA_BOXES_BUNDLE)));
				persistFilter();
			}
			return true;
		}
		return false;
	}
	
}
