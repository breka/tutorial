package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.FormatHelper;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class LongHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public LongHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, Long.class);
	}
	
	public void parse(android.content.Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, FormatHelper.toLong(parser.nextText()));
		} catch (Exception e) {
			Log.e(LongHandler.class.getName(), "error parsing long", e);
		}
	};
}
