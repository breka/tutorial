package org.imogene.uao.role;

import java.util.Date;

import org.imogene.common.data.Synchronizable;


public class Role  implements Synchronizable {
	
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

	public Role() {
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
	
	public Date getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(Date date) {
		uploadDate = date;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}
	
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	public void setModifiedFrom(String modifiedFrom) {
		this.modifiedFrom = modifiedFrom;
	}

	public void setModified(Date pModified) {
		modified = pModified;
	}

	public void setModifiedBy(String id) {
		modifiedBy = id;
	}
	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public void setCreatedBy(String id) {
		createdBy = id;
	}
	
	public String getCreatedBy() {
		return createdBy;
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
