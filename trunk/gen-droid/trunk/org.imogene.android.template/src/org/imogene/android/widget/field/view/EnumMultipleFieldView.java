package org.imogene.android.widget.field.view;

import java.util.Arrays;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.EnumConverter;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class EnumMultipleFieldView extends DefaultEntityView<boolean[]> {

	private final int mEntries;
	private final int mArray;

	public EnumMultipleFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.EnumMultipleFieldView, 0, 0);
		mEntries = a.getResourceId(W.styleable.EnumMultipleFieldView_entries, 0);
		mArray = a.getResourceId(W.styleable.EnumMultipleFieldView_array, 0);
		a.recycle();
	}
	
	@Override
	protected boolean isEmpty() {
		final boolean[] value = getValue();
		return value == null || Arrays.equals(value, new boolean[value.length]);
	}
	
	@Override
	public String getDisplay() {
		boolean[] value = getValue();
		if (value != null && !Arrays.equals(value, new boolean[value.length])) {
			return FormatHelper.displayEnumMulti(getResources().getStringArray(mEntries), value);
		}
		return getResources().getString(W.string.select);
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
		
		return EnumConverter.convert(getContext(), mArray, array).matches(value);
	}

}
