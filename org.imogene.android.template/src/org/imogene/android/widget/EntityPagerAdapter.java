package org.imogene.android.widget;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class EntityPagerAdapter extends PagerAdapter implements ViewPagerIndicator.PageInfoProvider {

	private static class Couple {
		int title;
		int view;
		
		public Couple(int title, int view) {
			this.title = title;
			this.view = view;
		}
	}
	
	private final ArrayList<Couple> mPages = new ArrayList<EntityPagerAdapter.Couple>();
	
	public void addPage(int title, int view) {
		mPages.add(new Couple(title, view));
	}
	
	public int getViewPosition(int viewId) {
		for (int i = 0; i < mPages.size(); i++) {
			if (mPages.get(i).view == viewId) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int getTitle(int pos) {
		return mPages.get(pos).title;
	}

	@Override
	public int getCount() {
		return mPages.size();
	}

	@Override
	public void startUpdate(View container) {}

	@Override
	public Object instantiateItem(View container, int position) {
		return container.findViewById(mPages.get(position).view);
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
