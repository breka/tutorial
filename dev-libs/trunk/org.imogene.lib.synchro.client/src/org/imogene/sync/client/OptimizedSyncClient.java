package org.imogene.sync.client;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface that describes a medoo 
 * synchronization server client
 * @author MEDES-IMPS
 */
public interface OptimizedSyncClient {		
	
	/* misc */
	public static String ERROR_PREFIX = "-error-";
	
	/* parameters constants */
	public static String LOGIN_PARAM="login";
	
	public static String PASSWD_PARAM="password";
	
	public static String TERMINALID_PARAM="terminal";
	
	public static String TYPE_PARAM="type";
	
	public static String CMD_PARAM="cmd";
	
	public static String LENGTH_PARAM="len";
	
	public static String SESSION_PARAM="session";	
	
	/* command constants */
	
	public static String CMD_INIT="init";
	
	public static String CMD_RESUME_SEND_INIT="initresumesend";
	
	public static String CMD_RESUME_RECEIVE_INIT="initresumereceive";
	
	public static String CMD_RESUME_SEND="resumesend";
	
	public static String CMD_RESUME_RECEIVE="resumereceive";
	
	public static String CMD_CLOSE="ackservmodif";
	
	public static String CMD_SENDMODIF="clmodif";
	
	public static String CMD_SERVERMODIF="reqservmodif";
	
	public static String CDM_DIRECTSEND="directsend";
	
	/**
	 * Initialize the session
	 * @param login the user login
	 * @param passwd the user password
	 * @param terminalId the terminal id
	 * @param type the synchronization type
	 * @return the session id
	 */
	public String initSession(String login, String passwd, String terminalId, String type) throws SynchronizationException;
	
	/**
	 * Resume a session that terminated with anomaly
	 * @param login the user login
	 * @param passwd the user password
	 * @param terminalId the terminal id
	 * @param type the synchronization type
	 * @param sessionId the session id
	 * @return nb of bytes already received or Error
	 */
	public String resumeSend(String login, String passwd, String terminalId, String type, String sessionId) throws SynchronizationException;
	
	/**
	 * Resume a session that terminated with anomaly
	 * @param login the user login
	 * @param passwd the user password
	 * @param terminalId the terminal id
	 * @param type the synchronization type
	 * @param sessionId the session id
	 * @param bytesReceived number of bytes already received
	 * @return Acknowledge or Error
	 */
	public String resumeReceive(String login, String passwd, String terminalId, String type, String sessionId, long bytesReceived) throws SynchronizationException;
	
	/**
	 * Resume the sent of the client modifications to the server
	 * @param sessionId the session id;
	 * @param data the modified entity, serialized
	 * @return the number of entity processed by the server
	 */
	public int resumeRequestModification(String sessionId, OutputStream out, long bytesReceived) throws SynchronizationException;
	
	/**
	 * Send the client modifications to the server
	 * @param sessionId the session id;
	 * @param data the modified entity, serialized
	 * @return the number of entity processed by the server
	 */
	public int sendClientModification(String sessionId, InputStream in) throws SynchronizationException;
	
	/**
	 * Resume the sent of the client modifications to the server
	 * @param sessionId the session id;
	 * @param data the modified entity, serialized
	 * @return the number of entity processed by the server
	 */
	public int resumeSendModification(String sessionId, InputStream in) throws SynchronizationException;
	
	/**
	 * Get server modifications 
	 * @param sessionId the session id
	 * @return the stream received from the server.
	 */
	public void requestServerModifications(String sessionId, OutputStream out) throws SynchronizationException;
	
	/**
	 * Close an opened session
	 * @param sessionId the session id
	 * @return true if the command success
	 */
	public boolean closeSession(String sessionId) throws SynchronizationException;
	
	/**
	 * Method that permits to send data 
	 * outside a global synchronization process.
	 * @param sessionId the sessionId;
	 * @param data the data to send
	 * @return number of modifications applied
	 */
	public int directSend(String sessionId, InputStream out) throws Exception;

}
