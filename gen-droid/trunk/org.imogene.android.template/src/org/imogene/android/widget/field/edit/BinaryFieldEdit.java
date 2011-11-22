package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.widget.field.FieldManager;
import org.imogene.android.widget.field.FieldManager.OnActivityResultListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class BinaryFieldEdit extends BaseFieldEdit<Uri> implements OnActivityResultListener {

	private int mRequestCode;
	
	public BinaryFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_edit_buttons);
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
	
	protected int displayId() {
		return W.string.bin_binary;
	}
	
	@Override
	public String getDisplay() {
		return getValue() != null ? getResources().getString(displayId()) : getEmptyText();
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final Uri uri = getValue();
		if (uri == null) {
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
			acquire(mRequestCode);
			break;
		case W.id.delete:
			delete();
			break;
		case W.id.view:
			view();
			break;
		}
	}
	
	protected void acquire(int requestCode) {
		Intent acquire = new Intent(Intent.ACTION_GET_CONTENT);
		onCreateIntent(acquire);
		getFieldManager().getActivity().startActivityForResult(acquire, mRequestCode);
	}
	
	protected void onCreateIntent(Intent acquire) {
		acquire.addCategory(Intent.CATEGORY_OPENABLE);
		acquire.setType("*/*");
	}
	
	protected void delete() {
		setValue(null);
	}
	
	protected void view() {
		Intent show = new Intent(Intent.ACTION_VIEW, getValue());
		getContext().startActivity(show);
	}
	
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == mRequestCode && resultCode != Activity.RESULT_CANCELED) {
			setValue(data.getData());
			return true;
		}
		return false;
	}
	
}
