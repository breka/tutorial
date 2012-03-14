package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.LocalizedTextList;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class LocalizedTextHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public LocalizedTextHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, LocalizedTextList.class);
	}
	
	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			LocalizedTextList ltl = new LocalizedTextList(parser.nextText());
			mMethod.invoke(entity, ltl);
		} catch (Exception e) {
			Log.e(StringHandler.class.getName(), "error parsing localized field", e);
		}
	}

}
