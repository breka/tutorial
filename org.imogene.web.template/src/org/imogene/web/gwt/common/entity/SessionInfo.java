package org.imogene.web.gwt.common.entity;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Seesion information about the current user
 * @author Medes-IMPS
 */
public class SessionInfo implements IsSerializable {

	private String sessionId;
	
	private ImogActor actor;

	public SessionInfo(){}
	
	public SessionInfo(String sessionId, ImogActor actor){
		this.sessionId = sessionId;
		this.actor =actor;
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ImogActor getActor() {
		return actor;
	}

	public void setActor(ImogActor actor) {
		this.actor = actor;
	}
}
