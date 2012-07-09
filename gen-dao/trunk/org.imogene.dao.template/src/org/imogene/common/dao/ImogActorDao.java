package org.imogene.common.dao;

import java.util.List;

import org.imogene.common.entity.ImogActor;

public interface ImogActorDao<T extends ImogActor> extends ImogBeanDao<T> {

	/**
	 * Get the associated actor from the login id.
	 * 
	 * @param login the actor login
	 * @return the actor if it exists or null
	 */
	public List<T> loadFromLogin(String login);

}
