package org.imogene.android.preference;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;

public class MyPreferenceManager {
	
	private Activity mActivity;

	private ArrayList<OnActivityResultListener> mActivityResultListeners;
	
    private int mNextRequestCode;
    
    public MyPreferenceManager(Activity activity, int firstRequestCode) {
        mActivity = activity;
        mNextRequestCode = firstRequestCode;
    }
    
    public Activity getActivity() {
    	return mActivity;
    }

	public void registerOnActivityResultListener(OnActivityResultListener listener) {
		if (mActivityResultListeners == null) {
			mActivityResultListeners = new ArrayList<OnActivityResultListener>();
		}
		if (!mActivityResultListeners.contains(listener)) {
			mActivityResultListeners.add(listener);
		}
	}
	
	public void unregisterOnActivityResultListener(OnActivityResultListener listener) {
		if (mActivityResultListeners != null) {
			mActivityResultListeners.remove(listener);
		}
	}
	
	public void dispatchActivityResult(int requestCode, int resultCode, Intent data) {
		ArrayList<OnActivityResultListener> list = null;
		if (mActivityResultListeners != null) {
			list = new ArrayList<OnActivityResultListener>(mActivityResultListeners);
		}
		
		if (list != null) {
			final int size = list.size();
			for (int i = 0; i< size; i++) {
				if (list.get(i).onActivityResult(requestCode, resultCode, data)) {
					break;
				}
			}
		}
	}
	
    public int getNextRequestCode() {
        synchronized (this) {
            return mNextRequestCode++;
        }
    }
	
	public static interface OnActivityResultListener {
		
        public boolean onActivityResult(int requestCode, int resultCode, Intent data);

	}
}
