package org.imogene.android.common.interfaces;

import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;

public interface Entity {
	
	public static interface Columns extends BaseColumns {
		public static final String ID = "id";
		public static final String MODIFIED = "modified";
		public static final String UPLOADDATE = "uploadDate";
		public static final String MODIFIEDBY = "modifiedBy";
		public static final String MODIFIEDFROM = "modifiedFrom";
		public static final String CREATED = "created";
		public static final String CREATEDBY = "createdBy";
		public static final String UNREAD = "unread";
		public static final String SYNCHRONIZED = "synchronized";
		
		public static final String TRANSLATIONS_TAG = "translations";
		
		public static final String SYNC_SYSTEM = "sync-system";
	}
	
	public long getRowId();
	
	public void setRowId(long rowId);
	
	public String getId();
	
	public void setId(String id);

	public long getModified();

	public void setModified(long modified);

	public String getModifiedBy();

	public void setModifiedBy(String modifiedBy);

	public String getModifiedFrom();

	public void setModifiedFrom(String modifiedFrom);

	public long getUploadDate();

	public void setUploadDate(long uploadDate);

	public long getCreated();

	public void setCreated(long created);

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public boolean getUnread();

	public void setUnread(boolean unread);

	public boolean getSynchronized();

	public void setSynchronized(boolean isSynchronized);
		
	public Uri commit(Context context, boolean local, boolean temporary);
}
