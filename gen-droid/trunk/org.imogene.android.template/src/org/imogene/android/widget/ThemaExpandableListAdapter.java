package org.imogene.android.widget;

import java.util.ArrayList;

import org.imogene.android.W;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Sync;
import org.imogene.android.database.AbstractDatabase;
import org.imogene.android.database.sqlite.SQLiteBuilder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public final class ThemaExpandableListAdapter extends BaseExpandableListAdapter {
	
	private final Context mContext;
	private final ArrayList<Integer> mGroupData;
	private final ArrayList<ArrayList<EntityChild>> mChildData;
	private final AbstractDatabase mHelper;

	public ThemaExpandableListAdapter(
			Context context,
			ArrayList<Integer> groupData,
			ArrayList<ArrayList<EntityChild>> childData) {
		super();
		mHelper = AbstractDatabase.getSuper(context);
		mContext = context;
		mGroupData = groupData;
		mChildData = childData;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view;
	
		if (convertView != null) {
			view = convertView;
		} else {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
		}
	
		TextView thema = (TextView) view.findViewById(android.R.id.text1);
		thema.setText(getGroup(groupPosition));
	
		return view;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view;
	
		if (convertView != null) {
			view = convertView;
		} else {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(W.layout.entity_row, parent, false);
		}
	
		EntityChild entity = getChild(groupPosition, childPosition);
	
		TextView main = (TextView) view.findViewById(W.id.list_main);
		main.setText(entity.mDescription);
	
		TextView secondary = (TextView) view.findViewById(W.id.list_secondary);
	
		SQLiteDatabase db = mHelper.getReadableDatabase();
		SQLiteBuilder builder = new SQLiteBuilder()
		.setSelectInTable(entity.mTable, "count(*)")
		.appendNotEq(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
		SQLiteStatement stat = db.compileStatement(builder.toSQL());
		long count = stat.simpleQueryForLong();
		stat.close();
	
		if (count > 1) {
			secondary.setText(mContext.getString(W.string.entity_count_pl, count));
		} else {
			secondary.setText(mContext.getString(W.string.entity_count_sg, count));
		}
	
		if (entity.mDrawable != 0) {
			ImageView icon = (ImageView) view.findViewById(W.id.list_icon);
			icon.setImageResource(entity.mDrawable);
		}
	
		View v = view.findViewById(W.id.list_color);
		v.setBackgroundDrawable(entity.mColor);
	
		return view;
	}

	public EntityChild getChild(int groupPosition, int childPosition) {
		return mChildData.get(groupPosition).get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return mChildData.get(groupPosition).size();
	}

	public Integer getGroup(int groupPosition) {
		return mGroupData.get(groupPosition);
	}

	public int getGroupCount() {
		return mGroupData.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean hasStableIds() {
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public static final class EntityChild {

		private final Uri mContentUri;
		private final String mTable;
		private final int mDescription;
		private final int mDrawable;
		private final Drawable mColor;
		
		public EntityChild(Uri contentUri, String table, int description, int drawable, Drawable color) {
			mContentUri = contentUri;
			mTable = table;
			mDescription = description;
			mDrawable = drawable;
			mColor = color;
		}
		
		public final Uri getContentUri() {
			return mContentUri;
		}
	}
}
