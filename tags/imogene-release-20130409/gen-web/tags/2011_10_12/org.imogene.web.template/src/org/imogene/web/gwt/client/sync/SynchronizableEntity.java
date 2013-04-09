package org.imogene.web.gwt.client.sync;

import java.util.Date;

public class SynchronizableEntity implements Synchronizable {
	
	/* Synchronizable properties */
	private String id;
	private Date modified;	
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private String createdBy;
	private Date created;

	/* Entity properties */
	private String name;

	public SynchronizableEntity() {
	}
	

	/* Synchronizable getters and setters */
	public String getId() {		
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}
	
	public Date getModified() {
		return modified;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModified(Date pModified) {
		modified = pModified;
	}
	
	public Date getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(Date date) {
		uploadDate = date;
	}

	public void setModifiedBy(String id) {
		modifiedBy = id;
	}
	
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public void setModifiedFrom(String modifiedFrom) {
		this.modifiedFrom = modifiedFrom;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	/* Entity getters and setters */
	public String getName() {
		return name;
	}

	public void setName(String param) {
		name = param;
	}

	public String getDisplayValue() {
		return name;
	}


	public void setEntity(Synchronizable entity) {
		// TODO Auto-generated method stub
		
	}
	

}
