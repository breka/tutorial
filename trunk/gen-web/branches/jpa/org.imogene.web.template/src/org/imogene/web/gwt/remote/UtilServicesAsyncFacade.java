package org.imogene.web.gwt.remote;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Facade classes which permits to call remote procedures defined by
 * <code>UtilServices</code>
 * 
 * @author Medes-IMPS
 */
public class UtilServicesAsyncFacade {

	private static UtilServicesAsyncFacade instance;

	private UtilServicesAsync proxy;

	private UtilServicesAsyncFacade() {
		proxy = (UtilServicesAsync) GWT.create(UtilServices.class);
		ServiceDefTarget utilServiceTarget = (ServiceDefTarget) proxy;
		utilServiceTarget.setServiceEntryPoint(GWT.getHostPageBaseURL()
				+ "/utilServices.serv");
	}

	/**
	 * @see <code>UtilsServicesAsync</code>
	 */
	public static UtilServicesAsyncFacade getInstance() {
		if (instance == null) {
			instance = new UtilServicesAsyncFacade();
		}
		return instance;
	}

	/**
	 * @see <code>UtilsServicesAsync</code>
	 */
	public void getUniqueId(String type, AsyncCallback<String> callback) {
		proxy.getUniqueId(type, callback);
	}

	/**
	 * @see <code>UtilsServicesAsync</code>
	 */
	public void getSessionId(AsyncCallback<String> callback) {
		proxy.getSessionId(callback);
	}

	/**
	 * @see <code>UtilsServiceAsync</code>
	 */
	public void getBinaryDesc(String binaryId, AsyncCallback<String> callback) {
		proxy.getBinaryDesc(binaryId, callback);
	}
}
