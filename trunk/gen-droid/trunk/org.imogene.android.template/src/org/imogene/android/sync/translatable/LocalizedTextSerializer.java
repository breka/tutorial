package org.imogene.android.sync.translatable;

import java.io.IOException;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.LocalizedText;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class LocalizedTextSerializer {

	public static void serialize(Context context,
			LocalizedText localized, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {

		serializer.startTag(null, LocalizedText.PACKAGE);
		serializer.attribute(null, Keys.KEY_ID, localized.getId());
		
		String fieldId = localized.getFieldId();
		if (fieldId != null) {
			serializer.startTag(null, Keys.KEY_FIELD_ID);
			serializer.text(fieldId);
			serializer.endTag(null, Keys.KEY_FIELD_ID);
		}
		
		String locale = localized.getLocale();
		if (locale != null) {
			serializer.startTag(null, Keys.KEY_LOCALE);
			serializer.text(locale);
			serializer.endTag(null, Keys.KEY_LOCALE);
		}

		String value = localized.getValue();
		if (value != null) {
			serializer.startTag(null, Keys.KEY_VALUE);
			serializer.text(value);
			serializer.endTag(null, Keys.KEY_VALUE);
		}
		
		serializer.startTag(null, Keys.KEY_ORIGINAL_VALUE);
		serializer.text(Boolean.toString(localized.getOriginalValue()));
		serializer.endTag(null, Keys.KEY_ORIGINAL_VALUE);
		
		serializer.startTag(null, Keys.KEY_POTENTIALY_WRONG);
		serializer.text(Boolean.toString(localized.getPotentialyWrong()));
		serializer.endTag(null, Keys.KEY_POTENTIALY_WRONG);		

		serializer.startTag(null, Keys.KEY_MODIFIED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(localized.getModified()));
		serializer.endTag(null, Keys.KEY_MODIFIED);

		serializer.startTag(null, Keys.KEY_MODIFIEDBY);
		serializer.text(localized.getModifiedBy());
		serializer.endTag(null, Keys.KEY_MODIFIEDBY);

		serializer.startTag(null, Keys.KEY_MODIFIEDFROM);
		serializer.text(localized.getModifiedFrom());
		serializer.endTag(null, Keys.KEY_MODIFIEDFROM);

		serializer.startTag(null, Keys.KEY_CREATED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(localized.getCreated()));
		serializer.endTag(null, Keys.KEY_CREATED);

		serializer.startTag(null, Keys.KEY_CREATEDBY);
		serializer.text(localized.getCreatedBy());
		serializer.endTag(null, Keys.KEY_CREATEDBY);

		serializer.endTag(null, LocalizedText.PACKAGE);

		serializer.flush();

	}

}
