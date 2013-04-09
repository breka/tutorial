package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.FormatHelper;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class EnumHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public EnumHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, int.class);
	}

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			Integer value = FormatHelper.toInteger(parser.nextText());
			mMethod.invoke(entity, value != null ? value.intValue() : -1);
		} catch (Exception e) {
			Log.e(EnumHandler.class.getName(), "error parsing single choice enumeration", e);
		}
	}
}
