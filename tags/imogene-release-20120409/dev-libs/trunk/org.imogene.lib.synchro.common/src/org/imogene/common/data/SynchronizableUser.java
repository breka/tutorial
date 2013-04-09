package org.imogene.common.data;

import java.util.Set;

import org.imogene.uao.role.Role;
import org.imogene.uao.synchronizable.SynchronizableEntity;


/**
 * This interface describes a SynchronizableUser, which is an Imogene bean that is an
 * application user too.
 * @author Medes-IMPS
 */
public interface SynchronizableUser extends Synchronizable {
	
	/** Get the assigned login for this user */
	public String getLogin();
	
	/** Set the assigned login for this user */
	public void setLogin(String login);

	/** Get the assigned password for this user */
	public String getPassword();

	/** Set the assigned password for this user */
	public void setPassword(String password);
	
	/**
	 * Get the roles that have been assigned to the user
	 * @return list of roles
	 */
	public Set<Role> getRoles();
	
	/**
	 * Assign a list of roles to the user
	 * @param roles a list of roles
	 */
	public void setRoles(Set<Role> roles);
	
	/**
	 * Add a role to the user assigned list of roles
	 * @param role a role
	 */
	public void addRole(Role role);
	
	/**
	 * Get the assigned entity types that the user can synchronize
	 * @return list of entity types
	 */
	public Set<SynchronizableEntity> getSynchronizables();
	
	/**
	 * Assign a list of entity types that the user can synchronize
	 * @param synchronizables list of entity types
	 */
	public void setSynchronizables(Set<SynchronizableEntity> synchronizables);
	
	/**
	 * Add an entity type to the list of entity types that the user can
	 * synchronize
	 * @param synchronizable an entity type
	 */
	public void addSynchronizable(SynchronizableEntity synchronizable);

}
