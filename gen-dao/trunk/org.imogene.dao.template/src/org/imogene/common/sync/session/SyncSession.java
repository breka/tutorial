package org.imogene.common.sync.session;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class represents a synchronization session.
 * 
 * @author MEDES-IMPS
 */
@Entity
public class SyncSession implements Serializable {

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date initDate;

	private String terminalId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date sendDate;

	private String type;

	private String userId;

	private String userType;

	/**
	 * Get the session id
	 * 
	 * @return the session id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the session id
	 * 
	 * @param id the session id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the date at when the session was initiated
	 * 
	 * @return the initiation date.
	 */
	public Date getInitDate() {
		return initDate;
	}

	/**
	 * Set the session initiation date
	 * 
	 * @param initDate the initiation date
	 */
	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	/**
	 * Get the id of the terminal that initiated this session.
	 * 
	 * @return The terminal id
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * Set the id of the terminal associated with this session.
	 * 
	 * @param terminalId
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * Get the date at when we started to send local modifications.
	 * 
	 * @return The date when we started to send local modification
	 */
	public Date getSendDate() {
		return sendDate;
	}

	/**
	 * Set the date when it started to send local modification.
	 * 
	 * @param sendDate the date
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * get the client type (ie: binstream, xml ...)
	 * 
	 * @return The client type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the client type
	 * 
	 * @param sessionType the client type
	 */
	public void setType(String sessionType) {
		type = sessionType;
	}

	/**
	 * Get the user id of the user that initiated this session.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Set the user id that initiated this session
	 * 
	 * @param userId the user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Set the type of the user that initiated this session
	 * 
	 * @return the user type (entity shortname)
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Get the type of the user that initiated this session
	 * 
	 * @param userType the user type (entity shortname)
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

}
