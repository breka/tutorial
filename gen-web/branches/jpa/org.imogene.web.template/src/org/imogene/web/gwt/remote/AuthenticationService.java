package org.imogene.web.gwt.remote;

import org.imogene.common.entity.ImogActor;
import org.imogene.common.entity.SessionInfo;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Remote service that enables to authenticate an application user
 * 
 * @author Medes-IMPS
 */
public interface AuthenticationService extends RemoteService {

	/**
	 * Validates the login/password
	 * 
	 * @param login
	 *            user login
	 * @param passwd
	 *            user password
	 * @return the associated actor or null if authentication failure
	 */
	public ImogActor validateLogin(String login, String passwd);

	/**
	 * Disconnects the current user.
	 */
	public void disconnect();

	/**
	 * Validates the current session
	 * 
	 * @param sessionId
	 *            the session id
	 * @return the actor that is associated with the session
	 */
	public ImogActor validateSession(String sessionId);

	/**
	 * Gets the server session id.
	 * 
	 * @return the server session id.
	 */
	public String getSessionId();

	/**
	 * Gets the current user information from session
	 */
	public SessionInfo currentUser();

}
