package org.imogene.android.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.database.sqlite.SQLiteWrapper;
import org.imogene.android.util.content.ContentUrisUtils;
import org.imogene.android.util.database.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.BaseColumns;

public class GpsLocation extends Location {
	
	public static class Columns implements BaseColumns {
		public static final String TABLE_NAME = "gpslocation";

		public static final String ACCURACY = "accuracy";
		public static final String ALTITUDE = "altitude";
		public static final String BEARING = "bearing";
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String PROVIDER = "provider";
		public static final String SPEED = "speed";
		public static final String TIME = "time";
		public static final String HASACCURACY = "hasaccuracy";
		public static final String HASALTITUDE = "hasaltitude";
		public static final String HASBEARING = "hasbearing";
		public static final String HASSPEED = "hasspeed";
	}
	
	public static GpsLocation getLocation(Context context, long _id) {
		Cursor c = SQLiteWrapper.query(context, Columns.TABLE_NAME, null, Columns._ID + "=" + _id);
		try {
			if (c.moveToFirst()) {
				return new GpsLocation(c);
			}
		} finally {
			c.close();
		}
		return null;
	}
	
	public static long saveLocation(Context context, Location location) {
		if (location == null) return -1;
		
		if (location instanceof GpsLocation) {
			return ((GpsLocation) location).saveOrUpdate(context);
		} else {
			return new GpsLocation(location).saveOrUpdate(context);
		}
	}
	
	public static final Intent build(Context context, Uri uri, SQLiteBuilder builder,
			String gpsColumn, String gpsMethod,	double north, double east, double south, double west) {
		SQLiteBuilder b = new SQLiteBuilder();
		if (builder != null)
			b.appendWhere(builder.create());
		
		SQLiteBuilder locations = new SQLiteBuilder(Columns.TABLE_NAME, Columns._ID);
		locations.appendSup(Columns.LONGITUDE, west);
		locations.appendInf(Columns.LONGITUDE, east);
		locations.appendSup(Columns.LATITUDE, south);
		locations.appendInf(Columns.LATITUDE, north);
		
		b.appendIn(gpsColumn, locations.create());
		
		String sql = DatabaseUtils.computeWhere(b).toSQL();

		EntityCursor c = (EntityCursor) SQLiteWrapper.query(context, uri, sql, null);
		final int count = c.getCount();
		if (count < 1) {
			return null;
		}

		Intent result = null;
		try {
			final Method method = c.getClass().getDeclaredMethod(gpsMethod, (Class[]) null);
			final String[] titles = new String[count];
			final String[] descriptions = new String[count];
			final double[] latitudes = new double[count];
			final double[] longitudes = new double[count];
			final String[] uris = new String[count];
			for (int i = 0; i < count; i++) {
				c.moveToPosition(i);
				titles[i] = c.getMainDisplay(context);
				descriptions[i] = c.getSecondaryDisplay(context);
				Location l = (Location) method.invoke(c, (Object[]) null);
				latitudes[i] = l.getLatitude();
				longitudes[i] = l.getLongitude();
				uris[i] = ContentUrisUtils.withAppendedId(uri, c.getId()).toString();
			}
			result = new Intent(Intents.ACTION_SHOW_CLOUDS);
			result.putExtra(Extras.EXTRA_ITEM_NUMBER, count);
			result.putExtra(Extras.EXTRA_ITEM_TITLES, titles);
			result.putExtra(Extras.EXTRA_ITEM_DESCRIPTIONS, descriptions);
			result.putExtra(Extras.EXTRA_ITEM_LATITUDES, latitudes);
			result.putExtra(Extras.EXTRA_ITEM_LONGITUDES, longitudes);
			result.putExtra(Extras.EXTRA_ITEM_URIS, uris);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		c.close();
		return result;
	}
	
	private long _id = -1;

	public GpsLocation(Location l) {
		super(l);
	}
	
	protected GpsLocation(Cursor c) {
		super("cursor");
		_id = c.getLong(c.getColumnIndexOrThrow(Columns._ID));
		setAccuracy(c.getFloat(c.getColumnIndexOrThrow(Columns.ACCURACY)));
		setAltitude(c.getDouble(c.getColumnIndexOrThrow(Columns.ALTITUDE)));
		setBearing(c.getFloat(c.getColumnIndexOrThrow(Columns.BEARING)));
		setLatitude(c.getDouble(c.getColumnIndexOrThrow(Columns.LATITUDE)));
		setLongitude(c.getDouble(c.getColumnIndexOrThrow(Columns.LONGITUDE)));
		setProvider(c.getString(c.getColumnIndexOrThrow(Columns.PROVIDER)));
		setSpeed(c.getFloat(c.getColumnIndexOrThrow(Columns.SPEED)));
		setTime(c.getLong(c.getColumnIndexOrThrow(Columns.TIME)));
	}
	
	public long saveOrUpdate(Context context) {
		ContentValues values = new ContentValues();
		values.put(Columns.ACCURACY, getAccuracy());
		values.put(Columns.ALTITUDE, getAltitude());
		values.put(Columns.BEARING, getBearing());
		values.put(Columns.LATITUDE, getLatitude());
		values.put(Columns.LONGITUDE, getLongitude());
		values.put(Columns.PROVIDER, getProvider());
		values.put(Columns.SPEED, getSpeed());
		values.put(Columns.TIME, getTime());
		values.put(Columns.HASACCURACY, hasAccuracy() ? 1 : 0);
		values.put(Columns.HASALTITUDE, hasAltitude() ? 1 : 0);
		values.put(Columns.HASBEARING, hasBearing() ? 1 : 0);
		values.put(Columns.HASSPEED, hasSpeed() ? 1 : 0);

		if (_id != -1) {
			SQLiteWrapper.update(context, Columns.TABLE_NAME, values, Columns._ID + "=" + _id, null);
		} else {
			_id = SQLiteWrapper.insert(context, Columns.TABLE_NAME, "", values);
		}
		return _id;
	}

}
