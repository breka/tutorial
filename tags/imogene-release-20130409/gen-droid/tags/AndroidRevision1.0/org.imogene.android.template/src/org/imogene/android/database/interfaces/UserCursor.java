package org.imogene.android.database.interfaces;

import android.content.Context;

public interface UserCursor extends EntityCursor {
	
	public String getLogin();
	
	public byte[] getPassword();
	
	public String getRoles();

	public String getUserDisplay(Context context);
}
