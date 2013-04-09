package org.imogene.web.gwt.common.entity;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This interface describes an Imogene modeled business entity
 * @author Medes-IMPS
 */
public interface ImogBean extends IsSerializable {
	
	 /** Get the unique ID for this bean */
    public String getId();
    
    /** Set the unique ID for this bean */
    public void setId(String id);
    
    /** Get the original creator of this bean. */
    public String getCreator();
    
    /** Set the original creator of this bean. */
    public void setCreator(String creator);
    
    /** Get the creation date of the bean. */
    public Date getCreationDate();
    
    /** Set the creation date of the bean. */
    public void setCreationDate(Date date);   
    
    /** Get the user which has modified this bean. */
    public String getModifier();
    
    /** Set the user which has modified this bean. */
    public void setModifier(String modifier);    
    
    /** Get the terminal which has modified this bean. */
    public String getModifiedFrom();
    
    /** Set the terminal which has modified this bean. */
    public void setModifiedFrom(String terminal); 
    
    /** Get the last modification date of the bean. */
    public Date getLastModificationDate();
    
    /** Set the creation date of the bean. */
    public void setLastModificationDate(Date date);  
    
    /** Get the upload date of the bean. */
    public Date getUploadDate();    
    
    /** Set the upload date of the bean. */
    public void setUploadDate(Date date);  
    
    /** Get the String representation of the bean, described by the MainFields*/
    public String getDisplayValue();
}
