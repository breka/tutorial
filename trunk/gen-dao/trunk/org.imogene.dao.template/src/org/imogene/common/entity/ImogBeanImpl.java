package org.imogene.common.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ImogBeanImpl extends ImogBean {

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String modifiedBy;

	private String modifiedFrom;

	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadDate;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date created) {
		this.created = created;
	}

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Date getModified() {
		return modified;
	}

	@Override
	public void setModified(Date modified) {
		this.modified = modified;
	}

	@Override
	public String getModifiedBy() {
		return modifiedBy;
	}

	@Override
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	@Override
	public void setModifiedFrom(String modifiedFrom) {
		this.modifiedFrom = modifiedFrom;
	}

	@Override
	public Date getUploadDate() {
		return uploadDate;
	}

	@Override
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	@Override
	public String getDisplayValue() {
		return null;
	}

}
