package org.imogene.android.widget.field.edit;

import java.util.ArrayList;

import org.imogene.android.W;
import org.imogene.android.util.LocalizedTextList;
import org.imogene.android.widget.field.FieldEntity;

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
import android.widget.EditText;
import android.widget.TextView;

public class LocalizedTextFieldEdit extends FieldEntity<LocalizedTextList> {
	
	private String[] mRegexs;
	private int[] mRegexDisplayIds;
	
	private int mStringType;
	
	private final String[] isoArray;
	private final String[] displayArray;
	
	private boolean mOtherLanguagesHidden = true;
	private final ArrayList<EditText> mEditors;
	private final ArrayList<TextView> mLanguages;

	public LocalizedTextFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, W.layout.localized_text_field_edit);
		TypedArray a = context.obtainStyledAttributes(attrs, W.styleable.LocalizedTextFieldEdit, 0, 0);
		setStringType(a.getInt(W.styleable.LocalizedTextFieldEdit_stringType, InputType.TYPE_NULL));
		a.recycle();
		
		isoArray = context.getResources().getStringArray(W.array.languages_iso);
		displayArray = context.getResources().getStringArray(W.array.languages_display);
		
		mEditors = new ArrayList<EditText>(isoArray.length);
		mLanguages = new ArrayList<TextView>(isoArray.length);
		
		findViewById(W.id.more_button).setOnClickListener(this);
		findViewById(W.id.less_button).setOnClickListener(this);
		
		a = context.getResources().obtainTypedArray(W.array.languages_editors);
		for (int i = 0; i < isoArray.length; i++) {
			int editId = a.getResourceId(i, 0);
			View v = findViewById(editId);
			if (v != null && v instanceof EditText) {
				mEditors.add((EditText) v);
			}
		}
		a.recycle();
		
		a = context.getResources().obtainTypedArray(W.array.languages_views);
		for (int i = 0; i < isoArray.length; i++) {
			int languageId = a.getResourceId(i, 0);
			View v = findViewById(languageId);
			if (v != null && v instanceof TextView) {
				TextView tv = (TextView) v;
				tv.setText(displayArray[i]);
				mLanguages.add(tv);
			}
		}
		a.recycle();
		setFocusable(false);
		updateOtherLanguagesVisibility();
	}
	
	@Override
	public void setValue(LocalizedTextList value) {
		LocalizedTextList ltl = value;
		if (ltl == null) {
			ltl = new LocalizedTextList();
		}
		super.setValue(ltl);
		for (int i = 0; i < isoArray.length; i++) {
			mEditors.get(i).setText(ltl.getLocalized(isoArray[i]));
			MyTextWatcher watcher = new MyTextWatcher(isoArray[i], ltl);
			mEditors.get(i).addTextChangedListener(watcher);
		}
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
	}
	
	public void setStringType(int stringType) {
		mStringType = stringType;
//		final TextView v = getValueView();
//		if (isReadOnly()) {
//			v.setEnabled(false);
//			v.setInputType(InputType.TYPE_NULL);
//		} else {
//			v.setEnabled(true);
//			switch (stringType) {
//			case 0:
//				v.setMaxLines(1);
//				v.setInputType(InputType.TYPE_CLASS_TEXT);
//				break;
//			case 1:
//				v.setMinLines(3);
//				v.setInputType(InputType.TYPE_CLASS_TEXT
//						| InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
//						| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//				break;
//			case 2:
//				v.setMaxLines(1);
//				v.setInputType(InputType.TYPE_CLASS_PHONE);
//				break;
//			case 3:
//				v.setMinLines(3);
//				v.setInputType(InputType.TYPE_CLASS_TEXT
//						| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
//				break;
//			case 4:
//				v.setMaxLines(1);
//				v.setInputType(InputType.TYPE_CLASS_TEXT
//						| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//				break;
//			}
//		}
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
	public boolean isValid() {
		final LocalizedTextList value = getValue();
		if (value.isEmpty()) {
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
	
	private void updateOtherLanguagesVisibility() {
		for (int i = 1; i < isoArray.length; i++) {
			findViewById(W.id.more_button).setVisibility(mOtherLanguagesHidden ? View.VISIBLE : View.GONE);
			findViewById(W.id.less_button).setVisibility(mOtherLanguagesHidden ? View.GONE : View.VISIBLE);
			mEditors.get(i).setVisibility(mOtherLanguagesHidden ? View.GONE : View.VISIBLE);
			mLanguages.get(i).setVisibility(mOtherLanguagesHidden ? View.GONE : View.VISIBLE);
		}
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case W.id.less_button:
		case W.id.more_button:
			mOtherLanguagesHidden = !mOtherLanguagesHidden;
			updateOtherLanguagesVisibility();
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
		return null;
	}
	
	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		final SavedState myState = new SavedState(superState);
		myState.otherLanguagesHidden = mOtherLanguagesHidden;
		LocalizedTextList ltl = getValue();
		if (ltl != null) {
			String[][] localized = getValue().createLocalizedArray();
			if (localized != null) {
				myState.locales = localized[0];
				myState.values = localized[1];
			}
		}
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
		mOtherLanguagesHidden = myState.otherLanguagesHidden;
		updateOtherLanguagesVisibility();
		if (myState.locales != null && myState.values != null) {
			LocalizedTextList ltl = getValue();
			if (ltl == null) {
				ltl = new LocalizedTextList();
			}
			for (int i = 0; i < myState.locales.length; i++) {
				ltl.add(myState.locales[i], myState.values[i]);
			}
			setValue(ltl);
		}
	}
	
	private static class SavedState extends BaseSavedState {
		
		private boolean otherLanguagesHidden;
		private String[] locales;
		private String[] values;

		public SavedState(Parcel source) {
			super(source);
			otherLanguagesHidden = source.readInt() == 0;
			locales = source.createStringArray();
			values = source.createStringArray();
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeInt(otherLanguagesHidden ? 0 : 1);
			dest.writeStringArray(locales);
			dest.writeStringArray(values);
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
	
	private static class MyTextWatcher implements TextWatcher {
		
		private final String mLocale;
		private final LocalizedTextList mLocalizedTextList;
		
		public MyTextWatcher(String locale, LocalizedTextList ltl) {
			mLocale = locale;
			mLocalizedTextList = ltl;
		}

		public void afterTextChanged(Editable s) {
			mLocalizedTextList.add(mLocale, s.toString());
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// Don't care
			
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// Don't care
			
		}
		
	}

}
