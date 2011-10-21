package org.imogene.android.sync.binary;
import java.io.IOException;
import java.io.InputStream;

import org.imogene.android.common.Binary;
import org.imogene.android.database.sqlite.BinaryCursor;
import org.imogene.android.util.base64.Base64;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;

public class BinarySerializer {

	public static final String PACKAGE = "org.imogene.data.Binary";
	
	public static void serialize(Context context, BinaryCursor cursor,
			XmlSerializer serializer) throws IllegalArgumentException,
			IllegalStateException, IOException {
		
		serializer.startTag(null, PACKAGE);
		
		serializer.startTag(null, Binary.Columns.ID);
		serializer.text(cursor.getId());
		serializer.endTag(null, Binary.Columns.ID);
		
		serializer.startTag(null, Binary.Columns.MODIFIED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getModified()));
		serializer.endTag(null, Binary.Columns.MODIFIED);

		serializer.startTag(null, Binary.Columns.UPLOADDATE);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getUploadDate()));
		serializer.endTag(null, Binary.Columns.UPLOADDATE);

		serializer.startTag(null, Binary.Columns.MODIFIEDBY);
		serializer.text(cursor.getModifiedBy());
		serializer.endTag(null, Binary.Columns.MODIFIEDBY);

		serializer.startTag(null, Binary.Columns.MODIFIEDFROM);
		serializer.text(cursor.getModifiedFrom());
		serializer.endTag(null, Binary.Columns.MODIFIEDFROM);

		serializer.startTag(null, Binary.Columns.CREATED);
		serializer.attribute(null, "class", "sql-timestamp");
		serializer.text(String.valueOf(cursor.getCreated()));
		serializer.endTag(null, Binary.Columns.CREATED);

		serializer.startTag(null, Binary.Columns.CREATEDBY);
		serializer.text(cursor.getCreatedBy());
		serializer.endTag(null, Binary.Columns.CREATEDBY);
		
		serializer.startTag(null, Binary.Columns.FILE_NAME);
		serializer.text(cursor.getFileName());
		serializer.endTag(null, Binary.Columns.FILE_NAME);
		
		serializer.startTag(null, Binary.Columns.CONTENT_TYPE);
		serializer.text(cursor.getContentType());
		serializer.endTag(null, Binary.Columns.CONTENT_TYPE);
		
		serializer.startTag(null, Binary.Columns.LENGTH);
		serializer.text(String.valueOf(cursor.getLength()));
		serializer.endTag(null, Binary.Columns.LENGTH);
		
		serializer.startTag(null, "content");
		
		Uri uri = ContentUris.withAppendedId(Binary.Columns.CONTENT_URI, cursor.getRowId());
		InputStream is = context.getContentResolver().openInputStream(uri);

		byte[] bytes = new byte[1024];
		while (is.read(bytes) != -1) {
			serializer.startTag(null, "data");
			serializer.text(new String(Base64.encodeBase64(bytes)));
			serializer.endTag(null, "data");
		}
		
		serializer.endTag(null, "content");
		
		serializer.startTag(null, Binary.Columns.PARENT_ENTITY);
		serializer.text(cursor.getParentEntity());
		serializer.endTag(null, Binary.Columns.PARENT_ENTITY);
		
		serializer.startTag(null, Binary.Columns.PARENT_KEY);
		serializer.text(cursor.getParentKey());
		serializer.endTag(null, Binary.Columns.PARENT_KEY);
		
		serializer.startTag(null, Binary.Columns.PARENT_FIELD_GETTER);
		serializer.text(cursor.getParentFieldGetter());
		serializer.endTag(null, Binary.Columns.PARENT_FIELD_GETTER);
		
		serializer.endTag(null, PACKAGE);
	}
}
