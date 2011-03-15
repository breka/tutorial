package org.imogene.android.util.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.imogene.android.Constants.Extras;
import org.imogene.android.Constants.Intents;
import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Tables;
import org.imogene.android.app.AbstractEntityListing;
import org.imogene.android.database.interfaces.EntityCursor;
import org.imogene.android.database.sqlite.SQLiteBuilder;
import org.imogene.android.provider.AbstractProvider.AbstractDatabase;

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
		values.put(Keys.KEY_HASACCURACY, location.hasAccuracy() ? 1 : 0);
		values.put(Keys.KEY_HASALTITUDE, location.hasAltitude() ? 1 : 0);
		values.put(Keys.KEY_HASBEARING, location.hasBearing() ? 1 : 0);
		values.put(Keys.KEY_HASSPEED, location.hasSpeed() ? 1 : 0);

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
			boolean hasAccuracy = c.getInt(c.getColumnIndexOrThrow(Keys.KEY_HASACCURACY)) != 0;
			boolean hasAltitude = c.getInt(c.getColumnIndexOrThrow(Keys.KEY_HASALTITUDE)) != 0;
			boolean hasBearing = c.getInt(c.getColumnIndexOrThrow(Keys.KEY_HASBEARING)) != 0;
			boolean hasSpeed = c.getInt(c.getColumnIndexOrThrow(Keys.KEY_HASSPEED)) != 0;
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
	
	public static final Intent build(Context context, Uri uri, SQLiteBuilder builder,
			String gpsColumn, String gpsMethod,	double north, double east, double south, double west) {
		SQLiteBuilder b = new SQLiteBuilder();
		if (builder != null)
			b.appendWhere(builder.create());
		
		SQLiteBuilder locations = new SQLiteBuilder(Tables.TABLE_GPSLOCATIONS, Keys.KEY_ROWID);
		locations.appendSup(Keys.KEY_LONGITUDE, west);
		locations.appendInf(Keys.KEY_LONGITUDE, east);
		locations.appendSup(Keys.KEY_LATITUDE, south);
		locations.appendInf(Keys.KEY_LATITUDE, north);
		
		b.appendIn(gpsColumn, locations.create());
		
		String sql = AbstractEntityListing.computeWhere(b).toSQL();

		EntityCursor c = AbstractDatabase.getSuper(context).query(uri, sql, null);
		final int count = c.getCount();
		if (count < 1) {
			return null;
		}

		Intent result = null;
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
