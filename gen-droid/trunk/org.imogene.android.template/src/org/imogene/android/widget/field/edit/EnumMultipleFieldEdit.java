package org.imogene.android.widget.field.edit;

import java.util.Arrays;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.EnumConverter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;

public class EnumMultipleFieldEdit extends BaseFieldEdit<boolean[]> implements OnMultiChoiceClickListener, android.content.DialogInterface.OnClickListener{

	private final int mEntries;
	private final int mArray;
	private final int mSize;
	
	public EnumMultipleFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.EnumMultipleFieldEdit, 0, 0);
		mEntries = a.getResourceId(W.styleable.EnumMultipleFieldEdit_entries, 0);
		mArray = a.getResourceId(W.styleable.EnumMultipleFieldEdit_array, 0);
		mSize = a.getInteger(W.styleable.EnumMultipleFieldEdit_size, 0);
		a.recycle();
		setValue(new boolean[mSize]);
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
	public boolean isValid() {
		final boolean[] value = getValue();
		if (isRequired())
			if (value == null)
				return false;
			else
				return !Arrays.equals(value, new boolean[value.length]);
		return true;
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
		
		private boolean[] value;

		public SavedState(Parcel source) {
			super(source);
			value = source.createBooleanArray();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeBooleanArray(value);
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
