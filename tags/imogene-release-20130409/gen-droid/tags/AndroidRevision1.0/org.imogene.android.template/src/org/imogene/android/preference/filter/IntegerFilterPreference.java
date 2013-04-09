package org.imogene.android.preference.filter;

import org.imogene.android.W;
import org.imogene.android.common.filter.IntegerFilter;
import org.imogene.android.util.FormatHelper;

import android.content.Context;
import android.util.AttributeSet;

public class IntegerFilterPreference extends NumberFilterPreference<Integer, IntegerFilter> {
	
	public IntegerFilterPreference(Context context, AttributeSet attrs) {
		super(context, attrs, IntegerFilter.FILTER_CREATOR);
		setDialogLayoutResource(W.layout.dialog_integer_filter);
	}

	@Override
	protected Integer parseNumber(String str) {
		return FormatHelper.toInteger(str);
	}
}
