package org.imogene.sync.server.binary.blob;

import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.common.data.Binary;
import org.imogene.common.data.Synchronizable;

public class BinaryBlob implements Binary {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger("org.imogene.sync.server.binary");
	
	/* Synchronizable properties */
	private String id;
	private Date modified;	
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private Date created;
	private String createdBy;
	
	/* binary info */
	private String fileName; 
    private String contentType;  
	private long length;   
    private BlobStream content;
    
    /* related entity info */
    private String parentEntity;
    private String parentKey;
    private String parentFieldGetter;
    
    
    
    public BinaryBlob() {
    	content = new BlobStream();
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

	public void setModified(Date pModified) {
		modified = pModified;
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
	
	/* Binary getters and setters */
	
	public String getDisplayValue()
    {
        return getId();
    }

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
		//if (content != null)
		//	content.setLength((int)length);
	}

	public BlobStream getContent() {
		return content;
	}

	public void setContent(BlobStream content) {
		this.content = content;
	}

	public InputStream createInputStream() {	
		if (content != null) {
			return content.createInputStream();
		}
		return null;
	}

	public void setContent(InputStream sourceStream) {
		content.setStream(sourceStream);
	}

	public void setEntity(Synchronizable entity) {
	}

	/* related entity getters and setters */
	
	public String getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}

	public String getParentKey() {
		return parentKey;
	}

	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	public String getParentFieldGetter() {
		return parentFieldGetter;
	}

	public void setParentFieldGetter(String parentFieldGetter) {
		this.parentFieldGetter = parentFieldGetter;
	}
	
	
    
	



    
} 
