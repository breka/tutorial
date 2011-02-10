package org.imogene.android.sync.translatable;

import java.io.IOException;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.LocalizedText;
import org.imogene.android.database.sqlite.LocalizedTextCursor;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class LocalizedTextSerializer {

	public static void serialize(Context context,
			LocalizedTextCursor cursor, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {

		serializer.startTag(null, LocalizedText.PACKAGE);

		serializer.startTag(null, Keys.KEY_ID);
		serializer.text(cursor.getId());
		serializer.endTag(null, Keys.KEY_ID);

		serializer.startTag(null, Keys.KEY_MODIFIED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getModified()));
		serializer.endTag(null, Keys.KEY_MODIFIED);

		serializer.startTag(null, Keys.KEY_UPLOADDATE);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getUploadDate()));
		serializer.endTag(null, Keys.KEY_UPLOADDATE);

		serializer.startTag(null, Keys.KEY_MODIFIEDBY);
		serializer.text(cursor.getModifiedBy());
		serializer.endTag(null, Keys.KEY_MODIFIEDBY);

		serializer.startTag(null, Keys.KEY_MODIFIEDFROM);
		serializer.text(cursor.getModifiedFrom());
		serializer.endTag(null, Keys.KEY_MODIFIEDFROM);

		serializer.startTag(null, Keys.KEY_CREATED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getCreated()));
		serializer.endTag(null, Keys.KEY_CREATED);

		serializer.startTag(null, Keys.KEY_CREATEDBY);
		serializer.text(cursor.getCreatedBy());
		serializer.endTag(null, Keys.KEY_CREATEDBY);

		String fieldId = cursor.getFieldId();
		if (fieldId != null) {
			serializer.startTag(null, Keys.KEY_FIELD_ID);
			serializer.text(fieldId);
			serializer.endTag(null, Keys.KEY_FIELD_ID);
		}

		String locale = cursor.getLocale();
		if (locale != null) {
			serializer.startTag(null, Keys.KEY_LOCALE);
			serializer.text(locale);
			serializer.endTag(null, Keys.KEY_LOCALE);
		}

		String value = cursor.getValue();
		if (value != null) {
			serializer.startTag(null, Keys.KEY_VALUE);
			serializer.text(value);
			serializer.endTag(null, Keys.KEY_VALUE);
		}

		serializer.endTag(null, LocalizedText.PACKAGE);

		serializer.flush();

	}

}
