package org.imogene.android.widget.field.view;

import java.util.Arrays;

import org.imogene.android.template.R;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.EnumConverter;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class EnumMultipleFieldView extends DefaultEntityView<boolean[]> {

	private final String[] mItems;
	private final int[] mItemsValues;

	public EnumMultipleFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EnumField, 0, 0);
		mItems = getResources().getStringArray(a.getResourceId(R.styleable.EnumField_igItems, 0));
		mItemsValues = getResources().getIntArray(a.getResourceId(R.styleable.EnumField_igItemsValues, 0));
		a.recycle();
	}
	
	@Override
	public boolean isEmpty() {
		final boolean[] value = getValue();
		return value == null || Arrays.equals(value, new boolean[value.length]);
	}
	
	@Override
	public String getDisplay() {
		boolean[] value = getValue();
		if (value != null && !Arrays.equals(value, new boolean[value.length])) {
			return FormatHelper.displayEnumMulti(mItems, value);
		}
		return super.getDisplay();
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setIcon(android.R.drawable.ic_dialog_info);
		final String[] items = getDisplay().split(" ; ");
		builder.setItems(items, null);
		builder.setPositiveButton(android.R.string.ok, null);
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final boolean[] array = getValue();
		if (array == null)
			return false;
		
		return EnumConverter.convert(mItemsValues, array).matches(value);
	}

}
