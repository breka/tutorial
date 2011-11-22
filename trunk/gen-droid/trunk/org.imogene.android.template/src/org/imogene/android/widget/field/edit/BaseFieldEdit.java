package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.widget.ErrorAdapter.ErrorEntry;
import org.imogene.android.widget.field.BaseField;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

public abstract class BaseFieldEdit<T> extends BaseField<T> {
	
	public interface OnValueChangeListener {
		public void onValueChange(int id);
	}
	
	private final View mRequiredView;
	private final View mHelpView;

	private boolean mNotifyValueChanged = true;
	private boolean mUpdateDisplayOnChange = true;
	private boolean mAutomaticVisibility = true;
	private boolean mReadOnly;
	private boolean mRequired;
	private int mHelpId;
	
	private Dialog mHelpDialog;
	private OnValueChangeListener mListener;

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
	
	public void init(T value) {
		disableNotifyValueChanged();
		setValue(value);
		enableNotifyValueChanged();
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
	
	@Override
	public boolean isEmpty() {
		return getValue() == null;
	}
	
	public boolean isValid() {
		return mRequired ? !isEmpty() : true;
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
	
	public ErrorEntry getErrorEntry(int tag) {
		ErrorEntry entry = new ErrorEntry();
		entry.setField(getId());
		entry.setTag(tag);
		entry.setTitle(getTitleId());
		if (mRequired) {
			entry.addMessage(getResources().getString(W.string.is_required));
		}
		return entry;
	}
	
	public void setAutomaticManageVisibility(boolean automatic) {
		mAutomaticVisibility = automatic;
	}
	
	public void setOnValueChangeListener(OnValueChangeListener listener) {
		mListener = listener;
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
	
	private void enableNotifyValueChanged() {
		if (!mNotifyValueChanged) {
			mNotifyValueChanged = true;
		}
	}
	
	private void disableNotifyValueChanged() {
		if (mNotifyValueChanged) {
			mNotifyValueChanged = false;
		}
	}
	
	protected boolean notifyValueChangedEnabled() {
		return mNotifyValueChanged;
	}
	
	protected void onChangeValue() {
		if (mUpdateDisplayOnChange) {
			super.onChangeValue();
		}
		if (mNotifyValueChanged) {
			notifyValueChange();
		}
		if (mRequired && mRequiredView != null) {
			mRequiredView.setVisibility(isEmpty() ? View.VISIBLE : View.GONE);
		}
	}
	
	private void notifyValueChange() {
		if (mListener != null) {
			mListener.onValueChange(getId());
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
	
}
