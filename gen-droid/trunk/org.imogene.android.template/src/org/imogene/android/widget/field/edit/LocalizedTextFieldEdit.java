package org.imogene.android.widget.field.edit;

import java.util.HashMap;
import java.util.Locale;

import org.imogene.android.template.R;
import org.imogene.android.util.LocalizedTextList;
import org.imogene.android.util.Tools;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class LocalizedTextFieldEdit extends StringFieldEdit<LocalizedTextList> {
	
	private final String[] isoArray;
	private final String[] displayArray;
	
	private boolean mOtherLanguagesHidden = true;
	
	private final HashMap<String, EditText> mEditors;
	private final ViewGroup mEntries;

	public LocalizedTextFieldEdit(Context context, AttributeSet attrs) {
		super(context, attrs, R.layout.field_edit_localized);

		isoArray = getResources().getStringArray(R.array.languages_iso);
		displayArray = getResources().getStringArray(R.array.languages_display);
		
		int pos = Tools.find(isoArray, Locale.getDefault().getLanguage());
		if (pos > -1) {
			Tools.replace(isoArray, 0, pos);
			Tools.replace(displayArray, 0, pos);
		}
		
		mEditors = new HashMap<String, EditText>(isoArray.length);
		
		findViewById(R.id.more_button).setOnClickListener(this);
		findViewById(R.id.less_button).setOnClickListener(this);
		
		mEntries = (ViewGroup) findViewById(R.id.localized_entries);
		
		boolean first = true;
		for (int i = 0; i < isoArray.length; i++) {
			if (first) {
				mEditors.put(isoArray[i], (EditText) findViewById(R.id.localized));
				TextView language = (TextView) findViewById(R.id.locale);
				language.setText(displayArray[i]);
				first = false;
			} else {
				ViewGroup entry = (ViewGroup) inflate(context, R.layout.localized_text_editor, mEntries);
				mEditors.put(isoArray[i], (EditText) entry.findViewById(R.id.localized));
				TextView language = (TextView) entry.findViewById(R.id.locale);
				language.setText(displayArray[i]);
			}
		}
		
		setFocusable(false);
		updateOtherLanguagesVisibility();
	}
	
	@Override
	public boolean isEmpty() {
		LocalizedTextList ltl = getValue();
		return ltl != null ? ltl.isEmpty() : true;
	}
	
	@Override
	public boolean isValid() {
		final LocalizedTextList value = getValue();
		if (value == null || value.isEmpty()) {
			return !isRequired();
		}
		final String[] regexs = getRegexs();
		if (regexs != null) {
			for (String regex : regexs) {
				if (!value.matches(regex)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void notifyUpdate() {
		super.setValue(getValue());
	}
	
	@Override
	public void setValue(LocalizedTextList value) {
		LocalizedTextList ltl = value;
		if (ltl == null) {
			ltl = new LocalizedTextList();
		}
		super.setValue(ltl);
		for (int i = 0; i < isoArray.length; i++) {
			MyTextWatcher watcher = new MyTextWatcher(isoArray[i]);

			EditText editText = mEditors.get(isoArray[i]);
			editText.setText(ltl.getLocalized(isoArray[i]));
			editText.addTextChangedListener(watcher);
		}
	}
	
	@Override
	public void setTitleId(int titleId) {
		super.setTitleId(titleId);
		getValueView().setHint(titleId);
	}
	
	private void updateOtherLanguagesVisibility() {
		findViewById(R.id.more_button).setVisibility(mOtherLanguagesHidden ? View.VISIBLE : View.GONE);
		findViewById(R.id.less_button).setVisibility(mOtherLanguagesHidden ? View.GONE : View.VISIBLE);
		mEntries.setVisibility(mOtherLanguagesHidden ? View.GONE : View.VISIBLE);
	}
	
	@Override
	protected void dispatchClick(View v) {
		switch (v.getId()) {
		case R.id.less_button:
		case R.id.more_button:
			mOtherLanguagesHidden = !mOtherLanguagesHidden;
			updateOtherLanguagesVisibility();
		}
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
	}
	
	private static class SavedState extends BaseSavedState {
		
		private boolean otherLanguagesHidden;

		public SavedState(Parcel source) {
			super(source);
			otherLanguagesHidden = source.readInt() == 0;
		}
		
		public SavedState(Parcelable superState) {
			super(superState);
		}
		
		@Override
		public void writeToParcel(Parcel dest, int flags) {		
			super.writeToParcel(dest, flags);
			dest.writeInt(otherLanguagesHidden ? 0 : 1);
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
	
	private class MyTextWatcher implements TextWatcher {
		
		private final String locale;
		
		public MyTextWatcher(String locale) {
			this.locale = locale;
		}

		public void afterTextChanged(Editable s) {
			getValue().add(locale, s.toString());
			notifyUpdate();
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// Don't care
			
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// Don't care
			
		}
		
	}
}
