package org.imogene.android.sync.filter;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.HashMap;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.sync.handler.BooleanHandler;
import org.imogene.android.sync.handler.PrimitiveLongHandler;
import org.imogene.android.sync.handler.StringHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class ClientFilterParser {

	private static final HashMap<String, FieldHandler<ClientFilter>> mHandlers = new HashMap<String, FieldHandler<ClientFilter>>();

	static {

		try {
			mHandlers.put(Keys.KEY_ID, new StringHandler<ClientFilter>(ClientFilter.class, "setId"));
			mHandlers.put(Keys.KEY_MODIFIED, new PrimitiveLongHandler<ClientFilter>(ClientFilter.class, "setModified"));
			mHandlers.put(Keys.KEY_MODIFIEDBY, new StringHandler<ClientFilter>(ClientFilter.class, "setModifiedBy"));
			mHandlers.put(Keys.KEY_MODIFIEDFROM, new StringHandler<ClientFilter>(ClientFilter.class, "setModifiedFrom"));
			mHandlers.put(Keys.KEY_UPLOADDATE, new PrimitiveLongHandler<ClientFilter>(ClientFilter.class, "setUploadDate"));
			mHandlers.put(Keys.KEY_CREATED,	new PrimitiveLongHandler<ClientFilter>(ClientFilter.class, "setCreated"));
			mHandlers.put(Keys.KEY_CREATEDBY, new StringHandler<ClientFilter>(ClientFilter.class, "setCreatedBy"));
			mHandlers.put(Keys.KEY_USERID, new StringHandler<ClientFilter>(ClientFilter.class, "setUserId"));
			mHandlers.put(Keys.KEY_TERMINALID, new StringHandler<ClientFilter>(ClientFilter.class, "setTerminalId"));
			mHandlers.put(Keys.KEY_CARDENTITY, new StringHandler<ClientFilter>(ClientFilter.class, "setCardEntity"));
			mHandlers.put(Keys.KEY_CARDENTITY, new StringHandler<ClientFilter>(ClientFilter.class, "setEntityField"));
			mHandlers.put(Keys.KEY_OPERATOR, new StringHandler<ClientFilter>(ClientFilter.class, "setOperator"));
			mHandlers.put(Keys.KEY_FIELDVALUE, new StringHandler<ClientFilter>(ClientFilter.class, "setFieldValue"));
			mHandlers.put(Keys.KEY_ISNEW, new BooleanHandler<ClientFilter>(ClientFilter.class, "setIsNew"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void parse(Context context, XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ClientFilter clientFilter = new ClientFilter();

		clientFilter.reset();

		clientFilter.setUnread(false);
		clientFilter.setSynchronized(true);

		while (parser.getEventType() != END_TAG
				|| !ClientFilter.PACKAGE.equals(parser.getName())) {
			if (parser.next() == START_TAG) {
				String name = parser.getName();
				if (mHandlers.containsKey(name)) {
					mHandlers.get(name).parse(context, parser, clientFilter);
				}
			}
		}

		clientFilter.commit(context, false, false);
	}
}
