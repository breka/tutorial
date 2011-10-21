package org.imogene.android.sync.filter;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.IOException;
import java.util.HashMap;

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
			mHandlers.put(ClientFilter.Columns.ID, new StringHandler<ClientFilter>(ClientFilter.class, "setId"));
			mHandlers.put(ClientFilter.Columns.MODIFIED, new PrimitiveLongHandler<ClientFilter>(ClientFilter.class, "setModified"));
			mHandlers.put(ClientFilter.Columns.MODIFIEDBY, new StringHandler<ClientFilter>(ClientFilter.class, "setModifiedBy"));
			mHandlers.put(ClientFilter.Columns.MODIFIEDFROM, new StringHandler<ClientFilter>(ClientFilter.class, "setModifiedFrom"));
			mHandlers.put(ClientFilter.Columns.UPLOADDATE, new PrimitiveLongHandler<ClientFilter>(ClientFilter.class, "setUploadDate"));
			mHandlers.put(ClientFilter.Columns.CREATED,	new PrimitiveLongHandler<ClientFilter>(ClientFilter.class, "setCreated"));
			mHandlers.put(ClientFilter.Columns.CREATEDBY, new StringHandler<ClientFilter>(ClientFilter.class, "setCreatedBy"));
			mHandlers.put(ClientFilter.Columns.USERID, new StringHandler<ClientFilter>(ClientFilter.class, "setUserId"));
			mHandlers.put(ClientFilter.Columns.TERMINALID, new StringHandler<ClientFilter>(ClientFilter.class, "setTerminalId"));
			mHandlers.put(ClientFilter.Columns.CARDENTITY, new StringHandler<ClientFilter>(ClientFilter.class, "setCardEntity"));
			mHandlers.put(ClientFilter.Columns.CARDENTITY, new StringHandler<ClientFilter>(ClientFilter.class, "setEntityField"));
			mHandlers.put(ClientFilter.Columns.OPERATOR, new StringHandler<ClientFilter>(ClientFilter.class, "setOperator"));
			mHandlers.put(ClientFilter.Columns.FIELDVALUE, new StringHandler<ClientFilter>(ClientFilter.class, "setFieldValue"));
			mHandlers.put(ClientFilter.Columns.ISNEW, new BooleanHandler<ClientFilter>(ClientFilter.class, "setIsNew"));
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
				|| !ClientFilter.Columns.PACKAGE.equals(parser.getName())) {
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
