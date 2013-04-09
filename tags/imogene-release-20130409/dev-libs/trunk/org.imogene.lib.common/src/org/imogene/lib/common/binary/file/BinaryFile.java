package org.imogene.lib.common.binary.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.persistence.Entity;

import org.imogene.lib.common.binary.Binary;

@Entity
public class BinaryFile extends Binary {

	private String fileName;

	private String contentType;

	private long length;

	private String parentEntity;

	private String parentKey;

	private String parentFieldGetter;

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public long getLength() {
		return length;
	}

	@Override
	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public String getParentEntity() {
		return parentEntity;
	}

	@Override
	public void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}

	@Override
	public String getParentKey() {
		return parentKey;
	}

	@Override
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	@Override
	public String getParentFieldGetter() {
		return parentFieldGetter;
	}

	@Override
	public void setParentFieldGetter(String parentFieldGetter) {
		this.parentFieldGetter = parentFieldGetter;
	}

	@Override
	public InputStream createInputStream() {
		try {
			return new FileInputStream(BinaryFileManager.getInstance().buildFilePath(getId(), fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getDisplayValue() {
		return getId();
	}

}
