package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.FormatHelper;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.location.Location;
import android.util.Log;

public class GpsHandler<T> implements FieldHandler<T> {

	private final Method mMethod;
	
	public GpsHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, Location.class);
	}

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			String[] la = parser.nextText().split(";");
			if (la.length == 2) {
				Double lat = FormatHelper.toDouble(la[0]);
				Double lon = FormatHelper.toDouble(la[1]);
				if (lat != null && lon != null) {
					Location l = new Location("gps");
					l.setLatitude(lat.doubleValue());
					l.setLongitude(lon.doubleValue());
					mMethod.invoke(entity, l);
				}
			}
		} catch (Exception e) {
			Log.e(GpsHandler.class.getName(), "error parsing gps location", e);
		}
	}
}
