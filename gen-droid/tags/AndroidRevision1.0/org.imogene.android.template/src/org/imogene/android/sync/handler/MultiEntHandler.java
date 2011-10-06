package org.imogene.android.sync.handler;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.imogene.android.Constants.Keys;
import org.imogene.android.Constants.Sync;
import org.imogene.android.sync.FieldHandler;
import org.xmlpull.v1.XmlPullParser;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MultiEntHandler<T> implements FieldHandler<T> {

	private final Method mMethod;
	private final String mPackage;
	private final Uri mUri;

	public MultiEntHandler(Class<T> c, String methodName, String pack, Uri uri) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, (Class[]) null);
		mPackage = pack;
		mUri = uri;
	}

	@SuppressWarnings("unchecked")
	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			if (parser.nextTag() == START_TAG) {
				parser.require(START_TAG, null, "collection");
				
				ArrayList<Uri> list = (ArrayList<Uri>) mMethod.invoke(entity, new Object[0]);
				ContentResolver res = context.getContentResolver();
				while (parser.nextTag() == START_TAG) {
					parser.require(START_TAG, null, mPackage);
					String id = parser.getAttributeValue(null, Keys.KEY_ID);
					Cursor c = res.query(mUri, new String[] { Keys.KEY_ROWID }, Keys.KEY_ID + "='" + id + "'", null, null);
					if (c.getCount() != 1) {
						c.close();
						ContentValues values = new ContentValues();
						values.put(Keys.KEY_ID, id);
						values.put(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
						list.add(res.insert(mUri, values));
					} else {
						c.moveToFirst();
						long rowId = c.getLong(0);
						c.close();
						list.add(ContentUris.withAppendedId(mUri, rowId));
					}
					parser.nextTag();
					parser.require(END_TAG, null, mPackage);
				}
			}
		} catch (Exception e) {
			Log.e(MultiEntHandler.class.getName(), "error parsing multiple entities' references", e);
		}

	}
}
