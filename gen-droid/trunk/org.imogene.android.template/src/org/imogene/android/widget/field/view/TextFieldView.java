package org.imogene.android.widget.field.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

public class TextFieldView extends DefaultEntityView<String> {
	
	public TextFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected boolean isEmpty() {
		return TextUtils.isEmpty(getValue());
	}
	
	@Override
	public String getDisplay() {
		return getValue();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final String str = getValue();
		return str != null ? str.matches(value) : false;
	}

}
