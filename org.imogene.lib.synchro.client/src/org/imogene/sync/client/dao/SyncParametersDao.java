package org.imogene.sync.client.dao;

import org.imogene.sync.client.SyncParameters;

/**
 * This interface describe the DAO that 
 * permits to access the application parameters.
 * @author medes @ medoo.fr 
 */
public interface SyncParametersDao {

	/**
	 * load the actual parameters
	 * @return the synchronization parameters
	 */
	public SyncParameters load();
	
	/**
	 * Store the new synchronization parameters
	 * @param parameters the new synchronization parameters
	 */
	public void store(SyncParameters parameters);
}
