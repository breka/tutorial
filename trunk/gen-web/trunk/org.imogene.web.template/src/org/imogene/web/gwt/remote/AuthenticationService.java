package org.imogene.web.gwt.remote;

import org.imogene.web.gwt.common.entity.ImogActor;

import com.google.gwt.user.client.rpc.RemoteService;


/**
 * Remote service that permits to authenticate an application user
 */
public interface AuthenticationService extends RemoteService {

	/**
	 * Validates the login/password
	 * @param login user login
	 * @param passwd user password
	 * @return the associated actor or null if authentication failure
	 */
	public ImogActor validateLogin(String login, String passwd);
	
	/**
	 * Disconnects the current user.
	 */
	public void disconnect();
	
	/**
	 * Validates the current session
	 * @param sessionId the session id
	 * @return the actor that is associated with the session
	 */
	public ImogActor validateSession(String sessionId);
	
	/**
	 * Get the server session id.
	 * @return the server session id.
	 */
	public String getSessionId();

}
