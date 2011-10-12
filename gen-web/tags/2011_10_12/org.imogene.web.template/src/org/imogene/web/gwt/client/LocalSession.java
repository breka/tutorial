package org.imogene.web.gwt.client;

import org.imogene.web.gwt.common.entity.ImogActor;

public class LocalSession {
	
	private static LocalSession instance = new LocalSession();
	
	private ImogActor currentUser;
	
	public static LocalSession get(){
		return instance;
	}
	
	public void setCurrentUser(ImogActor actor){
		currentUser = actor;
	}
	
	public ImogActor getCurrentUser(){
		return currentUser;
	}

}
