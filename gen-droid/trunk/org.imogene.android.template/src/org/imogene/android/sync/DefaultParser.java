package org.imogene.android.sync;

import java.io.IOException;

import org.imogene.android.common.Binary;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.common.LocalizedText;
import org.imogene.android.sync.binary.BinaryParser;
import org.imogene.android.sync.filter.ClientFilterParser;
import org.imogene.android.sync.translatable.LocalizedTextParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class DefaultParser {
	
	public static final void parse(Context context, XmlPullParser parser, String name) throws XmlPullParserException, IOException {
		if (Binary.PACKAGE.equals(name))
			BinaryParser.parse(context, parser);
		else if (ClientFilter.PACKAGE.equals(name))
			ClientFilterParser.parse(context, parser);
		else if (LocalizedText.PACKAGE.equals(name))
			LocalizedTextParser.parse(context, parser);
	}

}
