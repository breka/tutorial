package org.imogene.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class HorizontalSweeperView extends HorizontalScrollView {

	private static final int SWIPE_MIN_DISTANCE = 5;
	private static final int SWIPE_THRESHOLD_VELOCITY = 300;

	private GestureDetector mGestureDetector;
	private int mActiveFeature = 0;
	private int mViewCount = 0;
	
	private ViewFactory mFactory;
	
	public interface ViewFactory {
		public int getViewCount();
		public View makeView(int index, int width);
	}

	public HorizontalSweeperView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HorizontalSweeperView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalSweeperView(Context context) {
		super(context);
	}
	
	public void setViewFactory(ViewFactory factory) {
		mFactory = factory;
		setup();
	}
	
	private void setup() {
		if (mFactory != null) {
			mViewCount = mFactory.getViewCount();
			LinearLayout wrapper = new LinearLayout(getContext());
			wrapper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			wrapper.setOrientation(LinearLayout.HORIZONTAL);
			for (int i = 0; i < mViewCount; i++) {
				wrapper.addView(mFactory.makeView(i, getWidth()));
			}
		}
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mGestureDetector = new GestureDetector(new MyGestureDetector());
		setOnTouchListener(new MyTouchListener());
	}

	private class MyTouchListener implements View.OnTouchListener {
		
		public boolean onTouch(View v, MotionEvent event) {
			// If the user swipes
			if (mGestureDetector.onTouchEvent(event)) {
				return true;
			} else if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				int scrollX = getScrollX();
				int featureWidth = v.getMeasuredWidth();
				mActiveFeature = ((scrollX + (featureWidth / 2)) / featureWidth);
				int scrollTo = mActiveFeature * featureWidth;
				smoothScrollTo(scrollTo, 0);
				return true;
			} else {
				return false;
			}
		}
	}

	private class MyGestureDetector extends SimpleOnGestureListener {
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature < (mViewCount - 1)) ? mActiveFeature + 1 : mViewCount - 1;
					smoothScrollTo(mActiveFeature * featureWidth, 0);
					return true;
				}
				// left to right
				else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					int featureWidth = getMeasuredWidth();
					mActiveFeature = (mActiveFeature > 0) ? mActiveFeature - 1 : 0;
					smoothScrollTo(mActiveFeature * featureWidth, 0);
					return true;
				}
			} catch (Exception e) {
				Log.e("Fling", "There was an error processing the Fling event:" + e.getMessage());
			}
			return false;
		}
	}

}