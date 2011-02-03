package org.imogene.android.widget.field.edit;

import org.imogene.android.W;
import org.imogene.android.widget.field.FieldEntity;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.TextView;

public class StringFieldEdit extends FieldEntity<String> implements TextWatcher {
	
	private String[] mRegexs;
	private int[] mRegexDisplayIds;
	
	private int mStringType;
	
	public StringFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.string_field_edit);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.StringFieldEdit, 0, 0);
		setStringType(a.getInt(W.styleable.StringFieldEdit_stringType, InputType.TYPE_NULL));
		a.recycle();
		setFocusable(false);		
	}
	
	@Override
	public void setTitleId(int titleId) {
		super.setTitleId(titleId);
		getValueView().setHint(titleId);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setStringType(mStringType);
		final TextView v = getValueView();
		if (readOnly) {
			v.removeTextChangedListener(this);
		} else {
			v.addTextChangedListener(this);
		}
	}
	
	public void setStringType(int stringType) {
		mStringType = stringType;
		final TextView v = getValueView();
		if (isReadOnly()) {
			v.setEnabled(false);
			v.setInputType(InputType.TYPE_NULL);
		} else {
			v.setEnabled(true);
			switch (stringType) {
			case 0:
				v.setMaxLines(1);
				v.setInputType(InputType.TYPE_CLASS_TEXT);
				break;
			case 1:
				v.setMinLines(3);
				v.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
						| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				break;
			case 2:
				v.setMaxLines(1);
				v.setInputType(InputType.TYPE_CLASS_PHONE);
				break;
			case 3:
				v.setMinLines(3);
				v.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
				break;
			case 4:
				v.setMaxLines(1);
				v.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				break;
			}
		}
	}
	
	public int getStringType() {
		return mStringType;
	}
	
	public void setRegexs(String[] regexs) {
		mRegexs = regexs;
	}
	
	public void setRegexDisplayIds(int[] regexDisplayIds) {
		mRegexDisplayIds = regexDisplayIds;
	}
	
	@Override
	public boolean matchesDependencyValue(String value) {
		final String str = getValue();
		return str != null ? str.matches(value) : false;
	}
	
	@Override
	public boolean isValid() {
		final String value = getValue();
		if (TextUtils.isEmpty(value)) {
			return !isRequired();
		} else {
			if (mRegexs != null) {
				for (String regex : mRegexs) {
					if (!value.matches(regex))
						return false;
				}
				return true;
			} else {
				return true;
			}
		}
	}
	
	@Override
	public String getErrorMessage() {
		final Resources res = getResources();
		final StringBuilder builder = new StringBuilder();
		if (isRequired()) {
			builder.append(res.getString(W.string.is_required));
			if (mRegexDisplayIds != null) {
				builder.append('\n');
			}
		}
		if (mRegexDisplayIds != null) {
			boolean first = true;
			for (int id : mRegexDisplayIds) {
				builder.append(res.getString(id));
				if (first)
					first = false;
				else
					builder.append('\n');
			}
		}
		return builder.toString();
	}

	@Override
	public String getDisplay() {
		return getValue();
	}
	
	public void afterTextChanged(Editable s) {
		disableUpdateDisplayOnChange();
		setValue(s.toString());
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
		
		private String value;

		public SavedState(Parcel source) {
			super(source);
			value = source.readString();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeString(value);
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
