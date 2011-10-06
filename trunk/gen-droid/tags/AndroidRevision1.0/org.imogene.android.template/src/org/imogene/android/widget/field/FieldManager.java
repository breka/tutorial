package org.imogene.android.widget.field;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class FieldManager {
	
	private final Activity mActivity;
	
	private final RelationManager mManager;
	
	private int mNextId = 0;
	
	private ArrayList<OnActivityDestroyListener> mActivityDestroyListeners;
	
	private ArrayList<OnActivityResultListener> mActivityResultListeners;
	
	public FieldManager(Activity activity, RelationManager manager, int firstRequestCode) {
		mActivity = activity;
		mManager = manager;
		mNextId = firstRequestCode;
	}
	
	public Activity getActivity() {
		return mActivity;
	}
	
	public RelationManager getRelationManager() {
		return mManager;
	}
	
	public int getNextId() {
		return mNextId++;
	}

	public void registerOnActivityDestroyListener(OnActivityDestroyListener listener) {
		if (mActivityDestroyListeners == null) {
			mActivityDestroyListeners = new ArrayList<OnActivityDestroyListener>();
		}
		if (!mActivityDestroyListeners.contains(listener)) {
			mActivityDestroyListeners.add(listener);
		}
	}
	
	public void unregisterOnActivityDestroyListener(OnActivityDestroyListener listener) {
		if (mActivityDestroyListeners != null) {
			mActivityDestroyListeners.remove(listener);
		}
	}
	
	public void dispatchActivityDestroy() {
		ArrayList<OnActivityDestroyListener> list = null;
		if (mActivityDestroyListeners != null) {
			list = new ArrayList<OnActivityDestroyListener>(mActivityDestroyListeners);
		}
		
		if (list != null) {
			final int size = list.size();
			for (int i = 0; i< size; i++) {
				list.get(i).onActivityDestroy();
			}
		}
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
	
	public static interface OnActivityResultListener {
		public boolean onActivityResult(int requestCode, int resultCode, Intent data);
	}
	
	public static interface OnActivityDestroyListener {
		public void onActivityDestroy();
	}
	
	public static interface RelationManager {
		
		public String getCurrentId();
		
		public long getCurrentRowId();
		
		public Uri getCurrentContentUri();
	}

}
