package org.imogene.android.sync.http;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.imogene.android.sync.OptimizedSyncClient;
import org.imogene.android.sync.SynchronizationException;
import org.imogene.android.util.file.FileUtils;
import org.imogene.android.util.http.multipart.FileInputStreamPart;
import org.imogene.android.util.http.multipart.MultipartEntity;
import org.imogene.android.util.http.multipart.Part;
import org.imogene.android.util.http.ssl.SSLHttpClient;

import android.util.Log;

public class OptimizedSyncClientHttp implements OptimizedSyncClient{
	
	private static final String TAG = OptimizedSyncClientHttp.class.getName();
    
	private String url=null;
	
	public OptimizedSyncClientHttp(String url){
		this.url = url + "sync.html";
	}
	
	public String authentication(String login, String passwd, String terminalId) throws SynchronizationException {
		HttpClient client = new SSLHttpClient();

		if (url == null) {
			return null;
		}
		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+CMD_PARAM+"="+CMD_AUTH)
		.append("&"+LOGIN_PARAM+"="+login)
		.append("&"+PASSWD_PARAM+"="+passwd)
		.append("&"+TERMINALID_PARAM+"="+terminalId);
		
		HttpGet get = new HttpGet(builder.toString());
		
		try {
			/* request execution */
			HttpResponse response = client.execute(get);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int i;
					while ((i = is.read()) != -1) {
						sb.append((char) i);
					}
					return sb.toString();
				} else {
					throw new SynchronizationException("HTTP error code return : " + code, SynchronizationException.ERROR_AUTH);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_AUTH);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Session auth -> ", e, SynchronizationException.ERROR_AUTH);
		}
	}

	public boolean closeSession(UUID sessionId, boolean debug) throws SynchronizationException {
		HttpClient client = new SSLHttpClient();
		
		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+CMD_PARAM+"="+CMD_CLOSE)
		.append("&"+SESSION_PARAM+"="+sessionId)
		.append("&"+DEBUG_PARAM+"="+Boolean.toString(debug));
		
		HttpGet method = new HttpGet(builder.toString());
		
		try {
			/* request execution */
			HttpResponse response = client.execute(method);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				InputStream is = response.getEntity().getContent();
				StringBuffer sb = new StringBuffer();
				int i;
				while ((i = is.read())!=-1) {
					sb.append((char) i);
				}
				if (sb.toString().startsWith("ACK")) {
					return true;
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_CLOSING);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Closing session -> ", e, SynchronizationException.ERROR_CLOSING);
		}
		return false;
	}

	public String initSession(String login, String passwd, String terminalId, String type) throws SynchronizationException {
		HttpClient client = new SSLHttpClient();

		if (url == null) {
			return null;
		}
		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+CMD_PARAM+"="+CMD_INIT)
		.append("&"+LOGIN_PARAM+"="+login)
		.append("&"+PASSWD_PARAM+"="+passwd)
		.append("&"+TERMINALID_PARAM+"="+terminalId)
		.append("&"+TYPE_PARAM+"="+type);
		
		HttpGet get = new HttpGet(builder.toString());
		
		try {
			/* request execution */
			HttpResponse response = client.execute(get);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int i;
					while ((i = is.read()) != -1) {
						sb.append((char) i);
					}
					return sb.toString();
				} else {
					throw new SynchronizationException("HTTP error code return : " + code, SynchronizationException.ERROR_INIT);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_INIT);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Session init -> ", e, SynchronizationException.ERROR_INIT);
		}
	}
	
	/**
	 * Send data to the server
	 * @param sessionId the synchronization session id
	 * @param cmd the command
	 * @param fis the data to send
	 * @return 0 if done with success, -1 otherwise
	 * @throws SynchronizationException 
	 */
	private int sendData(UUID sessionId, String cmd, FileInputStream fis) throws SynchronizationException{	
		try {
			HttpClient client = new SSLHttpClient();
			
			StringBuilder builder = new StringBuilder(url)
			.append("?"+SESSION_PARAM+"="+sessionId)
			.append("&"+CMD_PARAM+"="+cmd);
			Log.i(TAG, builder.toString());
			
			HttpPost method = new HttpPost(builder.toString());
			
			String fileName = sessionId + ".cmodif";
			FileInputStreamPart part = new FileInputStreamPart("data", fileName, fis);
			MultipartEntity requestContent = new MultipartEntity(new Part[] {part});
			
			method.setEntity(requestContent);
			
			HttpResponse response = client.execute(method);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if(code == HttpStatus.SC_OK){
					Log.i(TAG, "Status OK");
					InputStream is = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int i;
					while ((i = is.read()) != -1) {
						sb.append((char) i);
					}
					Log.i(TAG, sb.toString());
					if(sb.toString().startsWith("ACK")){
						String result = sb.toString().split("_")[1];
						return result!=null?result.length()!=0?Integer.parseInt(result):0:0;
					} else {
						throw new SynchronizationException("Command 'send' : Server error code returned.");
					}
				} else {
					throw new Exception("HTTP error code returned : "+code);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_SEND);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Command 'send' -> ", e, SynchronizationException.ERROR_SEND);
		}
	}

	public String resumeReceive(String login, String passwd, String terminalId,
			String type, UUID sessionId, long bytesReceived) throws SynchronizationException {

		HttpClient client = new SSLHttpClient();

		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+CMD_PARAM+"="+CMD_RESUME_RECEIVE_INIT)
		.append("&"+LOGIN_PARAM+"="+login)
		.append("&"+PASSWD_PARAM+"="+passwd)
		.append("&"+TERMINALID_PARAM+"="+terminalId)
		.append("&"+TYPE_PARAM+"="+type)
		.append("&"+SESSION_PARAM+"="+sessionId)
		.append("&"+LENGTH_PARAM+"="+String.valueOf(bytesReceived));
		
		HttpGet get = new HttpGet(builder.toString());
		
		try {
			/* request execution */
			HttpResponse response = client.execute(get);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int i;
					while ((i = is.read()) != -1) {
						sb.append((char) i);
					}
					return sb.toString();
				} else {
					throw new SynchronizationException("HTTP error code returned." + code, SynchronizationException.ERROR_RECEIVE);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_RECEIVE);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Resume 'receive' session init -> ", e, SynchronizationException.ERROR_RECEIVE);
		}
	}

	public int resumeRequestModification(UUID sessionId, OutputStream out, long bytesReceived) throws SynchronizationException {
		
		HttpClient client = new SSLHttpClient();

		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+SESSION_PARAM+"="+sessionId)
		.append("&"+CMD_PARAM+"="+CMD_RESUME_RECEIVE)
		.append("&"+LENGTH_PARAM+"="+String.valueOf(bytesReceived));
	
		HttpGet method = new HttpGet(builder.toString());
		try {
			/* request execution */
			HttpResponse response = client.execute(method);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					long expectedLength = response.getEntity().getContentLength();				
					InputStream is = response.getEntity().getContent();
					FileUtils.writeInFile(is, out, expectedLength);
					return 0;
				} else {
					throw new SynchronizationException("Resume 'request' command ->HTTP code returned. " + code, SynchronizationException.ERROR_RECEIVE);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_RECEIVE);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Resume 'request' command ->HTTP code returned.", e, SynchronizationException.ERROR_RECEIVE);
		}
	}

	public String resumeSend(String login, String passwd, String terminalId,
			String type, UUID sessionId) throws SynchronizationException {
		HttpClient client = new SSLHttpClient();

		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+CMD_PARAM+"="+CMD_RESUME_SEND_INIT)
		.append("&"+LOGIN_PARAM+"="+login)
		.append("&"+PASSWD_PARAM+"="+passwd)
		.append("&"+TERMINALID_PARAM+"="+terminalId)
		.append("&"+TYPE_PARAM+"="+type)
		.append("&"+SESSION_PARAM+"="+sessionId);

		HttpGet method = new HttpGet(builder.toString());

		try {
			/* request execution */
			HttpResponse response = client.execute(method);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					InputStream is = response.getEntity().getContent();
					StringBuffer sb = new StringBuffer();
					int i;
					while ((i = is.read()) != -1) {
						sb.append((char) i);
					}
					return sb.toString();
				} else {
					throw new SynchronizationException("HTTP error code returned :"+code, SynchronizationException.ERROR_SEND);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_SEND);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Resume 'send' session init -> ", e, SynchronizationException.ERROR_SEND);
		}
	}

	public int resumeSendModification(UUID sessionId, FileInputStream fis) throws SynchronizationException {
		return sendData(sessionId, CMD_RESUME_SEND, fis);
	}

	public int sendClientModification(UUID sessionId, FileInputStream fis) throws SynchronizationException {
		return sendData(sessionId, CMD_SENDMODIF, fis);
	}
	
	public boolean requestServerModifications(UUID sessionId, OutputStream out) throws SynchronizationException {
		HttpClient client = new SSLHttpClient();

		/* request construction */
		StringBuilder builder = new StringBuilder(url)
		.append("?"+SESSION_PARAM+"="+sessionId)
		.append("&"+CMD_PARAM+"="+CMD_SERVERMODIF);
		
		HttpGet method = new HttpGet(builder.toString());
		try {
			/* request execution */
			HttpResponse response = client.execute(method);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(header.getValue())) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					long expectedLength = response.getEntity().getContentLength();
					InputStream is = response.getEntity().getContent();
					FileUtils.writeInFile(is, out, expectedLength);
					return true;
				} else {
					throw new SynchronizationException("Command 'receive' : HTTP error code returned." + code, SynchronizationException.ERROR_RECEIVE);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_RECEIVE);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Command 'receive' -> ", e, SynchronizationException.ERROR_RECEIVE);
		}
	}

	public int directSend(UUID sessionId, FileInputStream fis) throws SynchronizationException {
		return sendData(sessionId, CMD_DIRECTSEND, fis);
	}
	
	public boolean searchEntity(String login, String password, String searcheId, OutputStream os) throws SynchronizationException {
		HttpClient client = new SSLHttpClient();

		/* request construction */
		StringBuilder builder = new StringBuilder(url).append(
				"?" + CMD_PARAM + "=" + CMD_SEARCH).append(
				"&" + LOGIN_PARAM + "=" + login).append(
				"&" + PASSWD_PARAM + "=" + password).append(
				"&" + SEARCH_PARAM + "=" + searcheId);

		HttpGet method = new HttpGet(builder.toString());
		try {
			/* request execution */
			HttpResponse response = client.execute(method);
			Header header = response.getFirstHeader(HEADER_NAME);
			if (header != null && HEADER_VALUE.equals(HEADER_VALUE)) {
				int code = response.getStatusLine().getStatusCode();
				if (code == HttpStatus.SC_OK) {
					long expectedLength = response.getEntity().getContentLength();
					InputStream is = response.getEntity().getContent();
					FileUtils.writeInFile(is, os, expectedLength);
					return true;
				} else {
					throw new SynchronizationException("Command 'search' : HTTP error code returned." + code, SynchronizationException.ERROR_SEARCH);
				}
			} else {
				throw new SynchronizationException("HTTP header is invalid", SynchronizationException.ERROR_SEARCH);
			}
		} catch (Exception e) {
			throw new SynchronizationException("Command 'search' -> ", e, SynchronizationException.ERROR_SEARCH);
		}
	}
}
