package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.web.gwt.common.entity.ImogActor;
import org.imogene.web.gwt.common.entity.ImogRole;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface RoleActorServiceAsync {

	public void listActors(AsyncCallback<List<ImogActor>> callback);
	
	public void getActor(String id, AsyncCallback<ImogActor> callback);
	
	public void listRoles(AsyncCallback<List<ImogRole>> callback);
	
	public void getRole(String id, AsyncCallback<ImogRole> callback);
}
