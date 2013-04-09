package org.imogene.android.sync.handler;

import java.lang.reflect.Method;

import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Sync;
import org.imogene.android.common.Binary;
import org.imogene.android.sync.FieldHandler;
import org.xmlpull.v1.XmlPullParser;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class BinaryHandler<T> implements FieldHandler<T> {
	
	private final Method mMethod;

	public BinaryHandler(Class<T> c, String methodName) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, Uri.class);
	}

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			String id = parser.nextText();
			ContentResolver res = context.getContentResolver();
			Cursor c = res.query(Binary.CONTENT_URI, new String[] {Keys.KEY_ROWID}, Keys.KEY_ID + "='" + id + "'", null, null);
			if (c.getCount() != 1) {
				c.close();
				ContentValues values = new ContentValues();
				values.put(Keys.KEY_ID, id);
				values.put(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
				mMethod.invoke(entity, res.insert(Binary.CONTENT_URI, values));
			} else {
				c.moveToFirst();
				long rowId = c.getLong(0);
				c.close();
				mMethod.invoke(entity, ContentUris.withAppendedId(Binary.CONTENT_URI, rowId));
			}
		} catch (Exception e) {
			Log.e(BinaryHandler.class.getName(), "error parsing binary", e);
		}
	}
}
