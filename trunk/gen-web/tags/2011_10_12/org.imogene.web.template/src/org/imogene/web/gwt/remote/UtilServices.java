package org.imogene.web.gwt.remote;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Remote procedures declaration, which permits 
 * to use some useful application capabilities
 * @author Medes-IMPS
 */
public interface UtilServices extends RemoteService{
	
	/**
	 * Get unique id for new bean
	 * 
	 * @param type bean short name type
	 * @return the unique id
	 */
	public String getUniqueId(String type);
	
	/**
	 * Get the server session id.
	 * 
	 * @return the server session id.
	 */
	public String getSessionId();

	/**
	 * Get the meta data of a binary
	 * @param binaryId the binary id
	 * @return the formated binary meta-data
	 */
	public String getBinaryDesc(String binaryId);
}
