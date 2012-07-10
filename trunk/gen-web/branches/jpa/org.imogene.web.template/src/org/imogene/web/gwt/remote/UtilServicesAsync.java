package org.imogene.web.gwt.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous declaration for <code>UtilServices</code>
 * 
 * @author Medes-IMPS
 */
public interface UtilServicesAsync {

	/**
	 * @see <code>UtilServices</code>
	 */
	public void getUniqueId(String type, AsyncCallback<String> callback);

	/**
	 * @see <code>UtilServices</code>
	 */
	public void getSessionId(AsyncCallback<String> callback);

	/**
	 * @see <code>UtilServices</code>
	 */
	public void getBinaryDesc(String binaryId, AsyncCallback<String> callback);
}
