package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class NumberFieldView<T extends Number> extends DefaultEntityView<T> {

	private final String unit;
	private final int unitId;
	
	public NumberFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.NumberFieldView, 0, 0);
		unitId = a.getResourceId(W.styleable.NumberFieldView_unit, -1);
		a.recycle();
		
		if (unitId != -1) {
			unit = context.getString(unitId);
		} else {
			unit = null;
		}
	}

	@Override
	protected boolean isEmpty() {
		return getValue() == null;
	}
	
	@Override
	public String getDisplay() {
		final T number = getValue();
		if (unitId != -1) {
			return number != null ? number.toString() + " " + unit : null;
		}
		return number != null ? number.toString() : null;
	}
	
}
