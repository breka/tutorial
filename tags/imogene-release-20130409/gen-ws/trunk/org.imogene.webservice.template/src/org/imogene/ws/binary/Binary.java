package org.imogene.ws.binary;

import org.imogene.ws.entity.MedooBean;


public interface Binary extends MedooBean
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
    
	//public InputStream createInputStream() ;

}
