package org.imogene.android.util.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Tables;
import org.imogene.android.app.AbstractEntityListing;
import org.imogene.android.database.AbstractDatabase;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

public class GpsTableUtils {

	private static final String EXTRA_GPS_ROW = "org.imogene.android.database.gpstablehelper.extra.GPS_ROW";

	public static final long saveLocation(SQLiteDatabase db, Location location) {
		if (location == null) return -1;
		Bundle extras = location.getExtras();
		if (extras != null && extras.containsKey(EXTRA_GPS_ROW)) {
			return extras.getLong(EXTRA_GPS_ROW, -1);
		}
		
		ContentValues values = new ContentValues();
		values.put(Keys.KEY_ACCURACY, location.getAccuracy());
		values.put(Keys.KEY_ALTITUDE, location.getAltitude());
		values.put(Keys.KEY_BEARING, location.getBearing());
		values.put(Keys.KEY_LATITUDE, location.getLatitude());
		values.put(Keys.KEY_LONGITUDE, location.getLongitude());
		values.put(Keys.KEY_PROVIDER, location.getProvider());
		values.put(Keys.KEY_SPEED, location.getSpeed());
		values.put(Keys.KEY_TIME, location.getTime());
		values.put(Keys.KEY_HASACCURACY, location.hasAccuracy());
		values.put(Keys.KEY_HASALTITUDE, location.hasAltitude());
		values.put(Keys.KEY_HASBEARING, location.hasBearing());
		values.put(Keys.KEY_HASSPEED, location.hasSpeed());

		return db.insert(Tables.TABLE_GPSLOCATIONS, "", values);
	}

	public static final Location getLocation(SQLiteDatabase db, long row) {
		if (row == -1) return null;
		Cursor c = db.query(Tables.TABLE_GPSLOCATIONS, null, Keys.KEY_ROWID + "=" + row,
				null, null, null, null);
		if (c.getCount() == 1) {
			c.moveToFirst();

			float accuracy = c.getFloat(c.getColumnIndexOrThrow(Keys.KEY_ACCURACY));
			double altitude = c.getDouble(c.getColumnIndexOrThrow(Keys.KEY_ALTITUDE));
			float bearing = c.getFloat(c.getColumnIndexOrThrow(Keys.KEY_BEARING));
			double latitude = c.getDouble(c.getColumnIndexOrThrow(Keys.KEY_LATITUDE));
			double longitude = c.getDouble(c.getColumnIndexOrThrow(Keys.KEY_LONGITUDE));
			String provider = c.getString(c.getColumnIndexOrThrow(Keys.KEY_PROVIDER));
			float speed = c.getFloat(c.getColumnIndexOrThrow(Keys.KEY_SPEED));
			long time = c.getLong(c.getColumnIndexOrThrow(Keys.KEY_TIME));
			boolean hasAccuracy = Boolean.valueOf(c.getString(c.getColumnIndexOrThrow(Keys.KEY_HASACCURACY))).booleanValue();
			boolean hasAltitude = Boolean.valueOf(c.getString(c.getColumnIndexOrThrow(Keys.KEY_HASALTITUDE))).booleanValue();
			boolean hasBearing = Boolean.valueOf(c.getString(c.getColumnIndexOrThrow(Keys.KEY_HASBEARING))).booleanValue();
			boolean hasSpeed = Boolean.valueOf(c.getString(c.getColumnIndexOrThrow(Keys.KEY_HASSPEED))).booleanValue();
			c.close();
			
			Location loc = new Location(provider);
			if (hasAccuracy)
				loc.setAccuracy(accuracy);
			if (hasAltitude)
				loc.setAltitude(altitude);
			if (hasBearing)
				loc.setBearing(bearing);
			loc.setLatitude(latitude);
			loc.setLongitude(longitude);
			if (hasSpeed)
				loc.setSpeed(speed);
			loc.setTime(time);

			Bundle extras = new Bundle();
			extras.putLong(EXTRA_GPS_ROW, row);
			loc.setExtras(extras);
			
			return loc;
		} else {
			c.close();
			return null;
		}
	}
	
	private static final long[] getLocationsAround(SQLiteDatabase db, double latitude, double longitude, float distance) {
		final String[] columns = new String[] {Keys.KEY_ROWID, Keys.KEY_LATITUDE, Keys.KEY_LONGITUDE};
		final Cursor c = db.query(Tables.TABLE_GPSLOCATIONS, columns, null, null, null, null, null);
		final int crow = c.getColumnIndexOrThrow(Keys.KEY_ROWID);
		final int clat = c.getColumnIndexOrThrow(Keys.KEY_LATITUDE);
		final int clon = c.getColumnIndexOrThrow(Keys.KEY_LONGITUDE);
		final float[] results = new float[1];
		double lat;
		double lon;

		final int count = c.getCount();
		ArrayList<Long> list = new ArrayList<Long>();
		for (int i = 0; i < count; i++) {
			c.moveToPosition(i);
			lat = c.getDouble(clat);
			lon = c.getDouble(clon);
			Location.distanceBetween(lat, lon, latitude, longitude, results);
			if (results[0] < distance) {
				list.add(c.getLong(crow));
			}
		}
		c.close();
		
		final int size = list.size();
		long[] result = new long[size];
		for (int i = 0; i < size; i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	public static final Intent build(Context context, Uri uri, SQLiteBuilder builder,
			String gpsColumn, String gpsMethod,	Location location, float distance) {
		SQLiteDatabase db = AbstractDatabase.getSuper(context).getReadableDatabase();
		
		Intent result = null;
				
		long[] ids = getLocationsAround(db, location.getLatitude(), location.getLongitude(), distance);
		SQLiteBuilder b = new SQLiteBuilder();
		if (builder != null)
			b.appendWhere(builder.create());
		b.appendIn(gpsColumn, ids);
		
		String sql = AbstractEntityListing.computeWhere(b).toSQL();

		EntityCursor c = AbstractDatabase.getSuper(context).query(uri, sql, null);
		final int count = c.getCount();
		if (count < 1)
			return result;

		try {
			final Method method = c.getClass().getDeclaredMethod(gpsMethod, null);
			final String[] titles = new String[count];
			final String[] descriptions = new String[count];
			final double[] latitudes = new double[count];
			final double[] longitudes = new double[count];
			final String[] uris = new String[count];
			for (int i = 0; i < count; i++) {
				c.moveToPosition(i);
				titles[i] = c.getMainDisplay(context);
				descriptions[i] = c.getSecondaryDisplay(context);
				Location l = (Location) method.invoke(c, null);
				latitudes[i] = l.getLatitude();
				longitudes[i] = l.getLongitude();
				uris[i] = ContentUris.withAppendedId(uri, c.getRowId()).toString();
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
}
