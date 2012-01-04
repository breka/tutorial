package org.imogene.android.maps.preference;

import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.maps.database.PreCache;
import org.imogene.android.template.R;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class PrecachedMapAreasPreference extends Preference {
	
	private static final String COUNT_SQL = "select count(*) from " + PreCache.Columns.TABLE_NAME;
	
	public PrecachedMapAreasPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PrecachedMapAreasPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public CharSequence getSummary() {
		int count = (int) SQLiteWrapper.queryForLong(getContext(), COUNT_SQL);
		return getContext().getResources().getQuantityString(R.plurals.ig_precache_area_summary, count, count);
	}
}
