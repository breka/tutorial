package org.imogene.android.widget.field.view;

import org.imogene.android.template.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class DefaultEntityView<T> extends BaseFieldView<T> {

	public DefaultEntityView(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_default);
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
