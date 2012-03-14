package org.imogene.sync.client;

import org.imogene.common.data.SynchronizableUser;

public class IdentityManager {

	private static IdentityManager instance = new IdentityManager();
		
	public static IdentityManager getInstance(){
		return instance;
	}
	
	public SynchronizableUser loggedUser(){
		return null;
	}
}
