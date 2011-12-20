package org.imogene.android.common;

import org.imogene.android.database.sqlite.SQLiteWrapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
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
