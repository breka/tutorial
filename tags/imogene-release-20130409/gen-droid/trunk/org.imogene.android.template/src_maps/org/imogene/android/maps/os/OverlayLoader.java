package org.imogene.android.maps.os;

import java.util.ArrayList;

import org.imogene.android.database.interfaces.GeoreferencedEntityCursor;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;

public class OverlayLoader extends AsyncTask<String, Void, ArrayList<OverlayItem>> {
	
	public interface OnOverlaysLoadedListener {
		public void onOverlaysLoaded(ArrayList<OverlayItem> items);
	}
	
	private final ArrayList<OverlayItem> mItems = new ArrayList<OverlayItem>();
	private final Context mContext;
	private final Uri mContentUri;
	
	private OnOverlaysLoadedListener mListener;
	
	public OverlayLoader(Context context, Uri uri) {
		mContext = context;
		mContentUri = uri;
	}
	
	public void setOnOverlaysLoadedListener(OnOverlaysLoadedListener listener) {
		mListener = listener;
		if (listener != null && getStatus() == Status.FINISHED) {
			listener.onOverlaysLoaded(mItems);
		}
	}
	
	@Override
	protected ArrayList<OverlayItem> doInBackground(String... params) {
		GeoreferencedEntityCursor c = query(mContext, params[0]);
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			Location l = c.getGeoreference();
			if (l != null) {
				String id = c.getId();
				String title = c.getMainDisplay(mContext);
				String description = c.getSecondaryDisplay(mContext);
				GeoPoint point = new GeoPoint(l);
				mItems.add(new OverlayItem(id, title, description, point));
			}
		}
		c.close();
		return mItems;
	}
	
	private GeoreferencedEntityCursor query(Context context, String whereClause) {
		return (GeoreferencedEntityCursor) SQLiteWrapper.query(context,	mContentUri, whereClause, null);			
	}
	
	@Override
	protected void onPostExecute(ArrayList<OverlayItem> result) {
		if (mListener != null) {
			mListener.onOverlaysLoaded(mItems);
		}
	}
	

}
