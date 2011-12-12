package org.imogene.android.widget;

import java.util.ArrayList;

import org.imogene.android.template.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IntentChooserAdapter extends BaseAdapter {
	
	private final ArrayList<DisplayResolveInfo> mList = new ArrayList<DisplayResolveInfo>();

	private final LayoutInflater mInflater;

	private final PackageManager mPackageManager;

	public IntentChooserAdapter(Context context, Intent[] intents) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPackageManager = context.getPackageManager();

		for (int i = 0; i < intents.length; i++) {
			Intent ii = intents[i];
			if (ii == null) {
				continue;
			}
			ActivityInfo ai = ii.resolveActivityInfo(mPackageManager, 0);
			if (ai == null) {
				continue;
			}
			ResolveInfo ri = new ResolveInfo();
			ri.activityInfo = ai;
			mList.add(new DisplayResolveInfo(ri, ri.loadLabel(mPackageManager), ii, ai.loadLabel(mPackageManager)));
		}
	}
	
	public Intent intentForPosition(int position) {
		return mList.get(position).originalIntent;
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.ig_intent_chooser_item, parent, false);
		} else {
			view = convertView;
		}
		bindView(view, mList.get(position));
		return view;
	}

	private final void bindView(View view, DisplayResolveInfo info) {
		final ImageView icon = (ImageView) view.findViewById(android.R.id.icon);
		final TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		final TextView text2 = (TextView) view.findViewById(android.R.id.text2);
		
		if (info.displayIcon == null) {
			info.displayIcon = info.ri.loadIcon(mPackageManager);
		}
		icon.setImageDrawable(info.displayIcon);
		
		text1.setText(info.displayLabel);
		
		if (info.extendedInfo != null) {
			text2.setVisibility(View.VISIBLE);
			text2.setText(info.extendedInfo);
		} else {
			text2.setVisibility(View.GONE);
		}
	}

	private static final class DisplayResolveInfo {
		ResolveInfo ri;
		CharSequence displayLabel;
		Drawable displayIcon;
		CharSequence extendedInfo;
		Intent originalIntent;

		DisplayResolveInfo(ResolveInfo pri, CharSequence pLabel, Intent pIntent, CharSequence pInfo) {
			ri = pri;
			displayLabel = pLabel;
			extendedInfo = pInfo;
			originalIntent = pIntent;
		}
	}

}
