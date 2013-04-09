package org.imogene.sync.client;

import java.util.Date;
import java.util.Set;

import org.imogene.common.data.Synchronizable;
import org.imogene.uao.synchronizable.SynchronizableEntity;


/**
 * This interface describe the synchronization parameter object.
 * @author MEDES-IMPS
 */
public class SyncParameters implements Synchronizable {

	/* constant that specifies a binary synchronization type */
	public final static String SYNC_TYPE_BIN = "bin";
	
	/* constant that specifies a XML synchronization type */
	public final static String SYNC_TYPE_XML = "xml";
		
	/* */
	private static String ID = "sync-parameter";
	
	private Date created;
	
	private String createdBy;
	
	private String modifiedBy;
	
	private Date modified;
	
	private String login;
	
	private String password;
	
	private String terminalId;
	
	private Date uploadDate;
	
	private Set<SynchronizableEntity> directSend;
	
	private Set<SynchronizableEntity> synchronizable;
	
	private String type;
	
	private String url;
	
	/**
	 * Id of the parameters snapshot.
	 * @return the snapshot id
	 */
	public String getId(){
		return ID;
	}
	
	/*
	 * 
	 */
	public void setId(String id){		
	}
	
	/**
	 * Get the user login
	 * @return user login
	 */
	public String getLogin(){
		return login;
	}
	
	/**
	 * Set the user login
	 * @param login the user login
	 */
	public void setLogin(String pLogin){
		login = pLogin;
	}
	
	/**
	 * Get the user password
	 * @return user password
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * Set the user password
	 * @param password user password
	 */
	public void setPassword(String pPassword){
		password = pPassword;
	}
	
	/**
	 * Get the terminal id
	 * @return the terminal id
	 */
	public String getModifiedFrom(){
		return terminalId; 
	}
	
	/**
	 * Set the terminal id
	 * @param terminalId the terminal id
	 */
	public void setModifiedFrom(String pTerminalId){
		terminalId = pTerminalId;
	}
	
	/**
	 * Get the class name of the entities 
	 * that could be synchronized
	 * @return Array of class names
	 */
	public Set<SynchronizableEntity> getSynchronizable(){
		return synchronizable;
	}
	
	/**
	 * Set the class names of entities that could be synchronized
	 * @param classNames array of class name.
	 */
	public void setSynchronizable(Set<SynchronizableEntity> classNames){
		synchronizable = classNames;
	}
	
	/**
	 * Get the class name of the entities 
	 * that could be sent directly
	 * @return Array of class names
	 */
	public Set<SynchronizableEntity> getDirectSend(){
		return directSend;
	}
	
	/**
	 * Set the class names of entities 
	 * that could be sent directly
	 * @param classNames array of class name.
	 */
	public void setDirectSend(Set<SynchronizableEntity> classNames){
		directSend = classNames;
	}
	
	/**
	 * Get the synchronization type
	 * @return the synchronization type
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Set the synchronization type
	 * @param type the synchronization type
	 */
	public void setType(String pType){
		type = pType;
	}
	
	/**
	 * Get the synchronization server URL
	 * @return the synchronization server URL
	 */
	public String getUrl(){
		return url;
	}
	
	/**
	 * Set the synchronization server URL
	 * @param the synchronization server URL
	 */
	public void setUrl(String pUrl){
		url = pUrl;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the modified
	 */
	public Date getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(Date modified) {
		this.modified = modified;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
		
	/* (non-Javadoc)
	 * @see org.imogene.data.Synchronizable#setEntity(org.imogene.data.Synchronizable)
	 */
	public void setEntity(Synchronizable entity) {
		throw new RuntimeException("Not implemented"); 	
	}

	/**
	 * @return the uploadDateTime
	 */
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uploadDateTime the uploadDateTime to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getDisplayValue() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
	
}
