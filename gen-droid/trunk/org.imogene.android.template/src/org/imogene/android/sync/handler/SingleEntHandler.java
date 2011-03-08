package org.imogene.android.sync.handler;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.lang.reflect.Method;

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

public class SingleEntHandler<T> implements FieldHandler<T> {

	private final Method mMethod;
	private final String mPackage;
	private final Uri mUri;

	public SingleEntHandler(Class<T> c, String methodName, String pack, Uri uri) throws SecurityException, NoSuchMethodException {
		mMethod = c.getMethod(methodName, Uri.class);
		mPackage = pack;
		mUri = uri;
	}

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			if (parser.nextTag() == START_TAG) {
				parser.require(START_TAG, null, mPackage);
				String id = parser.getAttributeValue(null, Keys.KEY_ID);

				ContentResolver res = context.getContentResolver();
				Cursor c = res.query(mUri, new String[] { Keys.KEY_ROWID }, Keys.KEY_ID + "='" + id + "'", null, null);
				if (c.getCount() != 1) {
					c.close();
					ContentValues values = new ContentValues();
					values.put(Keys.KEY_ID, id);
					values.put(Keys.KEY_MODIFIEDFROM, Sync.SYNC_SYSTEM);
					mMethod.invoke(entity, res.insert(mUri, values));
				} else {
					c.moveToFirst();
					long rowId = c.getLong(0);
					c.close();
					mMethod.invoke(entity, ContentUris.withAppendedId(mUri,	rowId));
				}
			}
		} catch (Exception e) {
			Log.e(SingleEntHandler.class.getName(),
					"error parsing single entity's reference", e);
		}
	}

}
