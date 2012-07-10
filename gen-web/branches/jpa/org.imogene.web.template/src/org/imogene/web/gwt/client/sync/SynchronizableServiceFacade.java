package org.imogene.web.gwt.client.sync;

import java.util.List;

import org.imogene.common.sync.entity.SynchronizableEntity;
import org.imogene.web.gwt.client.Constants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class SynchronizableServiceFacade {

	private static SynchronizableServiceFacade instance;

	private SynchronizableServiceAsync proxy;

	/**
	 * Private constructor
	 */
	private SynchronizableServiceFacade() {
		proxy = (SynchronizableServiceAsync) GWT
				.create(SynchronizableService.class);

		ServiceDefTarget target = (ServiceDefTarget) proxy;
		GWT.getModuleBaseURL();
		target.setServiceEntryPoint(GWT.getModuleBaseURL()
				+ Constants.RPC_URL_BASE + "synchronizable.serv");
	}

	/**
	 * Instance accessor
	 */
	public static SynchronizableServiceFacade getInstance() {
		if (instance == null) {
			instance = new SynchronizableServiceFacade();
		}
		return instance;
	}

	/**
	 * 
	 * @param callback
	 */
	public void listAll(AsyncCallback<List<SynchronizableEntity>> callback) {
		proxy.listAll(callback);
	}
}
