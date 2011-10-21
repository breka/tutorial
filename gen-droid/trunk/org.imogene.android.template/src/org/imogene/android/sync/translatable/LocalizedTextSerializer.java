package org.imogene.android.sync.translatable;

import java.io.IOException;

import org.imogene.android.common.LocalizedText;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class LocalizedTextSerializer {

	public static void serialize(Context context,
			LocalizedText localized, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {

		serializer.startTag(null, LocalizedText.Columns.PACKAGE);
		serializer.attribute(null, LocalizedText.Columns.ID, localized.getId());
		
		String fieldId = localized.getFieldId();
		if (fieldId != null) {
			serializer.startTag(null, LocalizedText.Columns.FIELD_ID);
			serializer.text(fieldId);
			serializer.endTag(null, LocalizedText.Columns.FIELD_ID);
		}
		
		String locale = localized.getLocale();
		if (locale != null) {
			serializer.startTag(null, LocalizedText.Columns.LOCALE);
			serializer.text(locale);
			serializer.endTag(null, LocalizedText.Columns.LOCALE);
		}

		String value = localized.getValue();
		if (value != null) {
			serializer.startTag(null, LocalizedText.Columns.VALUE);
			serializer.text(value);
			serializer.endTag(null, LocalizedText.Columns.VALUE);
		}
		
		serializer.startTag(null, LocalizedText.Columns.ORIGINAL_VALUE);
		serializer.text(Boolean.toString(localized.getOriginalValue()));
		serializer.endTag(null, LocalizedText.Columns.ORIGINAL_VALUE);
		
		serializer.startTag(null, LocalizedText.Columns.POTENTIALY_WRONG);
		serializer.text(Boolean.toString(localized.getPotentialyWrong()));
		serializer.endTag(null, LocalizedText.Columns.POTENTIALY_WRONG);		

		serializer.startTag(null, LocalizedText.Columns.MODIFIED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(localized.getModified()));
		serializer.endTag(null, LocalizedText.Columns.MODIFIED);

		serializer.startTag(null, LocalizedText.Columns.MODIFIEDBY);
		serializer.text(localized.getModifiedBy());
		serializer.endTag(null, LocalizedText.Columns.MODIFIEDBY);

		serializer.startTag(null, LocalizedText.Columns.MODIFIEDFROM);
		serializer.text(localized.getModifiedFrom());
		serializer.endTag(null, LocalizedText.Columns.MODIFIEDFROM);

		serializer.startTag(null, LocalizedText.Columns.CREATED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(localized.getCreated()));
		serializer.endTag(null, LocalizedText.Columns.CREATED);

		serializer.startTag(null, LocalizedText.Columns.CREATEDBY);
		serializer.text(localized.getCreatedBy());
		serializer.endTag(null, LocalizedText.Columns.CREATEDBY);

		serializer.endTag(null, LocalizedText.Columns.PACKAGE);

		serializer.flush();

	}

}
