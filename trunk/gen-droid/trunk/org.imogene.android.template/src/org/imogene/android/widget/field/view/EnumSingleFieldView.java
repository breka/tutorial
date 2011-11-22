package org.imogene.android.widget.field.view;

import org.imogene.android.W;
import org.imogene.android.util.field.EnumConverter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class EnumSingleFieldView extends DefaultEntityView<Integer> {

	private final int mEntries;
	private final int mArray;
	
	public EnumSingleFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.EnumField, 0, 0);
		mEntries = a.getResourceId(W.styleable.EnumField_entries, 0);
		mArray = a.getResourceId(W.styleable.EnumField_array, 0);
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
				String[] array = getResources().getStringArray(mEntries);
				return array[intValue];
			}			
		}
		return super.getDisplay();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Integer i = getValue();
		if (i == null)
			return false;
		
		return EnumConverter.convert(getContext(), mArray, i.intValue()).matches(value);
	}

}
