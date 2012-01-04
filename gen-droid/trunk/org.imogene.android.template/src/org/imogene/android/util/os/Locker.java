package org.imogene.android.util.os;

import android.os.Handler;

public class Locker implements Runnable {

	private boolean mLocked = false;
	private final Handler mHandler = new Handler();
	
	public synchronized void lock() {
		mLocked = true;
		mHandler.postDelayed(this, 1000);
	}
	
	public synchronized boolean isLocked() {
		return mLocked;
	}
	
	@Override
	public synchronized void run() {
		mLocked = false;
	}
	
	public synchronized void cancel() {
		mHandler.removeCallbacks(this);
		mLocked = false;
	}
	
}
