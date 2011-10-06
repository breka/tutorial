package org.imogene.android.sync.handler;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

import org.imogene.android.common.interfaces.User;
import org.imogene.android.sync.FieldHandler;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Log;

public class RolesHandler<T extends User> implements FieldHandler<T> {

	public void parse(Context context, XmlPullParser parser, T entity) {
		try {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			while (parser.nextTag() == START_TAG) {
				if (first)
					first = false;
				else
					builder.append(';');
				builder.append(parser.nextText());
			}
			entity.setRoles(builder.toString());
		} catch (Exception e) {
			Log.e(ContentHandler.class.getName(), "error parsing binary content", e);
		}
	}

}
