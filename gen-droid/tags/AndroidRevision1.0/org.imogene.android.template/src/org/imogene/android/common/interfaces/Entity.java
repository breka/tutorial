package org.imogene.android.common.interfaces;

import android.content.Context;
import android.net.Uri;

public interface Entity {
	
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
