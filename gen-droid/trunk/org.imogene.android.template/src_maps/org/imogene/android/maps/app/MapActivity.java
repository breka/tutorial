package org.imogene.android.maps.app;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;

import org.imogene.android.maps.ResourceProxyImpl;
import org.imogene.android.maps.util.TileSourceUtil;
import org.imogene.android.template.R;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.CloudmadeUtil;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class MapActivity extends GDActivity {
	
	private static final String EXTRA_SHOW_LOCATION = "SampleMapActivity_showLocation";
	private static final String EXTRA_SHOW_COMPASS = "SampleMapActivity_showCompass";
	
	private static final String PREFERENCES_NAME = "name";
	private static final String PREFERENCE_ZOOM_LEVEL = "zoomLevel";
	private static final String PREFERENCE_SCROLL_X = "scrollX";
	private static final String PREFERENCE_SCROLL_Y = "scrollY";
	private static final String PREFERENCE_TILE_SOURCE = "tileSource";
	
	protected static final int DIALOG_MAPMODE_ID = 1;
	
	protected MapView mMapView;
	protected ResourceProxy mResourceProxy;
	
	private MyLocationOverlay mLocationOverlay;
	
	private SharedPreferences mPrefs;
	
	public MapActivity() {
		super();
	}
	
	public MapActivity(ActionBar.Type type) {
		super(type);
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// only do static initialisation if needed
		if (CloudmadeUtil.getCloudmadeKey().length() == 0) {
			CloudmadeUtil.retrieveCloudmadeKey(getApplicationContext());
		}
		
		mPrefs = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

		mResourceProxy = new ResourceProxyImpl(this);
		
		mMapView = new MapView(this, 256, mResourceProxy);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setMultiTouchControls(true);
		setActionBarContentView(
				mMapView, 
				new RelativeLayout.LayoutParams(
						LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		
		mMapView.getController().setZoom(mPrefs.getInt(PREFERENCE_ZOOM_LEVEL, 1));
		mMapView.scrollTo(mPrefs.getInt(PREFERENCE_SCROLL_X, 0), mPrefs.getInt(PREFERENCE_SCROLL_Y, 0));
		
		mLocationOverlay = new MyLocationOverlay(getBaseContext(), mMapView, mResourceProxy);
		mMapView.getOverlays().add(mLocationOverlay);
		
	}
	
	public MapView getMapView() {
		return mMapView;
	}
	
	public ResourceProxy getResourceProxy() {
		return mResourceProxy;
	}
	
	public void autozoom(IGeoPoint point) {
		mMapView.getController().animateTo(point);
	}
	
	public void changeCompassState() {
		if (mLocationOverlay.isCompassEnabled()) {
			mLocationOverlay.disableCompass();
		} else {
			mLocationOverlay.enableCompass();
		}
	}
	
	public void changeMyLocationState() {
		if (mLocationOverlay.isMyLocationEnabled()) {
			mLocationOverlay.disableMyLocation();
			Toast.makeText(this, R.string.maps_set_mode_hide_me, Toast.LENGTH_LONG).show();
		} else {
			mLocationOverlay.enableMyLocation();
			Toast.makeText(this, R.string.maps_set_mode_show_me, Toast.LENGTH_LONG).show();
		}
	}
	
	public void changeConnectionState() {
		final boolean useDataConnection = !mMapView.useDataConnection();
		mMapView.setUseDataConnection(useDataConnection);
		final int id = useDataConnection ? R.string.maps_set_mode_online : R.string.maps_set_mode_offline;
		Toast.makeText(this, id, Toast.LENGTH_LONG).show();
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
	protected void onPause() {
		final SharedPreferences.Editor edit = mPrefs.edit();
		edit.putString(PREFERENCE_TILE_SOURCE, mMapView.getTileProvider().getTileSource().name());
		edit.putInt(PREFERENCE_SCROLL_X, mMapView.getScrollX());
		edit.putInt(PREFERENCE_SCROLL_Y, mMapView.getScrollY());
		edit.putInt(PREFERENCE_ZOOM_LEVEL, mMapView.getZoomLevel());
		edit.commit();

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		final String tileSourceName = mPrefs.getString(PREFERENCE_TILE_SOURCE,
				TileSourceFactory.DEFAULT_TILE_SOURCE.name());
		try {
			final ITileSource tileSource = TileSourceFactory.getTileSource(tileSourceName);
			mMapView.setTileSource(tileSource);
		} catch (final IllegalArgumentException ignore) {
		}
	}

	@Override
	public boolean onTrackballEvent(final MotionEvent event) {
		return mMapView.onTrackballEvent(event);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_MAPMODE_ID:
			return new AlertDialog.Builder(this)
			.setTitle(R.string.maps_map_mode)
			.setSingleChoiceItems(
					TileSourceUtil.readableTileSources(mResourceProxy),
					-1,
					mMapModeListener)
			.create();
		default:
			return super.onCreateDialog(id);
		}
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_MAPMODE_ID:
			int position = mMapView.getTileProvider().getTileSource().ordinal();
			((AlertDialog) dialog).getListView().setItemChecked(position, true);
			break;

		default:
			break;
		}
		super.onPrepareDialog(id, dialog);
	}
	
	private final OnClickListener mMapModeListener = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			mMapView.setTileSource(TileSourceFactory.getTileSource(which));
			dialog.dismiss();
		}

	};
}
