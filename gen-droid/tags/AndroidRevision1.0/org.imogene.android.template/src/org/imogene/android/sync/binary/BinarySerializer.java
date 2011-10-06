package org.imogene.android.sync.binary;
import java.io.IOException;
import java.io.InputStream;

import org.imogene.android.Constants.Keys;
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
		
		serializer.startTag(null, Keys.KEY_FILE_NAME);
		serializer.text(cursor.getFileName());
		serializer.endTag(null, Keys.KEY_FILE_NAME);
		
		serializer.startTag(null, Keys.KEY_CONTENT_TYPE);
		serializer.text(cursor.getContentType());
		serializer.endTag(null, Keys.KEY_CONTENT_TYPE);
		
		serializer.startTag(null, Keys.KEY_LENGTH);
		serializer.text(String.valueOf(cursor.getLength()));
		serializer.endTag(null, Keys.KEY_LENGTH);
		
		serializer.startTag(null, "content");
		
		Uri uri = ContentUris.withAppendedId(Binary.CONTENT_URI, cursor.getRowId());
		InputStream is = context.getContentResolver().openInputStream(uri);

		byte[] bytes = new byte[1024];
		while (is.read(bytes) != -1) {
			serializer.startTag(null, "data");
			serializer.text(new String(Base64.encodeBase64(bytes)));
			serializer.endTag(null, "data");
		}
		
		serializer.endTag(null, "content");
		
		serializer.startTag(null, Keys.KEY_PARENT_ENTITY);
		serializer.text(cursor.getParentEntity());
		serializer.endTag(null, Keys.KEY_PARENT_ENTITY);
		
		serializer.startTag(null, Keys.KEY_PARENT_KEY);
		serializer.text(cursor.getParentKey());
		serializer.endTag(null, Keys.KEY_PARENT_KEY);
		
		serializer.startTag(null, Keys.KEY_PARENT_FIELD_GETTER);
		serializer.text(cursor.getParentFieldGetter());
		serializer.endTag(null, Keys.KEY_PARENT_FIELD_GETTER);
		
		serializer.endTag(null, PACKAGE);
	}
}
