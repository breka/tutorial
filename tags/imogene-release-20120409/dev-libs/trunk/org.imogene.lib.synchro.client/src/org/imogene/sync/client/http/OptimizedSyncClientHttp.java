package org.imogene.sync.client.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;
import org.imogene.sync.client.OptimizedSyncClient;
import org.imogene.sync.client.SynchronizationException;
import org.imogene.sync.client.base64.Base64;


public class OptimizedSyncClientHttp implements OptimizedSyncClient {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.client.http.SyncClientHttp");
	
	private String url="http://localhost:8080/ImogSynchro/";	
	
	private static final int maxAttempts=100;
	
	private boolean httpAuthentication = false;
	
	private String httpLogin;
	
	private String httpPassword;
	
	public OptimizedSyncClientHttp(String definedUrl){
		url = definedUrl;
		if(url.endsWith("/"))
			url = url + "sync.html";
		else if (url.endsWith("html"))
			;
		else
			url = url + "/sync.html";
	}
	
	/**
	 * By using this constructor, you specify that you want to use the HTTP authentication
	 * based on the specified login and password.
	 * This authentication will be set in all requests.
	 * 
	 * @param url the synchronization server 
	 * @param httpLogin the user login
	 * @param httpPassword the user password
	 */
	public OptimizedSyncClientHttp(String definedUrl, String httpLogin, String httpPassword){
		url = definedUrl;
		if(url.endsWith("/"))
			url = url + "sync.html";
		else if (url.endsWith("html"))
			;
		else
			url = url + "/sync.html";
		this.httpAuthentication = true;
		this.httpLogin = httpLogin;
		this.httpPassword = httpPassword;
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#closeSession(java.lang.String)
	 */
	public boolean closeSession(String sessionId) throws SynchronizationException {	
		try {
			/* request construction */
			NameValuePair cmdParam = new NameValuePair(CMD_PARAM, CMD_CLOSE);
			NameValuePair sessionParam = new NameValuePair(SESSION_PARAM,
					sessionId);
			NameValuePair debugParam = new NameValuePair("debug", "true");
			NameValuePair[] params = new NameValuePair[] { cmdParam,
					sessionParam, debugParam };
			GetMethod method = httpGetMethod(url);
			method.setQueryString(params);

			/* request execution */
			HttpClient client = new HttpClient();
			client.executeMethod(method);
			if (method.getResponseBodyAsString().startsWith("ACK"))
				return true;
		} catch (Exception ex) {
			throw new SynchronizationException("Closing session -> ", ex,
					SynchronizationException.ERROR_CLOSING);
		}
		return false;			
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#initSession(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String initSession(String login, String passwd, String terminalId, String type) throws SynchronizationException {
		try {
			/* request construction */
			NameValuePair cmdParam = new NameValuePair(CMD_PARAM, CMD_INIT);
			NameValuePair loginParam = new NameValuePair(LOGIN_PARAM, login);
			NameValuePair passwordParam = new NameValuePair(PASSWD_PARAM, passwd);
			NameValuePair terminalParam = new NameValuePair(TERMINALID_PARAM, terminalId);
			NameValuePair typeParam = new NameValuePair(TYPE_PARAM, type);
			NameValuePair[] params = new NameValuePair[] { cmdParam, loginParam, passwordParam, terminalParam, typeParam };
			GetMethod method = httpGetMethod(url);
			method.setQueryString(params);

			HttpClient client = new HttpClient();
			int code = client.executeMethod(method);
			if (code == HttpStatus.SC_OK) {
				String sessionId = method.getResponseBodyAsString();
				return sessionId;
			} else {
				throw new SynchronizationException("HTTP error code return : " + code, SynchronizationException.ERROR_INIT);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SynchronizationException("Session init -> ", ex, SynchronizationException.ERROR_INIT);
		}		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#resumeReceive(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public String resumeReceive(String login, String passwd, String terminalId,
			String type, String sessionId, long bytesReceived) throws SynchronizationException {	
		try {
			/* request construction */
			NameValuePair cmdParam = new NameValuePair(CMD_PARAM, CMD_RESUME_RECEIVE_INIT);
			NameValuePair loginParam = new NameValuePair(LOGIN_PARAM, login);
			NameValuePair passwordParam = new NameValuePair(PASSWD_PARAM, passwd);
			NameValuePair terminalParam = new NameValuePair(TERMINALID_PARAM, terminalId);
			NameValuePair typeParam = new NameValuePair(TYPE_PARAM, type);
			NameValuePair sessionParam = new NameValuePair(SESSION_PARAM, sessionId);
			NameValuePair lengthParam = new NameValuePair(LENGTH_PARAM, String.valueOf(bytesReceived));
			NameValuePair[] params = new NameValuePair[] { cmdParam, loginParam, passwordParam, terminalParam, typeParam, sessionParam,
					lengthParam };
			GetMethod method = httpGetMethod(url);
			method.setQueryString(params);

			HttpClient client = new HttpClient();
			int code = client.executeMethod(method);
			if (code == HttpStatus.SC_OK) {
				return method.getResponseBodyAsString();
			} else {
				throw new SynchronizationException("HTTP error code returned." + code, SynchronizationException.ERROR_RECEIVE);
			}
		} catch (Exception ex) {
			throw new SynchronizationException("Resume 'receive' session init -> ", ex, SynchronizationException.ERROR_RECEIVE);
		}				
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#resumeRequestModification(java.lang.String, java.io.InputStream, long)
	 */
	public int resumeRequestModification(String sessionId, OutputStream out,
			long bytesReceived) throws SynchronizationException {
		try {
			/* request construction */
			NameValuePair sessionParam = new NameValuePair(SESSION_PARAM, sessionId);
			NameValuePair cmdParam = new NameValuePair(CMD_PARAM, CMD_RESUME_RECEIVE);
			NameValuePair lengthParam = new NameValuePair(LENGTH_PARAM, String.valueOf(bytesReceived));
			NameValuePair[] params = new NameValuePair[] { sessionParam, cmdParam, lengthParam };
			GetMethod method = httpGetMethod(url);
			method.setQueryString(params);
			HttpClient client = new HttpClient();

			/* request execution */
			int code = client.executeMethod(method);
			if (code == HttpStatus.SC_OK) {
				/* read data sent by the server */
				long expectedLength = method.getResponseContentLength();
				InputStream is = method.getResponseBodyAsStream();
				writeInFile(is, out, expectedLength);
				return 0;
			} else {
				throw new SynchronizationException("Resume 'request' command ->HTTP code returned.", SynchronizationException.ERROR_RECEIVE);
			}
		} catch (Exception ex) {
			throw new SynchronizationException("Resume 'request' command ->HTTP code returned.", ex, SynchronizationException.ERROR_RECEIVE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#resumeSend(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public String resumeSend(String login, String passwd, String terminalId,
			String type, String sessionId) throws SynchronizationException {
		try {
			/* request construction */
			NameValuePair cmdParam = new NameValuePair(CMD_PARAM, CMD_RESUME_SEND_INIT);
			NameValuePair loginParam = new NameValuePair(LOGIN_PARAM, login);
			NameValuePair passwordParam = new NameValuePair(PASSWD_PARAM, passwd);
			NameValuePair terminalParam = new NameValuePair(TERMINALID_PARAM, terminalId);
			NameValuePair typeParam = new NameValuePair(TYPE_PARAM, type);
			NameValuePair sessionParam = new NameValuePair(SESSION_PARAM, sessionId);
			NameValuePair[] params = new NameValuePair[] { cmdParam, loginParam, passwordParam, terminalParam, typeParam, sessionParam };
			GetMethod method = httpGetMethod(url);
			method.setQueryString(params);

			HttpClient client = new HttpClient();
			int code = client.executeMethod(method);
			if (code == HttpStatus.SC_OK) {
				return method.getResponseBodyAsString();
			} else {
				throw new SynchronizationException("HTTP error code returned :"+code,
						SynchronizationException.ERROR_SEND);
			}
		} catch (Exception ex) {
			throw new SynchronizationException("Resume 'send' session init -> ", ex, SynchronizationException.ERROR_SEND);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#resumeSendModification(java.lang.String, java.io.InputStream)
	 */
	public int resumeSendModification(String sessionId, InputStream in)
			throws SynchronizationException {
		return sendData(sessionId, CMD_RESUME_SEND, in);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#requestServerModifications(java.lang.String, java.io.OutputStream)
	 */
	public void requestServerModifications(String sessionId, OutputStream out) throws SynchronizationException {
		try{
		/* request construction */
		NameValuePair sessionParam = new NameValuePair(SESSION_PARAM, sessionId);
		NameValuePair cmdParam = new NameValuePair(CMD_PARAM, CMD_SERVERMODIF);
		NameValuePair[] params = new NameValuePair[] { sessionParam, cmdParam };
		GetMethod method = httpGetMethod(url);
		method.setQueryString(params);
		HttpClient client = new HttpClient();
		
		/* request execution */
		int code = client.executeMethod(method);
		if (code == HttpStatus.SC_OK) {
			/* read data sent by the server */
			long expectedLength = method.getResponseContentLength();
			//System.out.println("Lenght of the result :"+expectedLength);
			InputStream is = method.getResponseBodyAsStream();				
			writeInFile(is,	out, expectedLength);											
		}else{
			throw new SynchronizationException("Command 'receive' : HTTP error code returned.");
		}
		}catch(Exception ex){
			throw new SynchronizationException("Command 'receive' -> ", ex, SynchronizationException.ERROR_RECEIVE);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#sendClientModification(java.lang.String, java.io.InputStream)
	 */
	public int sendClientModification(String sessionId, InputStream data) throws SynchronizationException{				
				
		return sendData(sessionId, CMD_SENDMODIF, data);
	}	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see org.imogene.sync.client.OptimizedSyncClient#directSend(java.lang.String, java.io.InputStream)
	 */
	public int directSend(String sessionId, InputStream data) throws Exception{
		return sendData(sessionId, CDM_DIRECTSEND, data);
	}
	
	/**
	 * Write the request result data incoming in the 
	 * input stream into the output stream
	 * @param is the request result input stream
	 * @param out the outputStream
	 * @param expectedLength the data length expected
	 * @return 0 if done with success, -1 otherwise 
	 */
	private int writeInFile(InputStream is, OutputStream out, long expectedLength) throws IOException {
		boolean finished = false;
		long bytesRead=0;
		int nbAttemps = 0;
		
		while (!finished) {
			/* while there is data to read, we read it */
			byte[] buffer;			
			/*while (is.available() > 0) {
				if (is.available() < 1024)
					buffer = new byte[is.available()];
				else
					buffer = new byte[1024];

				bytesRead = bytesRead + is.read(buffer);
				out.write(buffer);
				nbAttemps=0;
			}*/
			buffer = new byte[1024];
			int i=0;
			while((i = is.read(buffer))>0){
				bytesRead = bytesRead+i;
				out.write(buffer, 0, i);
				nbAttemps=0;
			}
			/* is the reading process finished ? */
			finished = ((bytesRead >= expectedLength && expectedLength!=-1) || ++nbAttemps > maxAttempts);
			
			/* if not finish we wait and we retry */
			if(!finished){
				try{
					Thread.sleep(500);
				}catch(InterruptedException ex){
					logger.error(ex.getMessage());
				}
			}
			else{			
				out.flush();
				out.close();
				is.close();
				if(bytesRead>=expectedLength)
					return 0;
				else 
					return -1;
			}
		}
		/* return 0, if read all data with success, -1 otherwise */
		if(bytesRead>=expectedLength)
			return 0;
		else 
			return -1;		
	}
	
	/**
	 * Send data to the server
	 * @param sessionId the synchronization session id
	 * @param cmd the command
	 * @param data the data to send
	 * @return 0 if done with success, -1 otherwise
	 */
	private int sendData(String sessionId, String cmd, InputStream data) throws SynchronizationException {
		try {
			/* request construction */
			PostMethod method = httpPostMethod(url);
			StringPart sessionParam = new StringPart(SESSION_PARAM, sessionId);
			StringPart cmdParam = new StringPart(CMD_PARAM, cmd);
			SyncPartSource dataSource = new SyncPartSource(data, sessionId + ".cmodif", data.available());
			FilePart dataFile = new FilePart("data", dataSource);
			Part[] parts = { sessionParam, cmdParam, dataFile };
			logger.debug("send data to url : "+url+" cmd : "+cmd);
			/* execute the request */
			MultipartRequestEntity mEntity = new MultipartRequestEntity(parts, method.getParams());
			method.setRequestEntity(mEntity);
			HttpClient client = new HttpClient();
			int code = client.executeMethod(method);
			if (code == HttpStatus.SC_OK) {
				String body = new String(method.getResponseBody());
				if (body.startsWith("ACK")) {
					return 0;
				} else {
					throw new SynchronizationException("Command 'send' : Server error code returned.");
				}
			}else{
				throw new Exception("HTTP error code returned : "+code);
			}
		} catch (Exception ex) {
			throw new SynchronizationException("Command 'send' -> ", ex, SynchronizationException.ERROR_SEND);
		}
	}
	
	/** */
	private PostMethod httpPostMethod(String url){
		PostMethod post = new PostMethod(url);
		if(httpAuthentication)
			setBasicAuthentication(post);
		return post;
	}
	
	/** */
	private GetMethod httpGetMethod(String url){
		GetMethod get = new GetMethod(url);
		if(httpAuthentication)
			setBasicAuthentication(get);
		return get;
	}
	
	/** */
	private void setBasicAuthentication(HttpMethodBase method){
		String strAuth =  httpLogin+":"+httpPassword;
		String encoding = new String(Base64.encodeBase64(strAuth.getBytes()));
		method.setRequestHeader("Authorization", "Basic " + encoding);
	}
	
}
