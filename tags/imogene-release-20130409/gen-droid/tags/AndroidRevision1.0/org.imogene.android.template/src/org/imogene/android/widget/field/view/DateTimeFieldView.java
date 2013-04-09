package org.imogene.android.widget.field.view;

import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;

import android.content.Context;
import android.util.AttributeSet;

public class DateTimeFieldView extends DefaultEntityView<Long> {

	public DateTimeFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected boolean isEmpty() {
		return getValue() == null;
	}
	
	@Override
	public String getDisplay() {
		final Long time = getValue();
		if (time != null) {
			return FormatHelper.displayAsDateTime(time.longValue());
		}
		return super.getDisplay();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Long date = getValue();
		return date != null ? FieldPattern.matchesDateTime(value, date.longValue()) : false;
	}

}
