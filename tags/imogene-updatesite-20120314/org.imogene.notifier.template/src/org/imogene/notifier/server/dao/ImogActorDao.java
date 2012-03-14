package org.imogene.notifier.server.dao;

import java.util.Set;

import org.imogene.common.entity.ImogActor;
import org.imogene.common.entity.ImogRole;


/**
 * Interface to access to the ImogActor
 * @author Medes-IMPS
 */
public interface ImogActorDao {

	/**
	 * Get the actor matching the id
	 * @param actorId the actorId
	 * @return the actor or null if doesn't exist
	 */
	public ImogActor getActorFormId(String actorId);
	
	/**
	 * Get the actor matching the login 
	 * @param actorLogin the actor login
	 * @return the actor or null if doesn't exist 
	 */
	public ImogActor getActorFromLogin(String actorLogin);
	
	
	/**
	 * Get the actors with the specified role
	 * @param role the role
	 * @return all actors with this role
	 */
	public Set<ImogActor> getActorsForRole(ImogRole role);
	
}
