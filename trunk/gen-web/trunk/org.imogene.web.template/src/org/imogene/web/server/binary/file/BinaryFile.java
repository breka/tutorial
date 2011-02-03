package org.imogene.web.server.binary.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.imogene.web.server.binary.Binary;


public class BinaryFile implements Binary {
	private Logger logger = Logger.getLogger(BinaryFile.class.getName());
	
    private String _id;
    private String _creator;
    private Date _creationDate;
    private String _modifier;
    private String _modifiedFrom;
    private Date _lastModificationDate;
    private Date _uploadDate;

    private String _fileName; 
    private String _contentType; 
    private long _length; 
    
    private String _parentEntity;
    private String _parentKey;
    private String _parentFieldGetter;   
    
     

    public BinaryFile()
    {
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }
    
	public String getFileName() {
		return _fileName;
	}

	public void setFileName(String name) {
		_fileName = name;
	}

    public String getContentType() {
        return _contentType;
    }

    public void setContentType(String contentType) {
        _contentType = contentType;
    }
    
	public long getLength() {
		return _length;
	}

	public void setLength(long _length) {
		this._length = _length;
	}
	
	public String getParentEntity() {
		return _parentEntity;
	}

	public void setParentEntity(String entity) {
		_parentEntity = entity;
	}
	
    public String getParentKey() {
        return _parentKey;
    }

    public void setParentKey(String key) {
        _parentKey = key;
    }

	public String getParentFieldGetter() {
		return _parentFieldGetter;
	}

	public void setParentFieldGetter(String fieldGetter) {
		_parentFieldGetter = fieldGetter;
	}

    public String getCreator(){
        return _creator;
    }

    public void setCreator(String param) {
        _creator = param;
    }

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date creationDate) {
        _creationDate = creationDate;
    }
    
    public String getModifier() {
        return _modifier;
    }

    public void setModifier(String param) {
    	_modifier = param;
    }
    public String getModifiedFrom() {
		return _modifiedFrom;
	}

	public void setModifiedFrom(String from) {
		_modifiedFrom = from;
	}

	public Date getLastModificationDate() {
        return _lastModificationDate;
    }

    public void setLastModificationDate(Date lastModificationDate) {
        _lastModificationDate = lastModificationDate;
    }
    
	public Date getUploadDate() {
		return _uploadDate;
	}
	
	public void setUploadDate(Date date) {
		_uploadDate = date;
	}
    
	public InputStream createInputStream() {
		try {
			return new FileInputStream(BinaryFileManager.getInstance().buildFilePath(_id, _fileName));
		} catch (FileNotFoundException ex) {
			logger.error(ex.getMessage());
		}
		return null;
	}
	
	
    public String getDisplayValue() {
        return getId();
    }


    
} // end class Binary
