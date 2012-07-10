package org.imogene.lib.common.binary.blob;

import java.io.InputStream;

import javax.persistence.Entity;

import org.imogene.lib.common.binary.Binary;

@Entity
public class BinaryBlob extends Binary {

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

	public BlobStream getContent() {
		return content;
	}

	public void setContent(BlobStream content) {
		this.content = content;
	}

	@Override
	public InputStream createInputStream() {
		if (content != null) {
			return content.createInputStream();
		}
		return null;
	}

	public void setContent(InputStream sourceStream) {
		content.setStream(sourceStream);
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

}
