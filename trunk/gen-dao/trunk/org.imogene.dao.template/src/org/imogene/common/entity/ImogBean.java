package org.imogene.common.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * This interface describes an Imogene modeled business entity
 * 
 * @author Medes-IMPS
 */
public abstract class ImogBean implements Serializable {

	/** Get the unique ID for this bean */
	public abstract String getId();

	/** Set the unique ID for this bean */
	public abstract void setId(String id);

	/** Get the creation date of the bean. */
	public abstract Date getCreated();

	/** Set the creation date of the bean. */
	public abstract void setCreated(Date created);

	/** Get the original creator of this bean. */
	public abstract String getCreatedBy();

	/** Set the original creator of this bean. */
	public abstract void setCreatedBy(String createdBy);

	/** Get the last modification date of the bean. */
	public abstract Date getModified();

	/** Set the creation date of the bean. */
	public abstract void setModified(Date modified);

	/** Get the user which has modified this bean. */
	public abstract String getModifiedBy();

	/** Set the user which has modified this bean. */
	public abstract void setModifiedBy(String modifiedBy);

	/** Get the terminal which has modified this bean. */
	public abstract String getModifiedFrom();

	/** Set the terminal which has modified this bean. */
	public abstract void setModifiedFrom(String modifiedFrom);

	/** Get the upload date of the bean. */
	public abstract Date getUploadDate();

	/** Set the upload date of the bean. */
	public abstract void setUploadDate(Date upload);

	/** Get the String representation of the bean, described by the MainFields */
	public abstract String getDisplayValue();
}
