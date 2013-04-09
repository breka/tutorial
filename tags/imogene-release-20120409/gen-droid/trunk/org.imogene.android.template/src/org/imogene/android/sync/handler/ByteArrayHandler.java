package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.base64.Base64;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;

public class ByteArrayHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;
	
	public ByteArrayHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, byte[].class);
	}

	public void parse(android.content.Context context, XmlPullParser parser, T entity) {
		try {
			mMethod.invoke(entity, Base64.decodeBase64(parser.nextText().getBytes()));
		} catch (Exception e) {
			Log.e(StringHandler.class.getName(), "error parsing string field", e);
		}
	};
}
