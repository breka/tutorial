package org.imogene.android.widget.field.edit;

import org.imogene.android.W;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class BooleanFieldEdit extends BaseFieldEdit<Boolean> implements OnClickListener {

	public BooleanFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default_divider);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setOnClickListener(readOnly ? null : this);
		setOnLongClickListener(readOnly ? null : this);
	}
	
	@Override
	public String getDisplay() {
		final Boolean bool = getValue();
		if (bool != null) {
			String[] array = getResources().getStringArray(W.array.select_yes_no);
			return bool.booleanValue() ? array[0] : array[1];
		} else {
			return getResources().getString(W.string.select);
		}
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Boolean b = getValue();
		return b != null ? b.booleanValue() == Boolean.parseBoolean(value) : false;
	}
	
	@Override
	protected void dispatchClick(View v) {
		showDialog(null);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		final Boolean init = getValue();
		builder.setSingleChoiceItems(W.array.select_yes_no, init != null ? (init ? 0 : 1) : -1, this);
		builder.setNeutralButton(android.R.string.cut, this);
		builder.setNegativeButton(android.R.string.cancel, null);
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case 0:
			setValue(true);
			dialog.dismiss();
			break;
		case 1:
			setValue(false);
			dialog.dismiss();
			break;
		case Dialog.BUTTON_NEUTRAL:
			setValue(null);
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
		
		private Boolean value;

		public SavedState(Parcel source) {
			super(source);
			 value = (Boolean) source.readValue(null);
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
