package org.imogene.web.gwt.common.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Describe an actor role.
 * @author Medes-IMPS
 */
public interface ImogRole extends IsSerializable {

	/**
	 * Set the role id.
	 * @param id the role id
	 */
	public void setId(String id);
	
	/**
	 * Get the role id
	 * @return the role id.
	 */
	public String getId();
	
	/**
	 * Set the name of the role
	 * @param roleName the role name
	 */
	public void setName(String roleName);
	
	/**
	 * Get the role name
	 * @return the name of the role
	 */
	public String getName();

}
