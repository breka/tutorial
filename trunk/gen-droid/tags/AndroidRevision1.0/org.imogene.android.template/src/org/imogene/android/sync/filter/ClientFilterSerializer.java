package org.imogene.android.sync.filter;

import java.io.IOException;

import org.imogene.android.Constants.Keys;
import org.imogene.android.common.ClientFilter;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class ClientFilterSerializer {

	public static void serialize(Context context, ClientFilterCursor cursor,
			XmlSerializer serializer) throws IllegalArgumentException,
			IllegalStateException, IOException {

		serializer.startTag(null, ClientFilter.PACKAGE);

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

		serializer.startTag(null, Keys.KEY_USERID);
		serializer.text(cursor.getUserId());
		serializer.endTag(null, Keys.KEY_USERID);

		serializer.startTag(null, Keys.KEY_TERMINALID);
		serializer.text(cursor.getTerminalId());
		serializer.endTag(null, Keys.KEY_TERMINALID);

		serializer.startTag(null, Keys.KEY_CARDENTITY);
		serializer.text(cursor.getCardEntity());
		serializer.endTag(null, Keys.KEY_CARDENTITY);

		serializer.startTag(null, Keys.KEY_ENTITYFIELD);
		serializer.text(cursor.getEntityField());
		serializer.endTag(null, Keys.KEY_ENTITYFIELD);

		String op = cursor.getOperator();
		if (op != null) {
			serializer.startTag(null, Keys.KEY_OPERATOR);
			serializer.text(op);
			serializer.endTag(null, Keys.KEY_OPERATOR);
		}

		String fieldvalue = cursor.getFieldValue();
		if (fieldvalue != null) {
			serializer.startTag(null, Keys.KEY_FIELDVALUE);
			serializer.text(fieldvalue);
			serializer.endTag(null, Keys.KEY_FIELDVALUE);
		}
		
		Boolean isNew = cursor.getIsNew();
		if (isNew != null) {
			serializer.startTag(null, Keys.KEY_ISNEW);
			serializer.text(isNew.toString());
			serializer.endTag(null, Keys.KEY_ISNEW);
		}

		serializer.endTag(null, ClientFilter.PACKAGE);

		serializer.flush();

	}

}
