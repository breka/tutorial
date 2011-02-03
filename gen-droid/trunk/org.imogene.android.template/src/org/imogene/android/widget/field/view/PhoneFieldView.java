package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class PhoneFieldView extends FieldEntityView<String> {

	public PhoneFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.default_field_entity);
		setOnClickListener(this);
		setIconId(android.R.drawable.sym_action_call);
	}
	
	@Override
	protected void dispatchClick(View v) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", getValue(), null));
		getContext().startActivity(intent);
	}
	
	@Override
	protected boolean isEmpty() {
		return TextUtils.isEmpty(getValue());
	}
	
	@Override
	protected String getDisplay() {
		return getValue();
	}

}
