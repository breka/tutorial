package org.imogene.common.metadata;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class ImogMetadata {

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String terminalId;

	private String name;

	private String value;

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

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String temrinalId) {
		this.terminalId = temrinalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
