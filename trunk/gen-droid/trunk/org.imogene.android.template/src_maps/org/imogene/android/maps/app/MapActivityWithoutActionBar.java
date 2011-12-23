package org.imogene.android.maps.app;

import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.maps.os.OverlayLoader;
import org.imogene.android.maps.os.OverlayLoader.OnOverlaysLoadedListener;
import org.imogene.android.maps.widget.BalloonView;
import org.imogene.android.template.R;
import org.imogene.android.util.content.ContentUrisUtils;
import org.imogene.android.util.database.DatabaseUtils;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView.LayoutParams;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MapActivityWithoutActionBar extends MapActivity {
	
	private static final int MENU_MAPMODE_ID = 1;
	private static final int MENU_OFFLINE_ID = 2;
	private static final int MENU_MYLOCATION_ID = 3;
	private static final int MENU_COMPASS_ID = 4;

	private SQLiteBuilder mSQLBuilder = null;
	private Uri mContentUri;
	
	private BalloonView mBalloonView;
	
	private OverlayLoader mLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContentUri = getIntent().getData();
		
		if (getIntent().hasExtra(Extras.EXTRA_WHERE)) {
			mSQLBuilder = getIntent().getParcelableExtra(Extras.EXTRA_WHERE);
		}
		
		mLoader = (OverlayLoader) getLastNonConfigurationInstance();
		if (mLoader == null) {
			mLoader = new OverlayLoader(getApplicationContext(), mContentUri);
			mLoader.execute(buildWhereClause());
		}
		
		mLoader.setOnOverlaysLoadedListener(mLoaderListener);
		
		mMapView.setMapListener(mMapListener);
		
		// hide the action bar
		getActionBar().setVisibility(View.GONE);
		findViewById(R.id.gd_action_bar_colorstrip).setVisibility(View.GONE);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		mLoader.setOnOverlaysLoadedListener(null);
		return mLoader;
	}
	
	private String buildWhereClause() {
		return DatabaseUtils.computeWhere(mSQLBuilder).toSQL();
//		BoundingBoxE6 bb = getMapView().getBoundingBox();
//		
//        SQLiteBuilder locations = new SQLiteBuilder(GpsLocation.Columns.TABLE_NAME, GpsLocation.Columns._ID);
//        locations.appendSup(GpsLocation.Columns.LONGITUDE, bb.getLonWestE6() / 1E6);
//        locations.appendInf(GpsLocation.Columns.LONGITUDE, bb.getLonEastE6() / 1E6);
//        locations.appendSup(GpsLocation.Columns.LATITUDE, bb.getLatSouthE6() / 1E6);
//        locations.appendInf(GpsLocation.Columns.LATITUDE, bb.getLatNorthE6() / 1E6);
//		
//		b.appendIn(TestFieldEntity.Columns.GEOFIELD, locations.create());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_MYLOCATION_ID, Menu.NONE, R.string.maps_my_location)
		.setIcon(R.drawable.ig_ic_menu_mylocation);
		menu.add(Menu.NONE, MENU_COMPASS_ID, Menu.NONE, R.string.maps_compass)
		.setIcon(R.drawable.ig_ic_menu_compass);
		menu.add(Menu.NONE, MENU_MAPMODE_ID, Menu.NONE, R.string.maps_map_mode)
		.setIcon(R.drawable.ig_ic_menu_mapmode);
		menu.add(Menu.NONE, MENU_OFFLINE_ID, Menu.NONE, R.string.maps_offline_mode)
		.setIcon(R.drawable.ig_ic_menu_offline);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_COMPASS_ID:
			changeCompassState();
			return true;
		case MENU_MAPMODE_ID:
			showDialog(DIALOG_MAPMODE_ID);
			return true;
		case MENU_MYLOCATION_ID:
			changeMyLocationState();
			return true;
		case MENU_OFFLINE_ID:
			changeConnectionState();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private final OnItemGestureListener<OverlayItem> mGestureListener = new OnItemGestureListener<OverlayItem>() {
		
		@Override
		public boolean onItemLongPress(int index, OverlayItem item) {
			return false;
		}
		
		@Override
		public boolean onItemSingleTapUp(int index, OverlayItem item) {
			boolean isRecycled = true;

			if (mBalloonView == null) {
				mBalloonView = new BalloonView(MapActivityWithoutActionBar.this);
				mBalloonView.setOnClickListener(mBalloonClickListener);
				isRecycled = false;
			}

			mBalloonView.setTag(ContentUrisUtils.withAppendedId(mContentUri, item.mUid));
			mBalloonView.setData(item.mTitle, item.mDescription);

			LayoutParams params = new LayoutParams(
					LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,
					item.mGeoPoint,
					LayoutParams.BOTTOM_CENTER,
					0, -34);

			if (isRecycled) {
				mBalloonView.setLayoutParams(params);
			} else {
				mMapView.addView(mBalloonView, params);
			}

			autozoom(item.mGeoPoint);
			return true;
		}
	};
	
	private final MapListener mMapListener = new MapListener() {
		
		@Override
		public boolean onZoom(ZoomEvent event) {
			if (mBalloonView != null && mBalloonView.getVisibility() == View.VISIBLE) {
				mBalloonView.requestLayout();
			}
			return false;
		}
		
		@Override
		public boolean onScroll(ScrollEvent event) {
			return false;
		}
	};
	
	private final OnClickListener mBalloonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Uri uri = (Uri) v.getTag();
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
			mBalloonView.setVisibility(View.GONE);
		}
	};
	
	private final OnOverlaysLoadedListener mLoaderListener = new OnOverlaysLoadedListener() {
		
		@Override
		public void onOverlaysLoaded(ArrayList<OverlayItem> items) {
			ItemizedIconOverlay<OverlayItem> ov = new ItemizedIconOverlay<OverlayItem>(
					items,
					getResources().getDrawable(R.drawable.maps_bubble),
					mGestureListener,
					mResourceProxy);
			
			mMapView.getOverlayManager().add(ov);
			mMapView.postInvalidate();
		}
	};
	
}
