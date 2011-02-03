package org.imogene.sync.server.binary.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.common.data.Binary;
import org.imogene.common.data.Synchronizable;

public class BinaryFile implements Binary {
	
	private Logger logger = Logger.getLogger("org.imogene.sync.server.binary");	
	
	/* synchronizable properties */
	private String id;
	private Date modified;	
	private Date uploadDate;
	private String modifiedBy;
	private String modifiedFrom;
	private Date created;
	private String createdBy;
	
	/* binary properties */
	private String fileName; 
    private String contentType;  
	private long length;   
    
    /* related entity properties */
    private String parentEntity;
    private String parentKey;
    private String parentFieldGetter;
    

	/**
	 * @return the id
	 */
	public String getId() {
		
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * 
	 */
	public Date getUploadDate() {
		return uploadDate;
	}
	
	/**
	 * 
	 */
	public void setUploadDate(Date date) {
		uploadDate = date;
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
	 * 
	 */
	public String getModifiedFrom() {
		return modifiedFrom;
	}

	/**
	 * 
	 */
	public void setModifiedFrom(String modifiedFrom) {
		this.modifiedFrom = modifiedFrom;
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
	
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	/**
	 * @return the length
	 */
	public long getLength() {
		return length;
	}
	
	/**
	 * @param length the length to set
	 */
	public void setLength(long length) {
		this.length = length;
	}
	
	/**
	 * @return the parentEntity
	 */
	public String getParentEntity() {
		return parentEntity;
	}
	
	/**
	 * @param parentEntity the parentEntity to set
	 */
	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}
	
	/**
	 * @return the parentKey
	 */
	public String getParentKey() {
		return parentKey;
	}
	
	/**
	 * @param parentKey the parentKey to set
	 */
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	
	/**
	 * @return the parentFieldGetter
	 */
	public String getParentFieldGetter() {
		return parentFieldGetter;
	}
	
	/**
	 * @param parentFieldGetter the parentFieldGetter to set
	 */
	public void setParentFieldGetter(String parentFieldGetter) {
		this.parentFieldGetter = parentFieldGetter;
	}
	
	/**
	 * 
	 */
	public InputStream createInputStream() {
		try {
			return new FileInputStream(BinaryFileManager.getInstance().buildFilePath(id, fileName));
		} catch (FileNotFoundException ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}

	/**
	 * 
	 */
	public void setEntity(Synchronizable entity) {
		// TODO Auto-generated method stub		
	}

	public String getDisplayValue() {
		return fileName;
	}
	
	
	 
    
}
