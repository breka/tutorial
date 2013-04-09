package org.imogene.android.util.field;

import org.imogene.android.util.FormatHelper;

import android.content.Context;

public class EnumConverter {
	
	public static final String convert(Context context, final int resId, int value) {
		if (value == -1) {
			return "-1";
		} else {
			int[] array = context.getResources().getIntArray(resId);
			return String.valueOf(array[value]);
		}
	}
	
	public static final String convert(Context context, final int resId, boolean[] values) {
		int[] array = context.getResources().getIntArray(resId);
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (int i = 0; i < values.length; i++) {
			if (values[i]) {
				if (first)
					first = false;
				else
					builder.append(';');
				builder.append(String.valueOf(array[i]));
			}
		}
		if (first)
			return "-1";
		else
			return builder.toString();
	}
	
	public static final int parse(Context context, final int resId, String str) {
		Integer i = FormatHelper.toInteger(str);
		if (i != null) {
			int[] array = context.getResources().getIntArray(resId);
			for (int j = 0; j < array.length; j++) {
				if (array[j] == i.intValue()) {
					return j;
				}
			}
		}
		return -1;
	}
	
	public static final boolean[] parseMulti(Context context, final int resId, String str) {
		int[] array = context.getResources().getIntArray(resId);
		boolean[] b = new boolean[array.length];
		if (str != null) {
			for (String substr : str.split(";")) {
				Integer i = FormatHelper.toInteger(substr);
				if (i != null)
					for (int j = 0; j < array.length; j++)
						if (array[j] == i.intValue())
							b[j] = true;
			}
		}
		return b;
	}

}
