package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class StringHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public StringHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, String.class);
	}

	public void parse(android.content.Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, parser.nextText());
		} catch (Exception e) {
			Log.e(StringHandler.class.getName(), "error parsing string field", e);
		}
	};
}
