package org.imogene.android.common.interfaces;


public interface User {
	
	public static interface Columns extends Entity.Columns {
		public static final String LOGIN = "login";
		public static final String PASSWORD = "password";
		public static final String ROLES = "assignedRoles";
	}
	
	public void setLogin(String login);
	
	public String getLogin();
	
	public void setPassword(byte[] password);
	
	public byte[] getPassword();
	
	public void setRoles(String roles);
	
	public String getRoles();
}
