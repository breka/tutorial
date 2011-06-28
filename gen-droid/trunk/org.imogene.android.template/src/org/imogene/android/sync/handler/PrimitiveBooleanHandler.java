package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class PrimitiveBooleanHandler<T> implements FieldHandler<T> {

	private final Method mMethod;

	public PrimitiveBooleanHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, boolean.class);
	}
	
	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, Boolean.parseBoolean(parser.nextText()));
		} catch (Exception e) {
			Log.e(PrimitiveLongHandler.class.getName(), "error parsing primitive boolean", e);
		}
	}
}