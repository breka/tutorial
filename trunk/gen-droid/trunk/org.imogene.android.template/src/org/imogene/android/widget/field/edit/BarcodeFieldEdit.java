package org.imogene.android.widget.field.edit;

import org.imogene.android.Constants.Intents;
import org.imogene.android.template.R;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class BarcodeFieldEdit extends BaseFieldEdit<String> implements OnActivityResultListener {
	
	private int mRequestCode;

	public BarcodeFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_edit_buttons);
		findViewById(R.id.acquire).setOnClickListener(this);
		findViewById(R.id.delete).setOnClickListener(this);
		findViewById(R.id.view).setOnClickListener(this);
	}
	
	@Override
	public boolean isEmpty() {
		return TextUtils.isEmpty(getValue());
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		findViewById(R.id.acquire).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(R.id.delete).setVisibility(readOnly ? View.GONE : View.VISIBLE);
		findViewById(R.id.view).setVisibility(readOnly ? View.GONE : View.VISIBLE);
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
			return getEmptyText();
		} else {
			return value;
		}
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final String value = getValue();
		if (TextUtils.isEmpty(value)) {
			findViewById(R.id.acquire).setVisibility(View.VISIBLE);
			findViewById(R.id.delete).setVisibility(View.GONE);
			findViewById(R.id.view).setVisibility(View.GONE);
		} else {
			findViewById(R.id.acquire).setVisibility(View.GONE);
			findViewById(R.id.delete).setVisibility(View.VISIBLE);
			findViewById(R.id.view).setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case R.id.acquire:
			Intent intent = new Intent(Intents.ACTION_SCAN);
			getFieldManager().getActivity().startActivityForResult(intent, mRequestCode);
			break;
		case R.id.delete:
			setValue(null);
			break;
		case R.id.view:
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
	
}
