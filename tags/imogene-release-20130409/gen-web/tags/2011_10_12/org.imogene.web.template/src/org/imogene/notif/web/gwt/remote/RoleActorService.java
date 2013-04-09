package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.web.gwt.common.entity.ImogActor;
import org.imogene.web.gwt.common.entity.ImogRole;

import com.google.gwt.user.client.rpc.RemoteService;


public interface RoleActorService extends RemoteService {

	public List<ImogActor> listActors();
	
	public ImogActor getActor(String id);
	
	public List<ImogRole> listRoles();
	
	public ImogRole getRole(String id);
}
