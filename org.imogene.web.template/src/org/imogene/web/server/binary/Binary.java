package org.imogene.web.server.binary;

import java.io.InputStream;

import org.imogene.web.gwt.common.entity.ImogBean;



public interface Binary extends ImogBean
{   
	public String getFileName() ;

	public void setFileName(String name) ;

    public String getContentType() ;

    public void setContentType(String contentType) ;
    
	public long getLength() ;

	public void setLength(long _length) ;
    
	public String getParentEntity() ;

	public void setParentEntity(String entity) ;
	
    public String getParentKey() ;

    public void setParentKey(String key) ;

	public String getParentFieldGetter() ;

	public void setParentFieldGetter(String fieldGetter) ;
    
	public InputStream createInputStream() ;

}
