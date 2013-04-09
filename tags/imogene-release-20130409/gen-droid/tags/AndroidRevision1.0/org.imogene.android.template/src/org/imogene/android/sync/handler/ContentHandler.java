package org.imogene.android.sync.handler;

import static org.xmlpull.v1.XmlPullParser.START_TAG;

import java.io.File;
import java.io.FileOutputStream;

import org.imogene.android.Constants.Paths;
import org.imogene.android.common.Binary;
import org.imogene.android.sync.FieldHandler;
import org.imogene.android.util.base64.Base64;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class ContentHandler implements FieldHandler<Binary>{

	public void parse(Context context, XmlPullParser parser, Binary entity) {
		try {
			File tmp = new File(Paths.PATH_TEMPORARY);
			tmp.mkdirs();
			File file = File.createTempFile("tmp", ".bin", tmp);
			FileOutputStream fos = new FileOutputStream(file);
		
			while (parser.nextTag() == START_TAG) {
				String value = parser.nextText();
				fos.write(Base64.decodeBase64(value.getBytes()));
			}
		
			fos.flush();
			fos.close();

			entity.setData(Uri.fromFile(file));
		} catch (Exception e) {
			Log.e(ContentHandler.class.getName(), "error parsing binary content", e);
		}
	}
}
