package org.imogene.lib.common.binary;

import java.io.InputStream;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.imogene.lib.common.entity.ImogBeanImpl;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Binary extends ImogBeanImpl {

	public abstract String getFileName();

	public abstract void setFileName(String name);

	public abstract String getContentType();

	public abstract void setContentType(String contentType);

	public abstract long getLength();

	public abstract void setLength(long _length);

	public abstract String getParentEntity();

	public abstract void setParentEntity(String entity);

	public abstract String getParentKey();

	public abstract void setParentKey(String key);

	public abstract String getParentFieldGetter();

	public abstract void setParentFieldGetter(String fieldGetter);

	public abstract InputStream createInputStream();

}
