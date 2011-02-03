package org.imogene.android.util;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class WakeLockSingleton {
	
	private static final String TAG = WakeLockSingleton.class.getName(); 

	private static WakeLockSingleton mWakeLockSingleton;
	private WakeLock mWakeLock;
	
	private static int mUsers = 0;
	
	private WakeLockSingleton(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
	}
	
	public static final WakeLockSingleton getInstance(Context context) {
		if (mWakeLockSingleton == null) {
			mWakeLockSingleton = new WakeLockSingleton(context);
		}
		return mWakeLockSingleton;
	}
	
	public final void acquire() {
		if (mUsers == 0) {
			mWakeLock.acquire();
		}
		mUsers++;
	}
	
	public final void release() {
		if (mUsers > 0) {
			mUsers--;
			if (mUsers == 0) {
				mWakeLock.release();
			}
		}
	}
}
