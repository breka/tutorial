package org.imogene.android.sync;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;

public interface FieldHandler<T> {

	public void parse(Context context, XmlPullParser parser, T entity);

}
