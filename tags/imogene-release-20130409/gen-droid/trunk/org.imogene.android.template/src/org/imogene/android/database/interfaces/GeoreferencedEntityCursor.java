package org.imogene.android.database.interfaces;

import android.location.Location;

public interface GeoreferencedEntityCursor extends EntityCursor {
	
	public Location getGeoreference();

}
