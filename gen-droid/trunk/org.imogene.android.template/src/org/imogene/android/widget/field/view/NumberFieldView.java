package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class NumberFieldView<T extends Number> extends DefaultEntityView<T> {

	public NumberFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.IntegerFieldEdit, 0, 0);
		final int unitId = a.getResourceId(W.styleable.IntegerFieldEdit_unit, -1);
		a.recycle();
		if (unitId != -1) {
			final View unit = findViewById(W.id.unit);
			if (unit != null && unit instanceof TextView) {
				((TextView) unit).setText(unitId);
			}
		}
		
	}

	@Override
	protected boolean isEmpty() {
		return getValue() == null;
	}
	
	@Override
	public String getDisplay() {
		final T number = getValue();
		return number != null ? number.toString() : null;
	}
	
}
