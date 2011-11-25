package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.util.field.EnumConverter;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

public class EnumSingleFieldEdit extends BaseFieldEdit<Integer> implements DialogInterface.OnClickListener {
	
	private final int mEntries;
	private final int mArray;
	
	public EnumSingleFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.EnumField, 0, 0);
		mEntries = a.getResourceId(W.styleable.EnumField_entries, 0);
		mArray = a.getResourceId(W.styleable.EnumField_array, 0);
		a.recycle();
		setValue(-1);
	}
	
	@Override
	public boolean isEmpty() {
		final Integer i = getValue();
		return i == null || i == -1;
	}
	
	@Override
	public boolean isValid() {
		Integer value = getValue();
		if (isRequired()) {
			return value != null && value.intValue() != -1;
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
	public void setValue(Integer value) {
		if (value == null) {
			super.setValue(-1);
		} else {
			super.setValue(value);
		}
	}
	
	@Override
	public String getDisplay() {
		final Integer value = getValue();
		if (value == null) {
			return getEmptyText();
		} else {
			final int intValue = value.intValue();
			if (intValue == -1) {
				return getEmptyText();
			} else {
				String[] array = getResources().getStringArray(mEntries);
				return array[intValue];
			}
		}
	}
	
	@Override
	public Integer getValue() {
		final Integer superValue = super.getValue();
		if (superValue == null)
			return -1;
		return superValue;
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Integer i = getValue();
		if (i == null)
			return false;
		
		return EnumConverter.convert(getContext(), mArray, i.intValue()).matches(value);
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setSingleChoiceItems(mEntries, getValue() != null ? getValue().intValue() : -1, this);
		builder.setNeutralButton(android.R.string.cut, this);
		builder.setNegativeButton(android.R.string.cancel, null);
	}
	
	@Override
	public void dispatchClick(View v) {
		showDialog(null);
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case Dialog.BUTTON_NEUTRAL:
			setValue(-1);
			break;
		default:
			if (which > -1) {
				setValue(which);
				dialog.dismiss();
			}
			break;
		}
	}
	
}
