package org.imogene.ws.entity;


public class RoleBeanImpl implements MedooRole {

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
