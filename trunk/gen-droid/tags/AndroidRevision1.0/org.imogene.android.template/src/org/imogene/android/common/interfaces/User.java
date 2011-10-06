package org.imogene.android.common.interfaces;


public interface User {
	
	public void setLogin(String login);
	
	public String getLogin();
	
	public void setPassword(byte[] password);
	
	public byte[] getPassword();
	
	public void setRoles(String roles);
	
	public String getRoles();
}
