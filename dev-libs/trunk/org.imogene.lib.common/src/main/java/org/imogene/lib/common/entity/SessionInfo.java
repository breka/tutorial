package org.imogene.lib.common.entity;

import java.io.Serializable;

/**
 * Session information for the current user
 * @author Medes-IMPS
 */
public class SessionInfo implements Serializable {

	private static final long serialVersionUID = -453939375643784554L;

	private String sessionId;
	private ImogActorImpl actor;

	public SessionInfo() {
	}

	public SessionInfo(String sessionId, ImogActorImpl actor) {
		this.sessionId = sessionId;
		this.actor = actor;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ImogActorImpl getActor() {
		return actor;
	}

	public void setActor(ImogActorImpl actor) {
		this.actor = actor;
	}
}
