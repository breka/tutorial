package org.imogene.android.app;

import org.imogene.android.W;
import org.imogene.android.widget.ViewPagerIndicator;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class ViewPagerIndicatorActivity extends BaseActivity {

	private ViewPager  mViewPager;
	private ViewPagerIndicator mIndicator;
	
	private Drawable mPrev;
	private Drawable mNext;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the indicator. We need some information here:
        // * What page do we start on.
        // * How many pages are there in total
        // * A callback to get page titles
		Resources res = getResources();
		mPrev = res.getDrawable(W.drawable.indicator_prev_arrow);
		mNext = res.getDrawable(W.drawable.indicator_next_arrow);
		setContentView(W.layout.view_pager);
    }
    
    @Override
    public void onContentChanged() {
    	super.onContentChanged();
        // Retrieve our viewpager
        mViewPager = (ViewPager)findViewById(W.id.pager);
        
        // Find the indicator from the layout
        mIndicator = (ViewPagerIndicator)findViewById(W.id.indicator);
		
        // Set the indicator as the pageChangeListener
        mViewPager.setOnPageChangeListener(mIndicator);
        
		// Set images for previous and next arrows.
		mIndicator.setArrows(mPrev, mNext);
    }
    
    public ViewPager getViewPager() {
    	ensureViewPager();
    	return mViewPager;
    }
    
    public ViewPagerIndicator getViewPagerIndicator() {
    	ensureViewPager();
    	return mIndicator;
    }
    
    private void ensureViewPager() {
    	if (mViewPager != null) {
    		return;
    	}
        setContentView(W.layout.default_view_pager);
    }
    
}