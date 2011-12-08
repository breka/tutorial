package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class NumberFieldView<T extends Number> extends DefaultEntityView<T> {

	private final String format;
	
	public NumberFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.NumberField, 0, 0);
		format = a.getString(W.styleable.NumberField_format);
		a.recycle();
	}
	
	@Override
	public String getDisplay() {
		final T number = getValue();
		if (number != null) {
			return String.format(format, number);
		}
		return null;
	}
	
}
