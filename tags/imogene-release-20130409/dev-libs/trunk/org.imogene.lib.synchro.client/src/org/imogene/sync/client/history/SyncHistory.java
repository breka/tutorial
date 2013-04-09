package org.imogene.sync.client.history;

import java.util.Date;

public class SyncHistory {

	public final static int STATUS_OK = 0;
	
	public final static int STATUS_ERROR = 1;
	
	public final static int LEVEL_SEND = 0;
	
	public final static int LEVEL_RECEIVE = 1;
	
	private String id;
	
	private Date date;
	
	private int status;	
	
	private int level;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public int getLevel(){
		return level;
	}
}
