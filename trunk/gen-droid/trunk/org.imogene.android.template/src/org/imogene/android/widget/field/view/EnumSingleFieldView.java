package org.imogene.android.widget.field.view;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class EnumSingleFieldView extends DefaultEntityView<Integer> {

	private final CharSequence[] mItems;
	private final int[] mItemsValues;
	
	public EnumSingleFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.EnumField, 0, 0);
		mItems = a.getTextArray(W.styleable.EnumField_entries);
		mItemsValues = getResources().getIntArray(a.getResourceId(W.styleable.EnumField_array, 0));
		a.recycle();
	}
	
	@Override
	public boolean isEmpty() {
		final Integer i = getValue();
		return i == null || i == -1;
	}
	
	@Override
	public String getDisplay() {
		final Integer value = getValue();
		if (value != null) {
			final int intValue = value.intValue();
			if (intValue != -1) {
				return FormatHelper.displayEnumSingle(mItems, mItemsValues, intValue);
			}
		}
		return super.getDisplay();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Integer i = getValue();
		if (i == null)
			return false;
		
		return i.toString().matches(value);
	}

}
