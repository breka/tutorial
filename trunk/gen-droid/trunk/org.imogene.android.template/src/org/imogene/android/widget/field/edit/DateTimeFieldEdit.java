package org.imogene.android.widget.field.edit;

import java.util.Calendar;

import org.imogene.android.template.R;
import org.imogene.android.app.DateTimePickerDialog;
import org.imogene.android.app.DateTimePickerDialog.OnDateTimeSetListener;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;
import org.imogene.android.widget.DateTimePicker;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class DateTimeFieldEdit extends DatesFieldEdit implements OnDateTimeSetListener {

	public DateTimeFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatesFieldEdit, 0, 0);
		if (a.hasValue(R.styleable.DatesFieldEdit_igDateMin)) {
			String date = a.getString(R.styleable.DatesFieldEdit_igDateMin);
			setMin(FormatHelper.readDateTime(date));
		} else {
			setMin(null);
		}
		if (a.hasValue(R.styleable.DatesFieldEdit_igDateMax)) {
			String date = a.getString(R.styleable.DatesFieldEdit_igDateMax);
			setMax(FormatHelper.readDateTime(date));
		} else {
			setMax(null);
		}
		a.recycle();
	}
	
	@Override
	public String getDisplay() {
		final Long time = getValue();
		if (time != null) {
			return FormatHelper.displayAsDateTime(time.longValue());
		} else {
			return getEmptyText();
		}
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Long date = getValue();
		return date != null ? FieldPattern.matchesDateTime(value, date.longValue()) : false;
	}
	
	public Dialog createDialog() {
		final Calendar cal = Calendar.getInstance();
		final Long time = getValue();
		if (time != null) {
			cal.setTimeInMillis(time.longValue());
		}
		return new DateTimePickerDialog(
				getContext(),
				this, 
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 
				cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE),
				false);
	}
	
	public void onDateTimeSet(DateTimePicker view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
		final Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
		setValue(cal.getTimeInMillis());
	}

}
