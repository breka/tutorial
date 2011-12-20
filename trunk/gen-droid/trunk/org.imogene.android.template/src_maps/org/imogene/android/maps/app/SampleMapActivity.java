package org.imogene.android.maps.app;

import greendroid.widget.ActionBarItem;

import org.imogene.android.template.R;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import android.os.Bundle;
import android.widget.Toast;

public class SampleMapActivity extends MapActivity {
	
	private static final String EXTRA_SHOW_LOCATION = "SampleMapActivity_showLocation";
	private static final String EXTRA_SHOW_COMPASS = "SampleMapActivity_showCompass";
	
	private static final int ACTIONBAR_MYLOCATION_ID = 1;
	private static final int ACTIONBAR_COMPASS_ID = 2;
	
	private MyLocationOverlay mLocationOverlay;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		MapView mapView = getMapView();
		
		mLocationOverlay = new MyLocationOverlay(getBaseContext(), mapView, getResourceProxy());
		mapView.getOverlays().add(mLocationOverlay);
		
		addActionBarItem(ActionBarItem.Type.LocateMyself, ACTIONBAR_MYLOCATION_ID);
		addActionBarItem(ActionBarItem.Type.Compass, ACTIONBAR_COMPASS_ID);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(EXTRA_SHOW_LOCATION, mLocationOverlay.isMyLocationEnabled());
		outState.putBoolean(EXTRA_SHOW_COMPASS, mLocationOverlay.isCompassEnabled());

		mLocationOverlay.disableMyLocation();
		mLocationOverlay.disableCompass();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState.getBoolean(EXTRA_SHOW_LOCATION)) {
			mLocationOverlay.enableMyLocation();
		}
		if (savedInstanceState.getBoolean(EXTRA_SHOW_COMPASS)) {
			mLocationOverlay.enableCompass();
		}
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
		case ACTIONBAR_COMPASS_ID:
			if (mLocationOverlay.isCompassEnabled()) {
				mLocationOverlay.disableCompass();
			} else {
				mLocationOverlay.enableCompass();
			}
			return true;
		case ACTIONBAR_MYLOCATION_ID:
			if (mLocationOverlay.isMyLocationEnabled()) {
				mLocationOverlay.disableMyLocation();
				Toast.makeText(this, R.string.maps_set_mode_hide_me, Toast.LENGTH_LONG).show();
			} else {
				mLocationOverlay.enableMyLocation();
				Toast.makeText(this, R.string.maps_set_mode_show_me, Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onHandleActionBarItemClick(item, position);
		}
	}

}
