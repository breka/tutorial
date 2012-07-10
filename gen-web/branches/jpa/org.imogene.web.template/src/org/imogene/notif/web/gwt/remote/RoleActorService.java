package org.imogene.notif.web.gwt.remote;

import java.util.List;

import org.imogene.common.entity.ImogActor;
import org.imogene.common.role.Role;

import com.google.gwt.user.client.rpc.RemoteService;

public interface RoleActorService extends RemoteService {

	public List<ImogActor> listActors();

	public ImogActor getActor(String id);

	public List<Role> listRoles();

	public Role getRole(String id);
}
