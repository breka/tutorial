package org.imogene.common.sync.history;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class SyncHistory implements Serializable {

	public final static int STATUS_OK = 0;

	public final static int STATUS_ERROR = 1;

	public final static int LEVEL_SEND = 0;

	public final static int LEVEL_RECEIVE = 1;

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	private String terminalId;

	private int status;

	private int level;

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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
