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

import org.imogene.android.template.R;
import org.imogene.android.widget.PeriodPicker;
import org.imogene.android.widget.PeriodPicker.OnPeriodChangedListener;

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
public class PeriodPickerDialog extends AlertDialog implements OnClickListener, 
        OnPeriodChangedListener {

    private static final String NUMBER = "number";
    
    private static final String UNIT = "unit";
    
    private final PeriodPicker mPeriodPicker;
    
    private final OnPeriodSetListener mCallBack;

    private int mInitialNumber;
    
    private int mInitialUnit;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnPeriodSetListener {

        /**
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *  with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onPeriodSet(PeriodPicker view, int number, int unit);
    }

    /**
     * @param context The context the dialog is to run in.
     * @param callBack How the parent is notified that the date is set.
     * @param year The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth The initial day of the dialog.
     */
    public PeriodPickerDialog(Context context,
            OnPeriodSetListener callBack,
            int number,
            int unit) {
        this(context, R.style.Theme_Dialog_Alert, 
                callBack, number, unit);
    }

    /**
     * @param context The context the dialog is to run in.
     * @param theme the theme to apply to this dialog
     * @param callBack How the parent is notified that the date is set.
     * @param year The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth The initial day of the dialog.
     */
    public PeriodPickerDialog(Context context,
            int theme,
            OnPeriodSetListener callBack,
            int numer,
            int unit) {
        super(context, theme);

        mCallBack = callBack;
        mInitialNumber = numer;

        updateTitle(mInitialNumber, mInitialUnit);
        
        setButton(context.getText(android.R.string.ok), this);
        setButton2(context.getText(android.R.string.cancel), (OnClickListener) null);
        setIcon(R.drawable.ic_dialog_time);
        
        LayoutInflater inflater = 
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.period_picker_dialog, null);
        setView(view);
        mPeriodPicker = (PeriodPicker) view.findViewById(R.id.periodPicker);
        mPeriodPicker.init(mInitialNumber, mInitialUnit, this);
    }
    
    public void onClick(DialogInterface dialog, int which) {
        if (mCallBack != null) {
            mPeriodPicker.clearFocus();
            mCallBack.onPeriodSet(mPeriodPicker, mPeriodPicker.getNumber(), mPeriodPicker.getUnit());
        }
    }
    
    public void onPeriodChanged(PeriodPicker view, int number, int unit) {
        updateTitle(number, unit);
    }
    
    public void updatePeriod(int number, int unit) {
        mInitialNumber = number;
        mInitialUnit = unit;
        mPeriodPicker.updatePeriod(number, unit);
    }

    private void updateTitle(int number, int unit) {
        setTitle(String.valueOf(number)+" "+String.valueOf(unit));
    }
    
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(NUMBER, mPeriodPicker.getNumber());
        state.putInt(UNIT, mPeriodPicker.getUnit());
        return state;
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int number = savedInstanceState.getInt(NUMBER);
        int unit = savedInstanceState.getInt(UNIT);
        mPeriodPicker.init(number, unit, this);
        updateTitle(number, unit);
    }
}
