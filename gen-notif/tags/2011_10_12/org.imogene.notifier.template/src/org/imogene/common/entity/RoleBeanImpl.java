package org.imogene.common.entity;


public class RoleBeanImpl implements ImogRole {

	private String id;
	
	private String name;
	
	@Override
	public String getId() {		
		return id;
	}

	@Override
	public String getName() {		
		return name;
	}

	@Override
	public void setId(String pId) {
		id = pId;
	}

	@Override
	public void setName(String roleName) {		
		name = roleName;
	}

}
