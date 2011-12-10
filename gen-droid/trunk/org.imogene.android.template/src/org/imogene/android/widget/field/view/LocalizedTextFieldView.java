package org.imogene.android.widget.field.view;

import java.util.ArrayList;

import org.imogene.android.template.R;
import org.imogene.android.util.LocalizedTextList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class LocalizedTextFieldView extends BaseFieldView<LocalizedTextList> {

	private final ViewFlipper mViewFlipper;
	private final View mLeftView;
	private final View mRightView;
	
	public LocalizedTextFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_view_localized);
		setClickable(false);
		mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		mLeftView = findViewById(R.id.left);
		mRightView = findViewById(R.id.right);
    	mLeftView.setOnClickListener(this);
    	mRightView.setOnClickListener(this);
	}
	
	@Override
	public void setValue(LocalizedTextList value) {
		super.setValue(value);
		init();
	}
	
	private void init() {
		mViewFlipper.removeAllViews();

		LocalizedTextList list = getValue();
        ArrayList<String> locales = list != null ? list.getAvailableLocales() : null;
        
        int size = locales.size();
        if (size > 0) {
        	SparseArray<String> display = new SparseArray<String>(size);
        	
    		String[] llocales = getResources().getStringArray(R.array.languages_iso);
    		String[] ldisplay = getResources().getStringArray(R.array.languages_display);
    		
    		for (int i = 0; i < llocales.length; i++) {
    			int index = 0;
    			if ((index = locales.indexOf(llocales[i])) != -1) {
    				display.put(index, ldisplay[i]);
    			}
    		}
    		
    		for (int i = 0; i < size; i++) {
    	    	View v = inflate(getContext(), R.layout.localized_text_viewer, null);
    	    	((TextView) v.findViewById(R.id.locale)).setText(display.get(i));
    	    	((TextView) v.findViewById(R.id.localized)).setText(list.getLocalized(locales.get(i)));
    	    	v.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	    	mViewFlipper.addView(v);
    		}
        }
        if (size > 1) {
        	mLeftView.setVisibility(View.VISIBLE);
        	mRightView.setVisibility(View.VISIBLE);
        } else {
        	mLeftView.setVisibility(View.GONE);
        	mRightView.setVisibility(View.GONE);
        }
	}
	
	@Override
	public boolean isEmpty() {
		LocalizedTextList ltl = getValue();
		return ltl != null ? ltl.isEmpty() : true;
	}
	
	@Override
	protected String getDisplay() {
		return null;
	}
	
	@Override
	public boolean matchesDependencyValue(String dependencyValue) {
		LocalizedTextList ltl = getValue();
		return ltl != null ? ltl.matches(dependencyValue) : false;
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			mViewFlipper.showNext();
			break;
		case R.id.right:
			mViewFlipper.showPrevious();
			break;
		}
	}
	
}
