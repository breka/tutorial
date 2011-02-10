package org.imogene.android.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.UUID;

import org.imogene.android.common.LocalizedText;

import android.content.Context;
import android.text.TextUtils;

public class LocalizedTextList {
	
	private String mTextId;
	private HashMap<String, LocalizedText> mMap;
	
	public LocalizedTextList() {
		mTextId = UUID.randomUUID().toString();
	}
	
	public LocalizedTextList(String textId) {
		mTextId = textId;
	}
	
	public String getTextId() {
		return mTextId;
	}
	
	public void add(LocalizedText lt) {
		String locale = lt.getLocale();
		if (locale == null) {
			return;
		}
		if (mMap == null) {
			mMap = new HashMap<String, LocalizedText>();
		}
		if (mMap.containsKey(locale)) {
			LocalizedText llt = mMap.get(locale);
			llt.setValue(lt.getValue());
		} else {
			mMap.put(locale, lt);
		}
	}
	
	public void add(String locale, String value) {
		if (locale == null) {
			return;
		}
		if (mMap == null) {
			mMap = new HashMap<String, LocalizedText>();
		}
		if (mMap.containsKey(locale)) {
			mMap.get(locale).setValue(value);
		} else {
			LocalizedText lt = new LocalizedText();
			lt.setLocale(locale);
			lt.setValue(value);
			mMap.put(locale, lt);
		}
	}

	public String getLocalized() {
		if (mMap != null && !mMap.isEmpty()) {
			String locale = Locale.getDefault().getLanguage();
			if (mMap.containsKey(locale)) {
				LocalizedText lt = mMap.get(locale);
				if (!TextUtils.isEmpty(lt.getValue())) {
					return lt.getValue();
				}
			}
			for (LocalizedText lt : mMap.values()) {
				if (!TextUtils.isEmpty(lt.getValue())) {
					return lt.getValue();
				}
			}
		}
		return null;
	}
	
	public String getLocalized(String locale) {
		if (mMap != null && !mMap.isEmpty()) {
			if (mMap.containsKey(locale)) {
				LocalizedText lt = mMap.get(locale);
				if (!TextUtils.isEmpty(lt.getValue())) {
					return lt.getValue();
				}
			}
		}
		return null;
	}
	
	public Iterator<LocalizedText> getLocalizedTexts() {
		if (mMap != null) {
			return mMap.values().iterator();
		}
		return null;
	}
	
	public boolean isEmpty() {
		if (mMap != null && !mMap.isEmpty()) {
			for (LocalizedText lt : mMap.values()) {
				if (!TextUtils.isEmpty(lt.getValue())) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean matches(String regex) {
		if (!isEmpty()) {
			for (LocalizedText lt : mMap.values()) {
				String value = lt.getValue();
				if (!TextUtils.isEmpty(value) && !value.matches(regex)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void saveThemAll(Context context, boolean local) {
		if (!local) {
			return;
		}
		boolean noneValid = true;
		if (mMap != null) {
			for (LocalizedText lt : mMap.values()) {
				lt.setFieldId(mTextId);
				lt.commit(context, true, false);
				if (!TextUtils.isEmpty(lt.getValue())) {
					noneValid = false;
				}
			}
		}
		if (noneValid) {
			mTextId = null;
		}
	}
	
	public String[][] createLocalizedArray() {
		String[][] result = new String[2][mMap.size()];
		int i = 0;
		for (LocalizedText lt : mMap.values()) {
			result[0][i] = lt.getLocale();
			result[1][i] = lt.getValue();
			i++;
		}
		return result;
	}
	
	
}
