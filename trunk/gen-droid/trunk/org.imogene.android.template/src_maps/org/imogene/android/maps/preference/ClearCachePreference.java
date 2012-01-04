package org.imogene.android.maps.preference;

import org.imogene.android.template.R;
import org.imogene.android.util.file.FileUtils;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import android.content.Context;
import android.os.Handler;
import android.preference.Preference;
import android.util.AttributeSet;

public class ClearCachePreference extends Preference {
	
	private final Handler mHandler = new Handler();
	private final Runnable mUpdate = new Runnable() {
		
		@Override
		public void run() {
			notifyChanged();
		}
	};
	
	public ClearCachePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ClearCachePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public CharSequence getSummary() {
		long size = FileUtils.getDirectorySize(OpenStreetMapTileProviderConstants.TILE_PATH_BASE);
		String readableSize = FileUtils.readableFileSize(size);
		return getContext().getString(R.string.ig_precache_clear_summary, readableSize);
	}
	
	@Override
	protected void onClick() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileUtils.deleteDirectory(OpenStreetMapTileProviderConstants.TILE_PATH_BASE);
				mHandler.post(mUpdate);
			}
		}).start();
	}

}
