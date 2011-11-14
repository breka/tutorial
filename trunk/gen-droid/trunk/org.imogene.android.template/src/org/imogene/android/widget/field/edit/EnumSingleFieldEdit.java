package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.util.field.EnumConverter;
import org.imogene.android.widget.field.FieldManager.OnActivityDestroyListener;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class EnumSingleFieldEdit extends BaseFieldEdit<Integer> implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, OnActivityDestroyListener {
	
	private final int mEntries;
	private final int mArray;
	
	public EnumSingleFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.EnumSingleFieldEdit, 0, 0);
		mEntries = a.getResourceId(W.styleable.EnumSingleFieldEdit_entries, 0);
		mArray = a.getResourceId(W.styleable.EnumSingleFieldEdit_array, 0);
		a.recycle();
		setValue(-1);
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
			return getResources().getString(W.string.select);
		} else {
			final int intValue = value.intValue();
			if (intValue == -1) {
				return getResources().getString(W.string.select);
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
	public boolean isValid() {
		return isRequired() ? getValue().intValue() != -1 : true;
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
	
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState myState = new SavedState(superState);
		myState.value = getValue();
		return myState;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		setValue(myState.value);
	}
	
	private static class SavedState extends BaseSavedState {
		
		private Integer value;

		public SavedState(Parcel source) {
			super(source);
			 value = (Integer) source.readValue(null);
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeValue(value);
		}
		
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
			
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}
		};
		
		
	}

}
