package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.FormatHelper;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class BooleanHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public BooleanHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, Boolean.class);
	}

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, FormatHelper.toBoolean(parser.nextText()));
		} catch (Exception e) {
			Log.e(BooleanHandler.class.getName(), "error parsing boolean", e);
		}
	}

}
