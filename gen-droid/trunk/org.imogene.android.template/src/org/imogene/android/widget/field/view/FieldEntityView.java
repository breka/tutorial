package org.imogene.android.widget.field.view;

import org.imogene.android.widget.field.FieldEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class FieldEntityView<T> extends FieldEntity<T> {

	public FieldEntityView(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs, layoutId);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		mReadOnly = readOnly;
	}
	
	protected boolean isEmpty() {
		return false;
	}
	
	@Override
	protected void onChangeValue() {
		super.onChangeValue();
		final boolean visible = !isEmpty() && isDependentVisible();
		setVisibility(visible ? View.VISIBLE : View.GONE);
	}

}
