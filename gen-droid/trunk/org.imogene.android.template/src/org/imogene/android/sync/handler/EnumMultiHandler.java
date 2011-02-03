package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.field.EnumConverter;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class EnumMultiHandler<T> implements FieldHandler<T> {

	private final Method mMethod;
	private final int mArrayId;
	
	public EnumMultiHandler(Class<T> c, String methodName, int resId) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, boolean[].class);
		mArrayId = resId;
	}

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, EnumConverter.parseMulti(context, mArrayId, parser.nextText()));
		} catch (Exception e) {
			Log.e(EnumMultiHandler.class.getName(), "error parsing single choice enumeration", e);
		}
	}
}
