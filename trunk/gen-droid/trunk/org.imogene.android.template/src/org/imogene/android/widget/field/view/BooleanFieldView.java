package org.imogene.android.widget.field.view;

import org.imogene.android.W;

import android.content.Context;
import android.util.AttributeSet;

public class BooleanFieldView extends DefaultEntityView<Boolean> {

	public BooleanFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public String getDisplay() {
		final Boolean bool = getValue();
		if (bool != null) {
			String[] array = getResources().getStringArray(W.array.select_yes_no);
			return bool.booleanValue() ? array[0] : array[1];
		}
		return super.getDisplay();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Boolean b = getValue();
		return b != null ? b.booleanValue() == Boolean.parseBoolean(value) : false;
	}
}
