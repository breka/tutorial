package org.imogene.android.widget.field.edit;

import java.util.Calendar;

import org.imogene.android.template.R;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TimePicker;

public class TimeFieldEdit extends DatesFieldEdit implements OnTimeSetListener {

	public TimeFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DatesFieldEdit, 0, 0);
		if (a.hasValue(R.styleable.DatesFieldEdit_igDateMin)) {
			String date = a.getString(R.styleable.DatesFieldEdit_igDateMin);
			setMin(FormatHelper.readTime(date));
		} else {
			setMin(null);
		}
		if (a.hasValue(R.styleable.DatesFieldEdit_igDateMax)) {
			String date = a.getString(R.styleable.DatesFieldEdit_igDateMax);
			setMax(FormatHelper.readTime(date));
		} else {
			setMax(null);
		}
		a.recycle();
	}
	
	@Override
	public String getDisplay() {
		final Long time = getValue();
		if (time != null) {
			return FormatHelper.displayAsTime(time.longValue());
		} else {
			return getEmptyText();
		}
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final Long date = getValue();
		return date != null ? FieldPattern.matchesTime(value, date.longValue()) : false;
	}
	
	public Dialog createDialog() {
		final Calendar cal = Calendar.getInstance();
		final Long time = getValue();
		if (time != null) {
			cal.setTimeInMillis(time.longValue());
		}
		return new TimePickerDialog(
				getContext(), 
				this,
				cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE),
				false);
	}
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		setValue(cal.getTimeInMillis());		
	}

}
