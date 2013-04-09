package org.imogene.sync.server.history;

import java.util.Date;

public class SyncHistory {

	public static int STATUS_OK;
	
	public static int STATUS_ERROR;
	
	private String id;
	
	private Date time;
	
	private String terminalId;
	
	private int status;	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
