package org.imogene.ws.dao;

import org.imogene.ws.binary.Binary;


/**
 * Manage persistence for Binary
 * @author MEDES-IMPS
 */
public interface BinaryDao {
	
    /**
     * Persists Binary instance.
     * @param bean to persist
     * @param isNew true is it is a new instance
     */
    //public void saveOrUpdateBinary(Binary bean);
    

	/**
	 * Gets Binary instance
	 * @param id instance id of the Binary to load
	 * @return a Binary instance
	 */
    public Binary getBinary(String id);
    
	/**
	 * Deletes a Binary instance
	 * @param entity the Binary instance to delete
	 */
   public void deleteBinary(Binary entity);
}
