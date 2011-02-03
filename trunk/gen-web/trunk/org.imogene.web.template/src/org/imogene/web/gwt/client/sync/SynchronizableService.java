package org.imogene.web.gwt.client.sync;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SynchronizableService extends RemoteService {

	public List<SynchronizableEntity> listAll(); 
}
