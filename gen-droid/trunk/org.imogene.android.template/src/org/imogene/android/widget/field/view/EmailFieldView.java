package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class EmailFieldView extends BaseFieldView<String> {

	public EmailFieldView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_default_divider);
		setOnClickListener(this);
		setIconId(android.R.drawable.sym_action_email);
	}
	
	@Override
	protected void dispatchClick(View v) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getValue(), null));
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
