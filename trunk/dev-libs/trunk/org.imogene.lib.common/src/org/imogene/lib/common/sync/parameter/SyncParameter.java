package org.imogene.lib.common.sync.parameter;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.imogene.lib.common.sync.entity.SynchronizableEntity;

/**
 * This interface describe the synchronization parameter object.
 * 
 * @author MEDES-IMPS
 */
@Entity
public class SyncParameter implements Serializable {

	/* constant that specifies a binary synchronization type */
	public static final String SYNC_TYPE_BIN = "bin";

	/* constant that specifies a XML synchronization type */
	public static final String SYNC_TYPE_XML = "xml";

	private static final String ID = "sync-parameter";

	@Id
	private String id = ID;

	private String login;

	private String password;

	private String terminalId;

	@OneToMany(targetEntity = SynchronizableEntity.class)
	private Set<SynchronizableEntity> directSend;

	@OneToMany(targetEntity = SynchronizableEntity.class)
	private Set<SynchronizableEntity> synchronizables;

	private String type;

	private String url;

	/**
	 * Id of the parameters snapshot.
	 * 
	 * @return the snapshot id
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
	}

	/**
	 * Get the user login
	 * 
	 * @return user login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Set the user login
	 * 
	 * @param login the user login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Get the user password
	 * 
	 * @return user password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the user password
	 * 
	 * @param password user password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the terminal id
	 * 
	 * @return the terminal id
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * Set the terminal id
	 * 
	 * @param terminalId the terminal id
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * Get the class name of the entities that could be synchronized
	 * 
	 * @return Array of class names
	 */
	public Set<SynchronizableEntity> getSynchronizables() {
		return synchronizables;
	}

	/**
	 * Set the class names of entities that could be synchronized
	 * 
	 * @param synchronizables array of synchronizables entities.
	 */
	public void setSynchronizables(Set<SynchronizableEntity> synchronizables) {
		this.synchronizables = synchronizables;
	}

	/**
	 * Get the class name of the entities that could be sent directly
	 * 
	 * @return Array of class names
	 */
	public Set<SynchronizableEntity> getDirectSend() {
		return directSend;
	}

	/**
	 * Set the class names of entities that could be sent directly
	 * 
	 * @param directSend array of class name.
	 */
	public void setDirectSend(Set<SynchronizableEntity> directSend) {
		this.directSend = directSend;
	}

	/**
	 * Get the synchronization type
	 * 
	 * @return the synchronization type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the synchronization type
	 * 
	 * @param type the synchronization type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the synchronization server URL
	 * 
	 * @return the synchronization server URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the synchronization server URL
	 * 
	 * @param the synchronization server URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
