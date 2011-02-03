package org.imogene.android.util.file;

import java.io.IOException;

import org.imogene.android.W;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.res.XmlResourceParser;


public class MimeTypeManager {
	
	private static MimeTypeManager mSingleton;
	private MimeTypes mMimeTypes;
	
	private MimeTypeManager(Context context) {
		MimeTypeParser parser = new MimeTypeParser();
		XmlResourceParser in = context.getResources().getXml(W.xml.mimetypes);
		try {
			mMimeTypes = parser.fromXmlResource(in);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static MimeTypeManager getInstance(Context context) {
		if (mSingleton == null) {
			mSingleton = new MimeTypeManager(context);
		}
		return mSingleton;
	}
	
	public String getExtension(String mimeType) {
		if (mMimeTypes == null) {
			return null;
		}
		return mMimeTypes.getExtension(mimeType);
	}
	
	public String getMimeType(String filename) {
		if (mMimeTypes == null) {
			return null;
		}
		return mMimeTypes.getMimeType(filename);
	}

}
