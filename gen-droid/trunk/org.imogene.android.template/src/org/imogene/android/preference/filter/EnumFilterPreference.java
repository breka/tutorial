package org.imogene.android.preference.filter;

import java.util.Arrays;

import org.imogene.android.template.R;
import org.imogene.android.common.filter.EnumFilter;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.EnumConverter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;

public class EnumFilterPreference extends FilterPreference<EnumFilter> implements OnMultiChoiceClickListener {

	private final boolean mIsMulti;
	private final int mEntries;
	private final int mSize;
	private final int mArray;
	
	public EnumFilterPreference(Context context, AttributeSet attrs) {
		super(context, attrs, EnumFilter.FILTER_CREATOR);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EnumFilterPreference, 0, 0);
		mIsMulti = a.getBoolean(R.styleable.EnumFilterPreference_igFilterMultiple, false);
		mEntries = a.getResourceId(R.styleable.EnumFilterPreference_igFilterEntries, 0);		
		mSize = a.getInteger(R.styleable.EnumFilterPreference_igFilterSize, 0);
		mArray = a.getResourceId(R.styleable.EnumFilterPreference_igFilterArray, 0);
		a.recycle();
		
		if (mSize == 0 || mEntries == 0 || mArray == 0)
			throw new IllegalStateException(
					 "EnumCriteriaPreference requires an entries array and a valid size.");
	}

	@Override
	public CharSequence getSummary() {
		boolean[] checkedItems = EnumConverter.parseMulti(getContext(), mArray, getFilter().getFieldValue());
		if (checkedItems != null && !Arrays.equals(checkedItems, new boolean[mSize]))
			return FormatHelper.displayEnumMulti(getContext().getResources().getStringArray(mEntries), checkedItems);
		return getContext().getString(android.R.string.unknownName);
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);
		boolean[] checkedItems = EnumConverter.parseMulti(getContext(), mArray, getFilter().getFieldValue());
		builder.setMultiChoiceItems(mEntries, checkedItems, this);
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if (which == DialogInterface.BUTTON_POSITIVE) {
			SparseBooleanArray sparse = ((AlertDialog) dialog).getListView().getCheckedItemPositions();
			boolean[] result = new boolean[mSize];
			for (int i = 0; i < mSize; i++)
				result[i] = sparse.get(i);
			EnumFilter filter = getFilter();
			filter.setMultipleSelectable(mIsMulti);
			filter.setFieldValue(EnumConverter.convert(getContext(), mArray, result));
			persistFilter();
		}
	}
	
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		// nothing to do, is necessary for initial checked items to be unselected
	}
    
}
