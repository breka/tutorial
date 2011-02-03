package org.imogene.uao.security;

import java.util.List;

import org.imogene.common.data.Synchronizable;
import org.imogene.common.data.SynchronizableUser;


public interface ImogSecurityPolicy {

	/**
	 * 
	 * @param bean The bean to secure
	 * @param actor The actor of this session
	 * @return a secured bean
	 */
	public Synchronizable toSecure(Synchronizable bean, SynchronizableUser actor);
	
	/**
	 * 
	 * @param list
	 * @param actor
	 * @return
	 */
	public List<Synchronizable> toSecure(List<Synchronizable> list, SynchronizableUser actor);
	
	/**
	 * 
	 * @param bean The secured bean
	 * @param actor The session actor
	 * @return The global 'unsecured' bean
	 */
	public Synchronizable toHibernate(Synchronizable bean, SynchronizableUser actor);
	
	/**
	 * 
	 * @param list
	 * @param actor
	 * @return
	 */
	public List<Synchronizable> toHibernate(List<Synchronizable> list, SynchronizableUser actor);
}
