package org.imogene.web.gwt.remote;

import org.imogene.web.gwt.common.entity.ImogActor;
import org.imogene.web.gwt.common.entity.SessionInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Asynchronous declaration for <code>AuthentificationService</code>
 * @author Medes-IMPS
 */
public interface AuthenticationServiceAsync {
	
	/*
	 * See <code>AuthenticationService</code>
	 */
	public void validateLogin(String login, String passwd,
			AsyncCallback<ImogActor> callback);
	
	/*
	 * see <code>AuthenticationService</code> 
	 */
	public void disconnect(AsyncCallback<Void> callback);
	
	/*
	 * see <code>AuthenticationService</code> 
	 */	
	public void validateSession(String sessionId, AsyncCallback<ImogActor> callback);
	
	/*
	 * see <code>AuthenticationService</code> 
	 */	
	public void getSessionId(AsyncCallback<String> callback);
	
	/*
	 * see <code>AuthenticationService</code> 
	 */	
	public void currentUser(AsyncCallback<SessionInfo> callback);
}
