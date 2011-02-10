package org.imogene.android.sync.translatable;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.HashMap;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.LocalizedText;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.sync.handler.PrimitiveLongHandler;
import org.imogene.android.sync.handler.StringHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class LocalizedTextParser {

	private static final HashMap<String, FieldHandler<LocalizedText>> mHandlers = new HashMap<String, FieldHandler<LocalizedText>>();

	static {
		try {
			mHandlers.put(Keys.KEY_ID, new StringHandler<LocalizedText>(LocalizedText.class, "setId"));
			mHandlers.put(Keys.KEY_MODIFIED, new PrimitiveLongHandler<LocalizedText>(LocalizedText.class, "setModified"));
			mHandlers.put(Keys.KEY_MODIFIEDBY, new StringHandler<LocalizedText>(LocalizedText.class, "setModifiedBy"));
			mHandlers.put(Keys.KEY_MODIFIEDFROM, new StringHandler<LocalizedText>(LocalizedText.class, "setModifiedFrom"));
			mHandlers.put(Keys.KEY_UPLOADDATE, new PrimitiveLongHandler<LocalizedText>(LocalizedText.class, "setUploadDate"));
			mHandlers.put(Keys.KEY_CREATED,	new PrimitiveLongHandler<LocalizedText>(LocalizedText.class, "setCreated"));
			mHandlers.put(Keys.KEY_CREATEDBY, new StringHandler<LocalizedText>(LocalizedText.class, "setCreatedBy"));
			mHandlers.put(Keys.KEY_FIELD_ID, new StringHandler<LocalizedText>(LocalizedText.class, "setFieldId"));
			mHandlers.put(Keys.KEY_LOCALE, new StringHandler<LocalizedText>(LocalizedText.class, "setLocale"));
			mHandlers.put(Keys.KEY_VALUE, new StringHandler<LocalizedText>(LocalizedText.class, "setValue"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void parse(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
		LocalizedText localizedText = new LocalizedText();

		localizedText.reset();

		localizedText.setUnread(true);
		localizedText.setSynchronized(true);

		while (parser.getEventType() != END_TAG
				|| !LocalizedText.PACKAGE.equals(parser.getName())) {
			if (parser.next() == START_TAG) {
				String name = parser.getName();
				if (mHandlers.containsKey(name)) {
					mHandlers.get(name)
							.parse(context, parser, localizedText);
				}
			}
		}

		localizedText.commit(context, false, false);
	}
}
