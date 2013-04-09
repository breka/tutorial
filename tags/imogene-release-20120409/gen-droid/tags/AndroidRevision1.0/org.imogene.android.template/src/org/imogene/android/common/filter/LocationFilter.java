package org.imogene.android.common.filter;

import java.util.ArrayList;

import org.imogene.android.common.ClientFilter;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.imogene.android.util.BoundingBox;

public class LocationFilter extends ClientFilter {
	
	public static final Creator<LocationFilter> FILTER_CREATOR = new DefaultCreator<LocationFilter>() {
		
		@Override
		protected LocationFilter newFilter() {
			return new LocationFilter();
		}
		
		@Override
		protected LocationFilter newFilter(ClientFilterCursor c) {
			return new LocationFilter(c);
		}
	};
	
	private ArrayList<BoundingBox> mBoxes;
	
	private LocationFilter() {
		super();
		init();
	}
	
	private LocationFilter(ClientFilterCursor c) {
		super(c);
		init();
	}
	
	private void init() {
		mBoxes = BoundingBox.fromString(getFieldValue());
	}
	
	@Override
	protected void preCommit() {
		super.preCommit();
		setOperator(GEOFILTER_OPERATOR);
		setFieldValue(BoundingBox.toString(mBoxes));
	}
	
	public ArrayList<BoundingBox> getBoxes() {
		return mBoxes;
	}
	
	public void setBoxes(ArrayList<BoundingBox> boxes) {
		if (mBoxes != null) {
			mBoxes.clear();
		}
		mBoxes = boxes;
	}
}
