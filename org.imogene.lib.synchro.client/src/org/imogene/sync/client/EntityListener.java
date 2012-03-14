package org.imogene.sync.client;

import org.imogene.common.data.Synchronizable;

public interface EntityListener {
	
	public void entitySavedOrUpdated(Synchronizable entity);
}
