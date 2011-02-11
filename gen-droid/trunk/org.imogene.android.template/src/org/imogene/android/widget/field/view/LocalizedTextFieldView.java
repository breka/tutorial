package org.imogene.android.widget.field.view;

import java.util.ArrayList;

import org.imogene.android.W;
import org.imogene.android.util.LocalizedTextList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

public class LocalizedTextFieldView extends DefaultEntityView<LocalizedTextList> {

	public LocalizedTextFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.localized_text_field_view);
	}
	
	@Override
	public void setValue(LocalizedTextList value) {
		super.setValue(value);
		Gallery g = (Gallery) findViewById(W.id.gallery);
	    g.setAdapter(new MyAdapter(getContext(), value));
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

	private static class MyAdapter extends BaseAdapter {
		
	    private final Context mContext;
	    private final LocalizedTextList mList;
	    private final ArrayList<String> mLocales;

	    public MyAdapter(Context c, LocalizedTextList list) {
	        mContext = c;
	        mList = list;
	        mLocales = list != null ? list.getAvailableLocales() : null;
	    }

	    public int getCount() {
	        return mLocales != null ? mLocales.size() : 0;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	    	View v = LayoutInflater.from(mContext).inflate(W.layout.localized_text_viewer, null);
	    	((TextView) v.findViewById(W.id.locale)).setText(mLocales.get(position));
	    	((TextView) v.findViewById(W.id.localized)).setText(mList.getLocalized(mLocales.get(position)));
	    	v.setLayoutParams(new Gallery.LayoutParams(parent.getWidth(), LayoutParams.WRAP_CONTENT));
	        return v;
	    }
	}
	
}
