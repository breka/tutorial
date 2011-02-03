package org.imogene.sync.server.metadata;

import org.springframework.web.multipart.MultipartFile;

public class MetadataCommand {

	/* the xml file */
	private MultipartFile data;
	
	/* the terminal id */
	private String id;

	public MultipartFile getData() {
		return data;
	}

	public void setData(MultipartFile data) {
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
