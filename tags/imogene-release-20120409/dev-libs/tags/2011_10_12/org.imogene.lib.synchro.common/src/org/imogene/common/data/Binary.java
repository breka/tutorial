package org.imogene.common.data;

import java.io.InputStream;


public interface Binary extends Synchronizable {
	
	/**
	 * Gets the name of the file this Binary represents
	 * @return file name
	 */
    public String getFileName() ;

    /**
     * Sets the name of the file this Binary represents
     * @param name file name
     */
	public void setFileName(String name) ;
	
	/**
	 * Gets the content type of the file this Binary represents
	 * @return content type
	 */
    public String getContentType();

    /**
     * Sets the content type of the file this Binary represents
     * @param contentType content type
     */
    public void setContentType(String contentType);
    
    /**
     * Gets the number of bytes contained in this Binary
     * @return length
     */
    public long getLength();

    /**
     * Sets the number of bytes contained in this Binary
     * @param length length
     */
	public void setLength(long length);
    
    /**
     * Gets a new InputStream for reading this Binary. 
     * This method can be called more than once and should 
     * therefore return a new stream every time
     * @return an InputStream
     */
    public InputStream createInputStream();
    
    
    /**
     * Sets the content of the Binary
     * @param stream
     */
   // public void setContent(InputStream stream);
    	
    /**
     * Gets the parent entity short name
     * @return parent entity short name
     */
	public String getParentEntity() ;

	/**
	 * Sets the parent entity short name
	 * @param parentEntity parent entity short name
	 */
	public void setParentEntity(String parentEntity) ;

	/**
	 * Gets the parent entity id
	 * @return the parent entity id
	 */
	public String getParentKey() ;

	/**
	 * Sets the parent entity id
	 * @param parentKey the parent entity id
	 */
	public void setParentKey(String parentKey) ;

	/**
	 * Gets the getter name of the parent entity field
	 * @return the getter name of the parent entity field
	 */
	public String getParentFieldGetter() ;

	/**
	 * Sets the getter name of the parent entity field
	 * @param parentField the getter name of the parent entity field
	 */
	public void setParentFieldGetter(String parentField) ;
 
} 
