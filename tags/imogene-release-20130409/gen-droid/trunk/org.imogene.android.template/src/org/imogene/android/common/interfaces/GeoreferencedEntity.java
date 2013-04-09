package org.imogene.android.common.interfaces;

import android.location.Location;

public interface GeoreferencedEntity extends Entity {
	
	public Location getGeoreference();
	
	public void setGeoreference(Location location);

}
