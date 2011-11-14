package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.widget.field.BaseField.DialogFactory;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public abstract class DatesFieldEdit extends BaseFieldEdit<Long> implements DialogFactory {
	
	private Long mMin;
	private Long mMax;

	public DatesFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default);
		setDialogFactory(this);
	}
	
	public void setMin(Long min) {
		mMin = min;
	}
	
	public Long getMin() {
		return mMin;
	}
	
	public void setMax(Long max) {
		mMax = max;
	}
	
	public Long getMax() {
		return mMax;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setOnClickListener(readOnly ? null : this);
		setOnLongClickListener(readOnly ? null : this);
	}
	
	@Override
	protected void dispatchClick(View v) {
		showDialog(null);
	}
	
	@Override
	public boolean isValid() {
		final Long value = getValue();
		if (value == null) {
			return !isRequired();
		} else {
			if (mMin != null && mMin > value)
				return false;
			if (mMax != null && mMax < value)
				return false;
			return true;
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
		
		private Long value;

		public SavedState(Parcel source) {
			super(source);
			 value = (Long) source.readValue(null);
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
