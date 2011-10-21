package org.imogene.android.sync.translatable;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.HashMap;

import org.imogene.android.common.LocalizedText;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.sync.handler.PrimitiveBooleanHandler;
import org.imogene.android.sync.handler.PrimitiveLongHandler;
import org.imogene.android.sync.handler.StringHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class LocalizedTextParser {

	private static final HashMap<String, FieldHandler<LocalizedText>> mHandlers = new HashMap<String, FieldHandler<LocalizedText>>();

	static {
		try {
			mHandlers.put(LocalizedText.Columns.MODIFIED, new PrimitiveLongHandler<LocalizedText>(LocalizedText.class, "setModified"));
			mHandlers.put(LocalizedText.Columns.MODIFIEDBY, new StringHandler<LocalizedText>(LocalizedText.class, "setModifiedBy"));
			mHandlers.put(LocalizedText.Columns.MODIFIEDFROM, new StringHandler<LocalizedText>(LocalizedText.class, "setModifiedFrom"));
			mHandlers.put(LocalizedText.Columns.CREATED,	new PrimitiveLongHandler<LocalizedText>(LocalizedText.class, "setCreated"));
			mHandlers.put(LocalizedText.Columns.CREATEDBY, new StringHandler<LocalizedText>(LocalizedText.class, "setCreatedBy"));
			mHandlers.put(LocalizedText.Columns.FIELD_ID, new StringHandler<LocalizedText>(LocalizedText.class, "setFieldId"));
			mHandlers.put(LocalizedText.Columns.LOCALE, new StringHandler<LocalizedText>(LocalizedText.class, "setLocale"));
			mHandlers.put(LocalizedText.Columns.VALUE, new StringHandler<LocalizedText>(LocalizedText.class, "setValue"));
			mHandlers.put(LocalizedText.Columns.ORIGINAL_VALUE, new PrimitiveBooleanHandler<LocalizedText>(LocalizedText.class, "setOriginalValue"));
			mHandlers.put(LocalizedText.Columns.POTENTIALY_WRONG, new PrimitiveBooleanHandler<LocalizedText>(LocalizedText.class, "setPotentialyWrong"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void parse(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
		LocalizedText localizedText = new LocalizedText();

		localizedText.reset();

		localizedText.setUnread(true);
		localizedText.setSynchronized(true);

		localizedText.setId(parser.getAttributeValue(null, LocalizedText.Columns.ID));
		
		while (parser.getEventType() != END_TAG
				|| !LocalizedText.Columns.PACKAGE.equals(parser.getName())) {
			if (parser.next() == START_TAG) {
				String name = parser.getName();
				if (mHandlers.containsKey(name)) {
					mHandlers.get(name).parse(context, parser, localizedText);
				}
			}
		}

		localizedText.commit(context, false, false);
	}
}
