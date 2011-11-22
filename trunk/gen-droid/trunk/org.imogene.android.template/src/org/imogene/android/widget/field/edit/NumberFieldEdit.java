package org.imogene.android.widget.field.edit;

import org.imogene.android.W;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public abstract class NumberFieldEdit<T extends Number> extends BaseFieldEdit<T> implements TextWatcher {
	
	private TextView mUnitView;
	
	private T mMin;
	private T mMax;
	
	public NumberFieldEdit(Context context, AttributeSet attrs, int layoutId) {
		super(context, attrs, layoutId);

		mUnitView = (TextView) findViewById(W.id.unit);
		if (mUnitView != null) {
			mUnitView.setSaveEnabled(false);
		}
		
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.NumberField, 0, 0);
		setUnitId(a.getResourceId(W.styleable.NumberField_unit, -1));
		a.recycle();
	}
	
	public void setMin(T min) {
		mMin = min;
	}
	
	public T getMin() {
		return mMin;
	}
	
	public void setMax(T max) {
		mMax = max;
	}
	
	public T getMax() {
		return mMax;
	}
	
	@Override
	public void setTitleId(int titleId) {
		super.setTitleId(titleId);
		getValueView().setHint(titleId);
	}
	
	public void setUnitId(int unitId) {
		if (mUnitView != null) {
			if (unitId > 0) {
				mUnitView.setText(unitId);
			}
			mUnitView.setVisibility(unitId > 0 ? View.VISIBLE : View.GONE);
		}
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		final TextView v = getValueView();
		if (readOnly) {
			v.removeTextChangedListener(this);
			v.setEnabled(false);
			v.setInputType(InputType.TYPE_NULL);
		} else {
			v.addTextChangedListener(this);
			v.setEnabled(true);
			v.setInputType(getInputType());
		}
	}
	
	protected abstract int getInputType();
	
	@Override
	public String getDisplay() {
		final T value = getValue();
		return value != null ? value.toString() : null;
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// Don't care
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Don't care
	}
	
}
