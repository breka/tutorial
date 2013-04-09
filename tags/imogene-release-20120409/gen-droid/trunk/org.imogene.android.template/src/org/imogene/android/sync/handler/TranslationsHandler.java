package org.imogene.android.sync.handler;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import org.imogene.android.common.LocalizedText;
import org.imogene.android.common.interfaces.Entity;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.sync.translatable.LocalizedTextParser;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class TranslationsHandler<T> implements FieldHandler<T> {
	
	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			while (parser.getEventType() != END_TAG || !Entity.Columns.TRANSLATIONS_TAG.equals(parser.getName())) {
				if (parser.nextTag() == START_TAG) {
					String name = parser.getName();
					if (LocalizedText.Columns.PACKAGE.equals(name)) {
						LocalizedTextParser.parse(context, parser);
					}
				}
			}
		} catch (Exception e) {
			Log.e(BinaryHandler.class.getName(), "error parsing translations", e);
		}
	}
}