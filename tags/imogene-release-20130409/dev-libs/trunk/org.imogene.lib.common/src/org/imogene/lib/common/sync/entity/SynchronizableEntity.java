package org.imogene.lib.common.sync.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SynchronizableEntity implements Serializable {

	@Id
	private String id;

	private String name;

	public String getId() {
		return id;
	}

	public void setId(String pId) {
		id = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
