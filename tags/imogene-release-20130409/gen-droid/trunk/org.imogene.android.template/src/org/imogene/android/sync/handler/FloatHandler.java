package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.FormatHelper;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class FloatHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public FloatHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, Float.class);
	}
	
	public void parse(android.content.Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, FormatHelper.toFloat(parser.nextText()));
		} catch (Exception e) {
			Log.e(FloatHandler.class.getName(), "error parsing float", e);
		}
	};

}