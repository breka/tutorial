package org.imogene.android.widget.field.edit;

import org.imogene.android.template.R;
import org.imogene.android.widget.field.BaseField.DialogFactory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class DatesFieldEdit extends BaseFieldEdit<Long> implements DialogFactory {
	
	private Long mMin;
	private Long mMax;
	
	public DatesFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_default);
		setDialogFactory(this);
	}
	
	public void setMin(Long min) {
		mMin = min;
	}
	
	public Long getMin() {
		return mMin;
	}
	
	public void setMax(Long max) {
		mMax = max;
	}
	
	public Long getMax() {
		return mMax;
	}

	@Override
	public boolean isValid() {
		final Long value = getValue();
		if (value == null) {
			return !isRequired();
		}
		if (mMin != null && mMin > value) {
			return false;
		}
		if (mMax != null && mMax < value) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setOnClickListener(readOnly ? null : this);
		setOnLongClickListener(readOnly ? null : this);
	}
	
	@Override
	protected void dispatchClick(View v) {
		showDialog(null);
	}
	
}
