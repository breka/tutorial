package org.imogene.android.widget.field.view;

import java.util.ArrayList;
import java.util.Locale;

import org.imogene.android.W;
import org.imogene.android.util.LocalizedTextList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class LocalizedTextFieldView extends DefaultEntityView<LocalizedTextList> implements OnItemSelectedListener {

	private Gallery mGallery;
	private int mItemsCount = 0;
	private int mCurrentPosition = 0;
	
	public LocalizedTextFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.localized_text_field_view);
		setClickable(false);
	}
	
	@Override
	public void setValue(LocalizedTextList value) {
		super.setValue(value);
		MyAdapter adapter = new MyAdapter(getContext(), value, this);
		mGallery = (Gallery) findViewById(W.id.gallery);
	    mGallery.setAdapter(adapter);
	    mGallery.setSpacing(0);
	    mGallery.setFadingEdgeLength(0);
	    mGallery.setOnItemSelectedListener(this);
	    mItemsCount = mGallery.getCount();
	    if (mItemsCount > 1) {
	    	int realCount = adapter.getRealCount();
	    	int pos = adapter.getLocalePosition(Locale.getDefault().getLanguage());
	    	int base = ((int) (Integer.MAX_VALUE / (2 * realCount))) * realCount;
	    	mGallery.setSelection(base + pos);
	    }
	}
	
	@Override
	protected boolean isEmpty() {
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
		case W.id.left:
			mCurrentPosition++;
			if (mCurrentPosition > mItemsCount - 1) {
				mCurrentPosition = 0;
			}
			mGallery.setSelection(mCurrentPosition);
			break;
		case W.id.right:
			mCurrentPosition--;
			if (mCurrentPosition < 0) {
				mCurrentPosition = mItemsCount - 1;
			}
			mGallery.setSelection(mCurrentPosition);
			break;
		}
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mCurrentPosition = position;
		if (parent.getHeight() < view.getHeight()) {
			parent.requestLayout();
			parent.invalidate();
		}
	}
	
	public void onNothingSelected(AdapterView<?> parent) {
		mCurrentPosition = 0;
	}

	private static class MyAdapter extends BaseAdapter {
		
	    private final Context mContext;
	    private final LocalizedTextList mList;
	    private final OnClickListener mListener;
	    private final ArrayList<String> mLocales;
	    private final SparseArray<String> mLocalesDisplay;
	    private final SparseArray<View> mViews;
	    private final int mRealCount;
	    
	    public MyAdapter(Context c, LocalizedTextList list, OnClickListener listener) {
	    	mContext = c;
	        mListener = listener;
	        mList = list;
	        mLocales = list != null ? list.getAvailableLocales() : null;
	        mRealCount = mLocales != null ? mLocales.size() : 0;

	        if (mRealCount > 0) {
	        	mLocalesDisplay = new SparseArray<String>(mRealCount);
	        	mViews = new SparseArray<View>(mRealCount);
	        	init();
	        } else {
	        	mLocalesDisplay = null;
	        	mViews = null;
	        }
	    }
	    
	    private void init() {
    		String[] locales = mContext.getResources().getStringArray(W.array.languages_iso);
    		String[] display = mContext.getResources().getStringArray(W.array.languages_display);
	    	
	    	for (int i = 0; i < locales.length; i++) {
	    		int index = 0;
	    		if ((index = mLocales.indexOf(locales[i])) != -1) {
	    			mLocalesDisplay.put(index, display[i]);
	    		}
	    	}
	    }
	    
	    public int getLocalePosition(String locale) {
	    	return mLocales != null ? mLocales.indexOf(locale) : -1;
	    }
	    
	    public int getRealCount() {
	    	return mRealCount;
	    }

	    public int getCount() {
	        return mRealCount > 0 ? (mRealCount == 1 ? 1 : Integer.MAX_VALUE) : 0;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	int moduloPosition = (position % mRealCount);
	    	if (mViews.get(moduloPosition) == null) {
		    	View v = LayoutInflater.from(mContext).inflate(W.layout.localized_text_viewer, null);
		    	((TextView) v.findViewById(W.id.locale)).setText(mLocalesDisplay.get(moduloPosition));
		    	((TextView) v.findViewById(W.id.localized)).setText(mList.getLocalized(mLocales.get(moduloPosition)));
		    	if (mRealCount != 1) {
		    		v.findViewById(W.id.left).setOnClickListener(mListener);
		    		v.findViewById(W.id.right).setOnClickListener(mListener);
		    	} else {
		    		v.findViewById(W.id.left).setVisibility(View.GONE);
		    		v.findViewById(W.id.right).setVisibility(View.GONE);
		    	}
		    	v.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		    	mViews.put(moduloPosition, v);
		    	return v;
	    	} else {
	    		return mViews.get(moduloPosition);
	    	}
	    }
	}
	
}
