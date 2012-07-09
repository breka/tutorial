package org.imogene.common.role;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role implements Serializable {

	@Id
	private String id;

	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String pId) {
		id = pId;
	}

	public void setName(String roleName) {
		name = roleName;
	}

}
