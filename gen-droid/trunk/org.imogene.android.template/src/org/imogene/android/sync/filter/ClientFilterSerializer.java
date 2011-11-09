package org.imogene.android.sync.filter;

import java.io.IOException;

import org.imogene.android.common.ClientFilter;
import org.imogene.android.database.sqlite.ClientFilterCursor;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class ClientFilterSerializer {

	public static void serialize(Context context, ClientFilterCursor cursor,
			XmlSerializer serializer) throws IllegalArgumentException,
			IllegalStateException, IOException {

		serializer.startTag(null, ClientFilter.Columns.PACKAGE);

		serializer.startTag(null, "id");
		serializer.text(cursor.getId());
		serializer.endTag(null, "id");

		serializer.startTag(null, ClientFilter.Columns.MODIFIED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getModified()));
		serializer.endTag(null, ClientFilter.Columns.MODIFIED);

		serializer.startTag(null, ClientFilter.Columns.UPLOADDATE);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getUploadDate()));
		serializer.endTag(null, ClientFilter.Columns.UPLOADDATE);

		serializer.startTag(null, ClientFilter.Columns.MODIFIEDBY);
		serializer.text(cursor.getModifiedBy());
		serializer.endTag(null, ClientFilter.Columns.MODIFIEDBY);

		serializer.startTag(null, ClientFilter.Columns.MODIFIEDFROM);
		serializer.text(cursor.getModifiedFrom());
		serializer.endTag(null, ClientFilter.Columns.MODIFIEDFROM);

		serializer.startTag(null, ClientFilter.Columns.CREATED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getCreated()));
		serializer.endTag(null, ClientFilter.Columns.CREATED);

		serializer.startTag(null, ClientFilter.Columns.CREATEDBY);
		serializer.text(cursor.getCreatedBy());
		serializer.endTag(null, ClientFilter.Columns.CREATEDBY);

		serializer.startTag(null, ClientFilter.Columns.USERID);
		serializer.text(cursor.getUserId());
		serializer.endTag(null, ClientFilter.Columns.USERID);

		serializer.startTag(null, ClientFilter.Columns.TERMINALID);
		serializer.text(cursor.getTerminalId());
		serializer.endTag(null, ClientFilter.Columns.TERMINALID);

		serializer.startTag(null, ClientFilter.Columns.CARDENTITY);
		serializer.text(cursor.getCardEntity());
		serializer.endTag(null, ClientFilter.Columns.CARDENTITY);

		serializer.startTag(null, ClientFilter.Columns.ENTITYFIELD);
		serializer.text(cursor.getEntityField());
		serializer.endTag(null, ClientFilter.Columns.ENTITYFIELD);

		String op = cursor.getOperator();
		if (op != null) {
			serializer.startTag(null, ClientFilter.Columns.OPERATOR);
			serializer.text(op);
			serializer.endTag(null, ClientFilter.Columns.OPERATOR);
		}

		String fieldvalue = cursor.getFieldValue();
		if (fieldvalue != null) {
			serializer.startTag(null, ClientFilter.Columns.FIELDVALUE);
			serializer.text(fieldvalue);
			serializer.endTag(null, ClientFilter.Columns.FIELDVALUE);
		}
		
		Boolean isNew = cursor.getIsNew();
		if (isNew != null) {
			serializer.startTag(null, ClientFilter.Columns.ISNEW);
			serializer.text(isNew.toString());
			serializer.endTag(null, ClientFilter.Columns.ISNEW);
		}

		serializer.endTag(null, ClientFilter.Columns.PACKAGE);

		serializer.flush();

	}

}
