package org.imogene.web.gwt.remote;

import org.imogene.common.entity.ImogActor;
import org.imogene.common.entity.SessionInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Facade classes that enables to call the remote procedures defined by
 * <code>AuthenticationService</code>
 * 
 * @author Medes-IMPS
 */
public class AuthenticationServiceAsyncFacade {
	private static AuthenticationServiceAsyncFacade instance;

	private AuthenticationServiceAsync proxy;

	/**
	 * Private constructor
	 */
	private AuthenticationServiceAsyncFacade() {
		proxy = (AuthenticationServiceAsync) GWT
				.create(AuthenticationService.class);

		ServiceDefTarget target = (ServiceDefTarget) proxy;
		target.setServiceEntryPoint(GWT.getHostPageBaseURL() + "/auth.serv");
	}

	/**
	 * Singleton access.
	 * 
	 * @return the instance
	 */
	public static AuthenticationServiceAsyncFacade getInstance() {
		if (instance == null) {
			instance = new AuthenticationServiceAsyncFacade();
		}
		return instance;
	}

	/**
	 * @see <code>AuthenticationServiceAsync</code>
	 */
	public void validateLogin(String login, String passwd,
			AsyncCallback<ImogActor> callback) {
		proxy.validateLogin(login, passwd, callback);
	}

	/**
	 * @see <code>AuthenticationServiceAsync</code>
	 */
	public void disconnect(AsyncCallback<Void> callback) {
		proxy.disconnect(callback);
	}

	/**
	 * @see <code>AuthenticationServiceAsync</code>
	 */
	public void validateSession(String sessionId,
			AsyncCallback<ImogActor> callback) {
		proxy.validateSession(sessionId, callback);
	}

	/**
	 * @see <code>AuthenticationServiceAsync</code>
	 */
	public void getSessionId(AsyncCallback<String> callback) {
		proxy.getSessionId(callback);
	}

	/**
	 * @see <code>AuthenticationServiceAsync</code>
	 */
	public void current(AsyncCallback<SessionInfo> callback) {
		proxy.currentUser(callback);
	}
}
