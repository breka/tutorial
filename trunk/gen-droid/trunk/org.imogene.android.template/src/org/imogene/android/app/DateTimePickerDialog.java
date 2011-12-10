/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.imogene.android.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.imogene.android.template.R;
import org.imogene.android.widget.DateTimePicker;
import org.imogene.android.widget.DateTimePicker.OnDateTimeChangedListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A simple dialog containing an {@link android.widget.DatePicker}.
 */
public class DateTimePickerDialog extends AlertDialog implements OnClickListener, 
        OnDateTimeChangedListener {
    
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    
    private final DateTimePicker mDateTimePicker;
    private final OnDateTimeSetListener mCallBack;
    private final Calendar mCalendar;
    private final java.text.DateFormat mTitleDateFormat;

    private int mInitialYear;
    private int mInitialMonth;
    private int mInitialDay;
    private int mInitialHourOfDay;
    private int mInitialMinute;
    private boolean mIs24HourView;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateTimeSetListener {

        /**
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *  with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onDateTimeSet(DateTimePicker view, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
    }

    /**
     * @param context The context the dialog is to run in.
     * @param callBack How the parent is notified that the date is set.
     * @param year The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth The initial day of the dialog.
     */
    public DateTimePickerDialog(Context context,
            OnDateTimeSetListener callBack,
            int year,
            int monthOfYear,
            int dayOfMonth,
            int hourOfDay,
            int minute,
            boolean is24HourView) {
        this(context, R.style.Theme_Dialog_Alert, 
                callBack, year, monthOfYear, dayOfMonth, hourOfDay, minute, is24HourView);
    }

    /**
     * @param context The context the dialog is to run in.
     * @param theme the theme to apply to this dialog
     * @param callBack How the parent is notified that the date is set.
     * @param year The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth The initial day of the dialog.
     */
    public DateTimePickerDialog(Context context,
            int theme,
            OnDateTimeSetListener callBack,
            int year,
            int monthOfYear,
            int dayOfMonth,
            int hourOfDay,
            int minute,
            boolean is24HourView) {
        super(context, theme);

        mCallBack = callBack;
        mInitialYear = year;
        mInitialMonth = monthOfYear;
        mInitialDay = dayOfMonth;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mIs24HourView = is24HourView;
        
        mTitleDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy hh:mm a");
        mCalendar = Calendar.getInstance();
        updateTitle(mInitialYear, mInitialMonth, mInitialDay, mInitialHourOfDay, mInitialMinute);
        
        setButton(context.getText(android.R.string.ok), this);
        setButton2(context.getText(android.R.string.cancel), this);
        setIcon(R.drawable.ic_dialog_time);
        
        LayoutInflater inflater = 
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_time_picker_dialog, null);
        setView(view);

        // initialize state
        mDateTimePicker = (DateTimePicker) view.findViewById(R.id.dateTimePicker);
        mDateTimePicker.init(mInitialYear, mInitialMonth, mInitialDay, mInitialHourOfDay, mInitialMinute, this);
        mDateTimePicker.setIs24HourView(mIs24HourView);
    }

//    @Override
//    public void show() {
//        super.show();
//
//        /* Sometimes the full month is displayed causing the title
//         * to be very long, in those cases ensure it doesn't wrap to
//         * 2 lines (as that looks jumpy) and ensure we ellipsize the end.
//         */
//        TextView title = (TextView) findViewById(android.R.id.title);
//        title.setSingleLine();
//        title.setEllipsize(TruncateAt.END);
//    }
    
    public void onClick(DialogInterface dialog, int which) {
        if (which == BUTTON_POSITIVE && mCallBack != null) {
            mDateTimePicker.clearFocus();
            mCallBack.onDateTimeSet(mDateTimePicker, mDateTimePicker.getYear(), 
                    mDateTimePicker.getMonth(), mDateTimePicker.getDayOfMonth(),
                    mDateTimePicker.getCurrentHour(), mDateTimePicker.getCurrentMinute());
        }
    }
    
    public void onDateTimeChanged(DateTimePicker view, int year,
            int month, int day, int hour, int minute) {
        updateTitle(year, month, day, hour, minute);
    }
    
    public void updateDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        mInitialYear = year;
        mInitialMonth = monthOfYear;
        mInitialDay = dayOfMonth;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mDateTimePicker.updateDateTime(year, monthOfYear, dayOfMonth, hourOfDay, minute);
    }

    private void updateTitle(int year, int month, int day, int hour, int minute) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        setTitle(mTitleDateFormat.format(mCalendar.getTime()));
    }
    
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mDateTimePicker.getYear());
        state.putInt(MONTH, mDateTimePicker.getMonth());
        state.putInt(DAY, mDateTimePicker.getDayOfMonth());
        state.putInt(HOUR, mDateTimePicker.getCurrentHour());
        state.putInt(MINUTE, mDateTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mDateTimePicker.is24HourView());
        return state;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
        int hour = savedInstanceState.getInt(HOUR);
        int minute = savedInstanceState.getInt(MINUTE);
        mDateTimePicker.init(year, month, day, hour, minute, this);
        mDateTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        updateTitle(year, month, day, hour, minute);
    }
}
