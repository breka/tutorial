package org.imogene.android.app;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;

import org.imogene.android.template.R;
import org.imogene.android.widget.ViewPagerIndicator;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class ViewPagerIndicatorActivity extends GDActivity {

	private ViewPager  mViewPager;
	private ViewPagerIndicator mIndicator;
	
	private Drawable mPrev;
	private Drawable mNext;
	
	public ViewPagerIndicatorActivity() {
		super();
	}
	
	public ViewPagerIndicatorActivity(ActionBar.Type type) {
		super(type);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the indicator. We need some information here:
        // * What page do we start on.
        // * How many pages are there in total
        // * A callback to get page titles
		Resources res = getResources();
		mPrev = res.getDrawable(R.drawable.ig_indicator_prev_arrow);
		mNext = res.getDrawable(R.drawable.ig_indicator_next_arrow);
		
		setActionBarContentView(R.layout.ig_view_pager);

		// Retrieve our viewpager
		mViewPager = (ViewPager)findViewById(R.id.ig_pager);
		
		// Find the indicator from the layout
		mIndicator = (ViewPagerIndicator)findViewById(R.id.ig_indicator);
		
		// Set the indicator as the pageChangeListener
		mViewPager.setOnPageChangeListener(mIndicator);
		
		// Set images for previous and next arrows.
		mIndicator.setArrows(mPrev, mNext);
    }
    
    public ViewPager getViewPager() {
    	return mViewPager;
    }
    
    public ViewPagerIndicator getViewPagerIndicator() {
    	return mIndicator;
    }
    
    
}