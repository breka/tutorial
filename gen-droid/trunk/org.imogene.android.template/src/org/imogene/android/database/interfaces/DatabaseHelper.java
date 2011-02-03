package org.imogene.android.database.interfaces;

import android.net.Uri;

public interface DatabaseHelper {
	
	public EntityCursor query(Uri uri, String where, String order);
	
	public long queryRowId(Uri uri, String id);
	
	public String queryId(Uri uri);
	
	public Uri findInDatabase(String id);

}
