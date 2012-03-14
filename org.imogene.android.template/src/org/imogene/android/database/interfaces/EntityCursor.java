package org.imogene.android.database.interfaces;

import android.content.Context;
import android.database.Cursor;

public interface EntityCursor extends Cursor /*CrossProcessCursor*/{

	public String getId();

	public long getModified();
	
	public String getModifiedBy();

	public String getModifiedFrom();

	public long getUploadDate();

	public long getCreated();

	public String getCreatedBy();

	public boolean getUnread();

	public boolean getSynchronized();

	public String getMainDisplay(Context context);
	
	public String getSecondaryDisplay(Context context);
}
