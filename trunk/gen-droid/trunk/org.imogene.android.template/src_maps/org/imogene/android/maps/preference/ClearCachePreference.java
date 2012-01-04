package org.imogene.android.maps.preference;

import org.imogene.android.template.R;
import org.imogene.android.util.file.FileUtils;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.util.AttributeSet;

public class ClearCachePreference extends Preference {
	
	private static final int MSG_UPDATE_ID = 1;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_ID:
				notifyChanged();
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		};
	};
	
	public ClearCachePreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ClearCachePreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onAttachedToActivity() {
		super.onAttachedToActivity();
		nonBlockingUpdate();
	}
	
	@Override
	public CharSequence getSummary() {
		String readableSize = FileUtils.readableFileSize(getPersistedLong(0));
		return getContext().getString(R.string.ig_precache_clear_summary, readableSize);
	}
	
	@Override
	protected void onClick() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				FileUtils.deleteDirectory(OpenStreetMapTileProviderConstants.TILE_PATH_BASE);
				blockingUpdate();
			}
		}).start();
	}

	private void nonBlockingUpdate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				blockingUpdate();
			}
		}).start();
	}
	
	private void blockingUpdate() {
		long size = FileUtils.getDirectorySize(OpenStreetMapTileProviderConstants.TILE_PATH_BASE);
		persistLong(size);
		mHandler.sendEmptyMessage(MSG_UPDATE_ID);
	}

}
