package org.imogene.android.sync.binary;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.HashMap;

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
			mHandlers.put(Binary.Columns.ID, new StringHandler<Binary>(Binary.class, "setId"));
			mHandlers.put(Binary.Columns.MODIFIED,	new PrimitiveLongHandler<Binary>(Binary.class, "setModified"));
			mHandlers.put(Binary.Columns.MODIFIEDBY, new StringHandler<Binary>(Binary.class, "setModifiedBy"));
			mHandlers.put(Binary.Columns.MODIFIEDFROM, new StringHandler<Binary>(Binary.class, "setModifiedFrom"));
			mHandlers.put(Binary.Columns.UPLOADDATE, new PrimitiveLongHandler<Binary>(Binary.class, "setUploadDate"));
			mHandlers.put(Binary.Columns.CREATED, new PrimitiveLongHandler<Binary>(Binary.class, "setCreated"));
			mHandlers.put(Binary.Columns.CREATEDBY, new StringHandler<Binary>(Binary.class, "setCreatedBy"));
			mHandlers.put(Binary.Columns.PARENT_ENTITY, new StringHandler<Binary>(Binary.class, "setParentEntity"));
			mHandlers.put(Binary.Columns.PARENT_KEY, new StringHandler<Binary>(Binary.class, "setParentKey"));
			mHandlers.put(Binary.Columns.PARENT_FIELD_GETTER, new StringHandler<Binary>(Binary.class, "setParentFieldGetter"));
			mHandlers.put(Binary.Columns.FILE_NAME, new StringHandler<Binary>(Binary.class, "setFileName"));
			mHandlers.put(Binary.Columns.CONTENT_TYPE, new StringHandler<Binary>(Binary.class, "setContentType"));
			mHandlers.put(Binary.Columns.LENGTH, new PrimitiveLongHandler<Binary>(Binary.class, "setLength"));
			mHandlers.put("content", new ContentHandler());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void parse(Context context, XmlPullParser parser) throws XmlPullParserException, IOException {
		Binary binary = new Binary();

		while (parser.getEventType() != END_TAG
				|| !Binary.Columns.PACKAGE.equals(parser.getName())) {
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
