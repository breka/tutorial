package org.imogene.android.sync.binary;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.HashMap;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.Binary;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.sync.handler.ContentHandler;
import org.imogene.android.sync.handler.PrimitiveLongHandler;
import org.imogene.android.sync.handler.StringHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class BinaryParser {
	
	private static final HashMap<String, FieldHandler<Binary>> mHandlers = new HashMap<String, FieldHandler<Binary>>();

	static {
		try {
			mHandlers.put(Keys.KEY_ID, new StringHandler<Binary>(Binary.class, "setId"));
			mHandlers.put(Keys.KEY_MODIFIED,	new PrimitiveLongHandler<Binary>(Binary.class, "setModified"));
			mHandlers.put(Keys.KEY_MODIFIEDBY, new StringHandler<Binary>(Binary.class, "setModifiedBy"));
			mHandlers.put(Keys.KEY_MODIFIEDFROM, new StringHandler<Binary>(Binary.class, "setModifiedFrom"));
			mHandlers.put(Keys.KEY_UPLOADDATE, new PrimitiveLongHandler<Binary>(Binary.class, "setUploadDate"));
			mHandlers.put(Keys.KEY_CREATED, new PrimitiveLongHandler<Binary>(Binary.class, "setCreated"));
			mHandlers.put(Keys.KEY_CREATEDBY, new StringHandler<Binary>(Binary.class, "setCreatedBy"));
			mHandlers.put(Keys.KEY_PARENT_ENTITY, new StringHandler<Binary>(Binary.class, "setParentEntity"));
			mHandlers.put(Keys.KEY_PARENT_KEY, new StringHandler<Binary>(Binary.class, "setParentKey"));
			mHandlers.put(Keys.KEY_PARENT_FIELD_GETTER, new StringHandler<Binary>(Binary.class, "setParentFieldGetter"));
			mHandlers.put(Keys.KEY_FILE_NAME, new StringHandler<Binary>(Binary.class, "setFileName"));
			mHandlers.put(Keys.KEY_CONTENT_TYPE, new StringHandler<Binary>(Binary.class, "setContentType"));
			mHandlers.put(Keys.KEY_LENGTH, new PrimitiveLongHandler<Binary>(Binary.class, "setLength"));
			mHandlers.put("content", new ContentHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void parse(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
		Binary binary = new Binary();

		while (parser.getEventType() != END_TAG
				|| !Binary.PACKAGE.equals(parser.getName())) {
			if (parser.next() == START_TAG) {
				String name = parser.getName();
				if (mHandlers.containsKey(name)) {
					mHandlers.get(name).parse(context, parser, binary);
				}
			}
		}

		binary.commit(context, false, false);
	}
}
