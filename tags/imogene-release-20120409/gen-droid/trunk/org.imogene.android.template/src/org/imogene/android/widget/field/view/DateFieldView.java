package org.imogene.android.widget.field.view;

import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;

import android.content.Context;
import android.util.AttributeSet;

public class DateFieldView extends DefaultEntityView<Long> {

	public DateFieldView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public String getDisplay() {
		final Long time = getValue();
		if (time != null) {
			return FormatHelper.displayAsDate(time.longValue());
		}
		return super.getDisplay();
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Long date = getValue();
		return date != null ? FieldPattern.matchesDate(value, date.longValue()) : false;
	}

}
