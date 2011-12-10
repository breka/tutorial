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

package org.imogene.android.widget;

import org.imogene.android.template.R;
import org.imogene.android.widget.NumberPicker.OnChangedListener;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * A view for selecting a month / year / day based on a calendar like layout.
 *
 * For a dialog using this view, see {@link android.app.DatePickerDialog}.
 */
public class PeriodPicker extends FrameLayout {

    private static final int DEFAULT_START_NUMBER = 1;
    private static final int DEFAULT_END_NUMBER = 100;
    
    private static final int DEFAULT_START_UNIT = 0;
    private static final int DEFAULT_END_UNIT = 2;
    
    /* UI Components */
    private final NumberPicker mNumberPicker;
    
    private final NumberPicker mUnitPicker;

    /**
     * How we notify users the date has changed.
     */
    private OnPeriodChangedListener mOnPeriodChangedListener;

    private int mNumber;
    
    private int mUnit;

    /**
     * The callback used to indicate the user changes the date.
     */
    public interface OnPeriodChangedListener {

        /**
         * @param view The view associated with this listener.
         * @param year The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility
         *  with {@link java.util.Calendar}.
         * @param dayOfMonth The day of the month that was set.
         */
        void onPeriodChanged(PeriodPicker view, int number, int unit);
    }

    public PeriodPicker(Context context) {
        this(context, null);
    }
    
    public PeriodPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PeriodPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.period_picker, this, true);

        mNumberPicker = (NumberPicker) findViewById(R.id.number);
        mNumberPicker.setSpeed(100);
        mNumberPicker.setOnChangeListener(new OnChangedListener() {
            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
                mNumber = newVal;
                if (mOnPeriodChangedListener != null) {
                    mOnPeriodChangedListener.onPeriodChanged(PeriodPicker.this, mNumber, mUnit);
                }
            }
        });

        mNumberPicker.setRange(DEFAULT_START_NUMBER, DEFAULT_END_NUMBER);
        
        String[] displayed = new String[] {"jours", "mois", "année"};
        mUnitPicker = (NumberPicker) findViewById(R.id.unit);
        mUnitPicker.setRange(DEFAULT_START_UNIT, DEFAULT_END_UNIT, displayed);
        mUnitPicker.setSpeed(200);
        mUnitPicker.setOnChangeListener(new OnChangedListener() {
            public void onChanged(NumberPicker picker, int oldVal, int newVal) {
                
                /* We display the month 1-12 but store it 0-11 so always
                 * subtract by one to ensure our internal state is always 0-11
                 */
                mUnit = newVal;
                if (mOnPeriodChangedListener != null) {
                    mOnPeriodChangedListener.onPeriodChanged(PeriodPicker.this, mNumber, mUnit);
                }
            }
        });
        
        // initialize to current date
        init(mNumber, mUnit, null);
        
        if (!isEnabled()) {
            setEnabled(false);
        }
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mNumberPicker.setEnabled(enabled);
        mUnitPicker.setEnabled(enabled);
    }

    public void updatePeriod(int number, int unit) {
        mNumber = number;
        mUnit = unit;
        updateSpinners();
    }

    private static class SavedState extends BaseSavedState {

        private final int mNumber;
        
        private final int mUnit;

        /**
         * Constructor called from {@link PeriodPicker#onSaveInstanceState()}
         */
        private SavedState(Parcelable superState, int number, int unit) {
            super(superState);
            mNumber = number;
            mUnit = unit;
        }
        
        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mNumber = in.readInt();
            mUnit = in.readInt();
        }

        public int getNumber() {
            return mNumber;
        }
        
        public int getUnit() {
        	return mUnit;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mNumber);
            dest.writeInt(mUnit);
        }

        @SuppressWarnings("unused")
		public static final Parcelable.Creator<SavedState> CREATOR =
                new Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


    /**
     * Override so we are in complete control of save / restore for this widget.
     */
    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        dispatchThawSelfOnly(container);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        
        return new SavedState(superState, mNumber, mUnit);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mNumber = ss.getNumber();
        mUnit = ss.getUnit();
    }

    /**
     * Initialize the state.
     * @param year The initial year.
     * @param monthOfYear The initial month.
     * @param dayOfMonth The initial day of the month.
     * @param onDateChangedListener How user is notified date is changed by user, can be null.
     */
    public void init(int number, int unit, OnPeriodChangedListener onPeriodChangedListener) {
        mNumber = number;
        mUnit = unit;
        mOnPeriodChangedListener = onPeriodChangedListener;
        updateSpinners();
    }

    private void updateSpinners() {
        mNumberPicker.setCurrent(mNumber);
        mUnitPicker.setCurrent(mUnit);
    }

    public int getNumber() {
        return mNumber;
    }
    
    public int getUnit() {
    	return mUnit;
    }
}
