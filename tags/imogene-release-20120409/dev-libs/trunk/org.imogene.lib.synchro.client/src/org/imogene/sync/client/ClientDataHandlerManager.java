package org.imogene.sync.client;

import org.imogene.common.data.handler.DataHandlerManager;
import org.imogene.common.data.handler.EntityHandler;

public class ClientDataHandlerManager extends DataHandlerManager {

	public void add(String className, EntityHandler handler){
		((ClientEntityDao)handler.getDao()).setHandlerManager(this);
		handlers.put(className, handler);
	}
}
