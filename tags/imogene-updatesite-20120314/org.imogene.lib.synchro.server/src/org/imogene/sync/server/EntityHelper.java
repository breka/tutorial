package org.imogene.sync.server;

import java.util.List;

import org.imogene.common.data.Synchronizable;

public interface EntityHelper {

	/**
	 * Get the entity ids associated to the specified entity.
	 * Works only for the association 1<->N and 1<->*
	 * @param entity the parent entity 
	 * @return list of associated entities
	 */
	public List<Synchronizable> getAssociatedEntitiesIds(Synchronizable entity);
}
