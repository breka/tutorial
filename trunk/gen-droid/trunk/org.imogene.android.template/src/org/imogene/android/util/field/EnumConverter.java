package org.imogene.android.util.field;

import org.imogene.android.util.FormatHelper;

import android.content.Context;

public class EnumConverter {
	
	public static final String convert(int[] itemsValues, boolean[] values) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (int i = 0; i < values.length; i++) {
			if (values[i]) {
				if (first)
					first = false;
				else
					builder.append(';');
				builder.append(String.valueOf(itemsValues[i]));
			}
		}
		if (first)
			return "-1";
		else
			return builder.toString();
	}
	
	public static final String convert(Context context, int itemsValuesId, boolean[] values) {
		return convert(context.getResources().getIntArray(itemsValuesId), values);
	}
	
	public static final boolean[] parseMulti(int[] itemsValues, String str) {
		boolean[] b = new boolean[itemsValues.length];
		if (str != null) {
			for (String substr : str.split(";")) {
				Integer i = FormatHelper.toInteger(substr);
				if (i != null) {
					for (int j = 0; j < itemsValues.length; j++) {
						if (itemsValues[j] == i.intValue()) {
							b[j] = true;
						}
					}
				}
			}
		}
		return b;
	}
	
	public static final boolean[] parseMulti(Context context, int itemsValuesId, String str) {
		return parseMulti(context.getResources().getIntArray(itemsValuesId), str);
	}

}
