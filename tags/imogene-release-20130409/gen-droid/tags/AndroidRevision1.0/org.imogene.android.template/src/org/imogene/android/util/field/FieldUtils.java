package org.imogene.android.util.field;

import java.util.ArrayList;
import java.util.Arrays;

import android.location.Location;
import android.net.Uri;
import android.text.TextUtils;

public class FieldUtils {
	
	public static final boolean isEmpty(String s) {
		return TextUtils.isEmpty(s);
	}
	
	public static final boolean isEmpty(Long l) {
		return l == null;
	}
	
	public static final boolean isEmpty(Integer i) {
		return i == null;
	}
	
	public static final boolean isEmpty(Float f) {
		return f == null;
	}
	
	public static final boolean isEmpty(Boolean b) {
		return b == null;
	}
	
	public static final boolean isEmpty(int i) {
		return i == -1;
	}
	
	public static final boolean isEmpty(boolean[] array) {
		return Arrays.equals(new boolean[array.length], array);
	}
	
	public static final boolean isEmpty(Uri uri) {
		return uri == null;
	}
	
	public static final boolean isEmpty(ArrayList<Uri> uris) {
		return uris == null || uris.isEmpty();
	}
	
	public static final boolean isEmpty(Location l) {
		return l == null;
	}
	
	public static final boolean isValid(String s, boolean required, String[] regexs) {
		if (isEmpty(s)) {
			return !required;
		} else {
			if (regexs != null) {
				for (String regex : regexs) {
					if (!s.matches(regex))
						return false;
				}
				return true;
			} else {
				return true;
			}
		}
	}
	
	public static final boolean isValid(Long l, boolean required, Long min, Long max) {
		if (isEmpty(l)) {
			return !required;
		} else {
			if (min != null && min > l)
				return false;
			if (max != null && max < l)
				return false;
			return true;
		}
	}
	
	public static final boolean isValid(Float f, boolean required, Float min, Float max) {
		if (isEmpty(f)) {
			return !required;
		} else {
			if (min != null && min > f)
				return false;
			if (max != null && max < f)
				return false;
			return true;
		}
	}

	public static final boolean isValid(Integer i, boolean required, Integer min, Integer max) {
		if (isEmpty(i)) {
			return !required;
		} else {
			if (min != null && min > i)
				return false;
			if (max != null && max < i)
				return false;
			return true;
		}
	}
	
	public static final boolean isValid(Boolean b, boolean required) {
		return required?!isEmpty(b):true;
	}
	
	public static final boolean isValid(int i, boolean required) {
		return required?!isEmpty(i):true;
	}
	
	public static final boolean isValid(boolean[] array, boolean required) {
		return required?!isEmpty(array):true;
	}
	
	public static final boolean isValid(Uri uri, boolean required) {
		return required?!isEmpty(uri):true;
	}
	
	public static final boolean isValid(ArrayList<Uri> uris, boolean required) {
		return required?!isEmpty(uris):true;
	}
	
	public static final boolean isValid(Location l, boolean required) {
		return required?!isEmpty(l):true;
	}
}
