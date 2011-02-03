package org.imogene.android.widget.field.edit;

import java.util.Calendar;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

public class DateFieldEdit extends DatesFieldEdit implements OnDateSetListener {

	public DateFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public String getDisplay() {
		final Long time = getValue();
		if (time != null) {
			return FormatHelper.displayAsDate(time.longValue());
		} else {
			return getResources().getString(W.string.select);
		}
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Long date = getValue();
		return date != null ? FieldPattern.matchesDate(value, date.longValue()) : false;
	}
	
	public Dialog createDialog() {
		final Calendar cal = Calendar.getInstance();
		final Long time = getValue();
		if (time != null) {
			cal.setTimeInMillis(time.longValue());
		}
		return new DatePickerDialog(
				getContext(), 
				this, 
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH));
	}
	
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		final Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth);
		setValue(cal.getTimeInMillis());
	}

}
