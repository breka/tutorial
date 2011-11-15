package org.imogene.android.widget.field.edit;

import java.util.Calendar;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.DatePicker;

public class DateFieldEdit extends DatesFieldEdit implements OnDateSetListener {
	
	public DateFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.DatesFieldEdit, 0, 0);
		if (a.hasValue(W.styleable.DatesFieldEdit_dateMin)) {
			String date = a.getString(W.styleable.DatesFieldEdit_dateMin);
			setMin(FormatHelper.readDate(date));
		} else {
			setMin(null);
		}
		if (a.hasValue(W.styleable.DatesFieldEdit_dateMax)) {
			String date = a.getString(W.styleable.DatesFieldEdit_dateMax);
			setMax(FormatHelper.readDate(date));
		} else {
			setMax(null);
		}
		a.recycle();
	}
	
	@Override
	public String getDisplay() {
		final Long time = getValue();
		if (time != null) {
			return FormatHelper.displayAsDate(time.longValue());
		} else {
			return getEmptyText();
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
