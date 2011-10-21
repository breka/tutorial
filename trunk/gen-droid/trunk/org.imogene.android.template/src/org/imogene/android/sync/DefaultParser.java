package org.imogene.android.sync;

import java.io.IOException;

import org.imogene.android.common.Binary;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.sync.binary.BinaryParser;
import org.imogene.android.sync.filter.ClientFilterParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class DefaultParser {
	
	public static final void parse(Context context, XmlPullParser parser, String name) throws XmlPullParserException, IOException {
		if (Binary.Columns.PACKAGE.equals(name))
			BinaryParser.parse(context, parser);
		else if (ClientFilter.Columns.PACKAGE.equals(name))
			ClientFilterParser.parse(context, parser);
	}

}
