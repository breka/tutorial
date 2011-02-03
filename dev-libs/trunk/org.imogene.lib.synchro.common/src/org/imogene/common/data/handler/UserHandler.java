package org.imogene.common.data.handler;

import java.util.List;

import org.imogene.common.data.SynchronizableUser;


/**
 * Interface that describes a User manager 
 * that enables to manage user access
 * @author MEDES-IMPS
 */
public interface UserHandler extends EntityHandler {

	public List<SynchronizableUser> getUserFromLogin(String login);
	
}
