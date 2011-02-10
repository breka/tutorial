package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class DefaultEntityView<T> extends FieldEntityView<T> {

	public DefaultEntityView(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.default_field_entity);
		setOnClickListener(this);
	}
	
	public DefaultEntityView(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs, layoutId);
		setOnClickListener(this);
	}
	
	@Override
	protected void dispatchClick(View v) {
		showDialog(null);
	}
	
	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage(getDisplay());
		builder.setPositiveButton(android.R.string.ok, null);
	}

}
