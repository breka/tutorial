package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

public class NumberFieldView<T extends Number> extends DefaultEntityView<T> {

	private final String unit;
	
	public NumberFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.NumberField, 0, 0);
		unit = a.getString(W.styleable.NumberField_unit);
		a.recycle();
	}
	
	@Override
	public String getDisplay() {
		final T number = getValue();
		if (number != null) {
			if (TextUtils.isEmpty(unit)) {
				return number.toString();
			} else {
				return number + " " + unit;
			}
		}
		return null;
	}
	
}
