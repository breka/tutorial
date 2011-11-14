package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class BinaryFieldView extends BaseFieldView<Uri> {

	public BinaryFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default_divider);
		setOnClickListener(this);
	}
	
	@Override
	protected void dispatchClick(View v) {
		if (getValue() != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW, getValue());
			getContext().startActivity(intent);
		}
	}
	
	protected int getDisplayId() {
		return W.string.bin_binary;
	}
	
	@Override
	protected boolean isEmpty() {
		return getValue() == null;
	}
	
	@Override
	public String getDisplay() {
		if (getValue() != null) {
			return getResources().getString(getDisplayId());			
		}
		return super.getDisplay();
	}
	

}
