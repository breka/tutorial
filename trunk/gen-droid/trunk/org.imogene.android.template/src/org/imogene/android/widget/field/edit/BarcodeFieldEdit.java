package org.imogene.android.widget.field.edit;

import org.imogene.android.Constants.Intents;
import org.imogene.android.W;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class BarcodeFieldEdit extends BaseFieldEdit<String> implements OnActivityResultListener {
	
	private int mRequestCode;

	public BarcodeFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_edit_buttons_divider);
		findViewById(W.id.acquire).setOnClickListener(this);
		findViewById(W.id.delete).setOnClickListener(this);
		findViewById(W.id.view).setOnClickListener(this);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		findViewById(W.id.acquire).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(W.id.delete).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(W.id.view).setVisibility(readOnly ? View.GONE : View.VISIBLE);
	}
	
	@Override
	public void onAttachedToHierarchy(FieldManager manager) {
		super.onAttachedToHierarchy(manager);
		manager.registerOnActivityResultListener(this);
		mRequestCode = manager.getNextId();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final String str = getValue();
		return str != null ? str.matches(value) : false;
	}
	
	@Override
	public String getDisplay() {
		final String value = getValue();
		if (TextUtils.isEmpty(value)) {
			return getResources().getString(W.string.select);
		} else {
			return value;
		}
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final String value = getValue();
		if (TextUtils.isEmpty(value)) {
			findViewById(W.id.acquire).setVisibility(View.VISIBLE);
			findViewById(W.id.delete).setVisibility(View.GONE);
			findViewById(W.id.view).setVisibility(View.GONE);
		} else {
			findViewById(W.id.acquire).setVisibility(View.GONE);
			findViewById(W.id.delete).setVisibility(View.VISIBLE);
			findViewById(W.id.view).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case W.id.acquire:
			Intent intent = new Intent(Intents.ACTION_SCAN);
			getFieldManager().getActivity().startActivityForResult(intent, mRequestCode);
			break;
		case W.id.delete:
			setValue(null);
			break;
		case W.id.view:
			showDialog(null);
			break;
		}
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage(getValue());
		builder.setPositiveButton(android.R.string.ok, null);
	}
	
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mRequestCode && resultCode != Activity.RESULT_CANCELED) {
			setValue(data.getStringExtra("SCAN_RESULT"));
			return true;
		}
		return false;
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
		
		private String value;

		public SavedState(Parcel source) {
			super(source);
			value = source.readString();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeString(value);
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
