package org.imogene.android.widget;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class EntityPagerAdapter extends PagerAdapter {

	private static class Page {
		int title;
		int view;
		boolean visible;
		
		public Page(int title, int view, boolean visible) {
			this.title = title;
			this.view = view;
			this.visible = true;
		}
		
	}
	
	private final ArrayList<Page> mPages = new ArrayList<Page>();
	private final Context mContext;
	
	public EntityPagerAdapter(Context context) {
		mContext = context;
	}
	
	public void addPage(int title, int view) {
		mPages.add(new Page(title, view, true));
	}
	
	public void setPageVisible(int view, boolean visible) {
		for (Page c : mPages) {
			if (c.view == view) {
				c.visible = visible;
			}
		}
	}
	
	public int getViewPosition(int viewId) {
		int position = 0;
		for (Page c : mPages) {
			if (c.view == viewId) {
				return position;
			}
			if (c.visible) {
				position++;
			}
		}
		return -1;
	}
	
	public int getTitle(int position) {
		int count = 0;
		for (Page c : mPages) {
			if (c.visible && count == position) {
				return c.title;
			}
			if (c.visible) {
				count++;
			}
		}
		return 0;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getText(getTitle(position));
	}

	@Override
	public int getCount() {
		int count = 0;
		for (Page c : mPages) {
			if (c.visible) {
				count++;
			}
		}
		return count;
	}

	@Override
	public void startUpdate(View container) {}

	@Override
	public Object instantiateItem(View container, int position) {
		int count = 0;
		for (Page c : mPages) {
			if (c.visible && count == position) {
				return container.findViewById(c.view);
			} else if (c.visible) {
				count++;
			}
		}
		return null;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {}

	@Override
	public void finishUpdate(View container) {}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {}

}
