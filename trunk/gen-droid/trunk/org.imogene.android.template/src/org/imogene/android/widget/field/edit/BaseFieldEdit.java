package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.widget.field.BaseField;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class BaseFieldEdit<T> extends BaseField<T> {
	
	private final View mRequiredView;
	private final View mHelpView;

	private boolean mUpdateDisplayOnChange = true;
	private boolean mAutomaticVisibility = true;
	private boolean mReadOnly;
	private boolean mRequired;
	private int mHelpId;
	
	private Dialog mHelpDialog;

	public BaseFieldEdit(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs, layoutId);
		
		mHelpView = findViewById(W.id.help);
		if (mHelpView != null) {
			mHelpView.setSaveEnabled(false);
		}

		mRequiredView = findViewById(W.id.required);
		if (mRequiredView != null) {
			mRequiredView.setSaveEnabled(false);
		}
		
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.BaseFieldEdit, 0, 0);
		setHelpId(a.getResourceId(W.styleable.BaseFieldEdit_help, 0));
		setReadOnly(a.getBoolean(W.styleable.BaseFieldEdit_readOnly, false));
		setRequired(a.getBoolean(W.styleable.BaseFieldEdit_required, false));
		a.recycle();
	}
	
	public boolean isValid() {
		return mRequired ? getValue() != null : true;
	}
	
	public String getErrorMessage() {
		if (mRequired)
			return getResources().getString(W.string.is_required);
		return null;
	}

	public void setRequired(boolean required) {
		mRequired = required;
		if (mRequiredView != null) {
			mRequiredView.setVisibility(required ? View.VISIBLE : View.GONE);
		}
	}
	
	public boolean isRequired() {
		return mRequired;
	}
	
	
	public void setReadOnly(boolean readOnly) {
		mReadOnly = readOnly;
		setEnabled(!readOnly);
	}
	
	public boolean isReadOnly() {
		return mReadOnly;
	}
	
	public void setHelpId(int helpId) {
		mHelpId = helpId;
		if (mHelpView != null) {
			mHelpView.setOnClickListener(helpId > 0 ? this : null);
			mHelpView.setVisibility(helpId > 0 ? View.VISIBLE : View.GONE);			
		}
	}
	
	public int getHelpId() {
		return mHelpId;
	}
	
	public void setAutomaticManageVisibility(boolean automatic) {
		mAutomaticVisibility = automatic;
	}
	
	@Override
	public void onDependencyChanged() {
		if (!mAutomaticVisibility) {
			return;
		}
		super.onDependencyChanged();
	}
	
	protected void enableUpdateDisplayOnChange() {
		if (!mUpdateDisplayOnChange) {
			mUpdateDisplayOnChange = true;
		}
	}
	
	protected void disableUpdateDisplayOnChange() {
		if (mUpdateDisplayOnChange) {
			mUpdateDisplayOnChange = false;
		}
	}
	
	protected void onChangeValue() {
		if (mUpdateDisplayOnChange) {
			super.onChangeValue();
		}
		if (mRequired && mRequiredView != null) {
			mRequiredView.setVisibility(isValid() ? View.GONE : View.VISIBLE);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == W.id.help) {
			showHelpDialog(null);
		} else {
			dispatchClick(v);
		}
	}
	
	private void showHelpDialog(Bundle state) {
		Builder builder = new AlertDialog.Builder(getContext())
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle(getTitleId())
		.setMessage(mHelpId)
		.setPositiveButton(android.R.string.ok, null);
		
		getFieldManager().registerOnActivityDestroyListener(this);
		
		final Dialog dialog = mHelpDialog = builder.create();
		if (state != null) {
			dialog.onRestoreInstanceState(state);
		}
		dialog.setOnDismissListener(this);
		dialog.show();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if (dialog.equals(mHelpDialog)) {
			mHelpDialog = null;
		}
	}
	
	@Override
	public void onActivityDestroy() {
		super.onActivityDestroy();
		if (mHelpDialog != null && mHelpDialog.isShowing()) {
			mHelpDialog.dismiss();
		}
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final boolean hasHelpDialog = mHelpDialog != null && mHelpDialog.isShowing();
		if (!hasHelpDialog) {
			return superState;
		}
		
		final SavedState myState = new SavedState(superState);
		myState.isHelpDialogShowing = hasHelpDialog;
		myState.helpDialogBundle = hasHelpDialog ? mHelpDialog.onSaveInstanceState() : null;
		return myState;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}
		
		final SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		if (myState.isHelpDialogShowing) {
			showHelpDialog(myState.helpDialogBundle);
		}
	}
	
	private static class SavedState extends BaseSavedState {
		
		private boolean isHelpDialogShowing;
		private Bundle helpDialogBundle;

		public SavedState(Parcel source) {
			super(source);
			isHelpDialogShowing = source.readInt() == 1;
			helpDialogBundle = source.readBundle();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(isHelpDialogShowing ? 1 : 0);
			dest.writeBundle(helpDialogBundle);
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
