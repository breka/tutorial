package org.imogene.web.gwt.common.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RoleBeanImpl implements ImogRole, IsSerializable {

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
