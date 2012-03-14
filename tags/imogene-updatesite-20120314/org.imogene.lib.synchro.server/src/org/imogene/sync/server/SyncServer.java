package org.imogene.sync.server;

import java.io.InputStream;
import java.io.OutputStream;

import org.imogene.common.data.SynchronizableUser;
import org.imogene.sync.serializer.ImogSerializationException;

/**
 * This interface describes 
 * a synchronization server.
 * @author MEDES-IMPS
 */
public interface SyncServer {
	 	
	/**
	 * Initialize the session between 
	 * the client and the server
	 * @param termId the terminal id
	 * @return the session id
	 */
	public String initSession(String termId, String type, SynchronizableUser user);
		
	/**
	 * Apply modification sent by the client.
	 * @param sessionId the current synchronization session id.
	 * @param data Data to synchronize serialized stream.
	 * @return The number of documents added or -1 if an error occurred.
	 */
	public int applyClientModifications(String sessionId, InputStream data) throws ImogSerializationException;
	
	
	/**
	 * Retrieve the current modification 
	 * that have been to send to the client.
	 * @param sessionId The id of the synchronization session.
	 * @param out The output stream where to serialize the data
	 */
	public void getServerModifications(String sessionId, OutputStream out) throws ImogSerializationException;
	
	/**
	 * Close the synchronization session.
	 * @param sessionId the session to close id
	 * @param status the session status sent by the client
	 * @return A result code.
	 */
	public int closeSession(String sessionId, int status);
	
	/**
	 * check the validity of a session
	 * @param sessionId the session to check id
	 * @return True if it is a valid session, false otherwise
	 */
	public boolean checkSession(String sessionId);
}
