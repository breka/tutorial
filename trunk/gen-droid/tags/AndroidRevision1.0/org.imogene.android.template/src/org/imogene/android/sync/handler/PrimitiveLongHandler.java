package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.FormatHelper;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class PrimitiveLongHandler<T> implements FieldHandler<T> {

	private final Method mMethod;

	public PrimitiveLongHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, long.class);
	}
	
	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			Long l = FormatHelper.toLong(parser.nextText());
			if (l != null) {
				mMethod.invoke(entity, l.longValue());
			}
		} catch (Exception e) {
			Log.e(PrimitiveLongHandler.class.getName(), "error parsing primitive long", e);
		}
	}
	
}
