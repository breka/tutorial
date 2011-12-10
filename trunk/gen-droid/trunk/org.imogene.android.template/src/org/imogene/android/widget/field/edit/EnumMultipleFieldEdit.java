package org.imogene.android.widget.field.edit;

import java.util.Arrays;

import org.imogene.android.template.R;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.EnumConverter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;

public class EnumMultipleFieldEdit extends BaseFieldEdit<boolean[]> implements OnMultiChoiceClickListener, android.content.DialogInterface.OnClickListener {

	private final int mEntries;
	private final int mArray;
	private final int mSize;
	
	public EnumMultipleFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_default);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EnumField, 0, 0);
		mEntries = a.getResourceId(R.styleable.EnumField_entries, 0);
		mArray = a.getResourceId(R.styleable.EnumField_array, 0);
		mSize = a.getInteger(R.styleable.EnumField_size, 0);
		a.recycle();
		setValue(new boolean[mSize]);
	}
	
	@Override
	public boolean isEmpty() {
		final boolean[] value = getValue();
		return value == null || Arrays.equals(value, new boolean[value.length]);
	}
	
	@Override
	public boolean isValid() {
		final boolean[] value = getValue();
		if (isRequired()) {
			return value != null && !Arrays.equals(value, new boolean[value.length]);
		}
		return true;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setOnClickListener(readOnly ? null : this);
		setOnLongClickListener(readOnly ? null : this);
	}
	
	@Override
	public void setValue(boolean[] value) {
		if (value == null) {
			super.setValue(new boolean[mSize]);
		} else {
			super.setValue(value);
		}
	}
	
	@Override
	public String getDisplay() {
		boolean[] value = getValue();
		if (value == null || Arrays.equals(value, new boolean[mSize])) {
			return getEmptyText();
		} else {
			return FormatHelper.displayEnumMulti(getResources().getStringArray(mEntries), value);
		}
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final boolean[] array = getValue();
		if (array == null)
			return false;
		
		return EnumConverter.convert(getContext(), mArray, array).matches(value);
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setMultiChoiceItems(mEntries, getValue() != null ? getValue().clone() : null, this);
		builder.setPositiveButton(android.R.string.ok, this);
		builder.setNeutralButton(android.R.string.cut, this);
		builder.setNegativeButton(android.R.string.cancel, this);
	}
	
	@Override
	public void dispatchClick(View v) {
		showDialog(null);
	}
	
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		// nothing to do, is necessary for initial checked items to be unselected
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case Dialog.BUTTON_POSITIVE:
			final SparseBooleanArray sparse = ((AlertDialog) dialog).getListView().getCheckedItemPositions();
			boolean[] result = new boolean[mSize];
			for (int i = 0; i < mSize; i++)
				result[i] = sparse.get(i);
			setValue(result);
			break;
		case Dialog.BUTTON_NEUTRAL:
			setValue(new boolean[mSize]);
			break;
		}
	}
	
}
