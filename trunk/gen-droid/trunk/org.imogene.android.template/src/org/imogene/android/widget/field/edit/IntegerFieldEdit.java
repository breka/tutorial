package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.util.FormatHelper;
import org.imogene.android.util.field.FieldPattern;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class IntegerFieldEdit extends BaseFieldEdit<Integer> implements TextWatcher {
	
	private TextView mUnitView;

	private Integer mMin;
	private Integer mMax;
	
	public IntegerFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.field_edit_numeric);
		
		mUnitView = (TextView) findViewById(W.id.unit);
		if (mUnitView != null) {
			mUnitView.setSaveEnabled(false);
		}
		
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.IntegerFieldEdit, 0, 0);
		setUnitId(a.getResourceId(W.styleable.IntegerFieldEdit_unit, -1));
		if (a.hasValue(W.styleable.IntegerFieldEdit_intMin)) {
			setMin(a.getInt(W.styleable.IntegerFieldEdit_intMin, 0));
		} else {
			setMin(null);
		}
		if (a.hasValue(W.styleable.IntegerFieldEdit_intMax)) {
			setMax(a.getInt(W.styleable.IntegerFieldEdit_intMax, 0));
		} else {
			setMax(null);
		}
		a.recycle();
		setFocusable(false);		
	}
	
	@Override
	public void setTitleId(int titleId) {
		super.setTitleId(titleId);
		getValueView().setHint(titleId);
	}
	
	public void setUnitId(int unitId) {
		if (mUnitView != null) {
			if (unitId > 0) {
				mUnitView.setText(unitId);
			}
			mUnitView.setVisibility(unitId > 0 ? View.VISIBLE : View.GONE);
		}
	}
	
	public void setMin(Integer min) {
		mMin = min;
	}
	
	public Integer getMin() {
		return mMin;
	}
	
	public void setMax(Integer max) {
		mMax = max;
	}
	
	public Integer getMax() {
		return mMax;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		final TextView v = getValueView();
		if (readOnly) {
			v.removeTextChangedListener(this);
			v.setEnabled(false);
			v.setInputType(InputType.TYPE_NULL);
		} else {
			v.addTextChangedListener(this);
			v.setEnabled(true);
			v.setInputType(
				InputType.TYPE_CLASS_NUMBER |
				InputType.TYPE_NUMBER_FLAG_SIGNED);
		}
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {	
		final Integer i = getValue();
		return i != null ? FieldPattern.matchesInt(value, i.intValue()) : false;
	}
	
	@Override
	public boolean isValid() {
		final Integer value = getValue();
		if (value == null) {
			return !isRequired();
		} else {
			if (mMin != null && mMin > value)
				return false;
			if (mMax != null && mMax < value)
				return false;
			return true;
		}
	}
	
	@Override
	public String getErrorMessage() {
		final Resources res = getResources();
		final boolean required = isRequired();
		final StringBuilder builder = new StringBuilder();
		if (required) {
			builder.append(res.getString(W.string.is_required));
		}
		if (mMin != null) {
			if (required)
				builder.append('\n');
			builder.append(res.getString(W.string.greater_than_decimal, mMin));
		}
		if (mMax != null) {
			if (required || mMin != null)
				builder.append('\n');
			builder.append(res.getString(W.string.lower_than_decimal, mMax));
		}
		return builder.toString();
	}
	
	@Override
	public String getDisplay() {
		final Integer value = getValue();
		return value != null ? value.toString() : null;
	}
	
	public void afterTextChanged(Editable s) {
		disableUpdateDisplayOnChange();
		setValue(FormatHelper.toInteger(s.toString()));
		enableUpdateDisplayOnChange();
	}

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// Don't care
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// Don't care
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState myState = new SavedState(superState);
		myState.value = getValue();
		return myState;
	}
	
	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if (state == null || !state.getClass().equals(SavedState.class)) {
			// Didn't save state for us in onSaveInstanceState
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());
		setValue(myState.value);
	}
	
	private static class SavedState extends BaseSavedState {
		
		private Integer value;

		public SavedState(Parcel source) {
			super(source);
			value = (Integer) source.readValue(null);
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeValue(value);
		}
		
		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
			
			public SavedState createFromParcel(Parcel source) {
				return new SavedState(source);
			}
		};

	}
}
