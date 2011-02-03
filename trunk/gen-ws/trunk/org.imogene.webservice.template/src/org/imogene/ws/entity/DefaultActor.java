package org.imogene.ws.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;


public class DefaultActor implements MedooActor {
	
	private String login;	
	private String id;	
	private Date lastLoginDate;	
	private String password;	
	private Date creationDate;	
	private String creator;	
	private Date modificationDate;	
	private String modifiedFrom;	
	private Set<MedooRole> roles = new HashSet<MedooRole>();
	

	public Date getLastLoginDate() {		
		return lastLoginDate;
	}

	public String getLogin() {		
		return login;
	}
	
	public void setLogin(String pLogin){
		login = pLogin;
	}


	public String getPassword() {		
		return password;
	}


	public void setLastLoginDate(Date date) {
		lastLoginDate = date;		
	}


	public void setPassword(String pass) {		
		password = pass;
	}


	public Date getCreationDate() {		
		return creationDate;
	}


	public String getCreator() {		
		return creator;
	}


	public String getDisplayValue() {		
		return login;
	}


	public String getId() {		
		return id;
	}


	public Date getLastModificationDate() {		
		return modificationDate;
	}


	public String getModifiedFrom() {		
		return modifiedFrom;
	}


	public String getModifier() {
		// TODO Auto-generated method stub
		return null;
	}


	public Date getUploadDate() {
		
		return null;
	}


	public void setCreationDate(Date date) {	
		creationDate = date;
	}


	public void setCreator(String creator) {
		this.creator = creator;	
	}


	public void setId(String id) {		
		this.id = id;
	}


	public void setLastModificationDate(Date date) {
		modificationDate = date;
	}


	public void setModifiedFrom(String terminal) {
		modifiedFrom = terminal;		
	}


	public void setModifier(String modifier) {
		//TODO do nothing	
	}


	public void setUploadDate(Date date) {
		// TODO Auto-generated method stub		
	}


	public Integer getDefaultNotificationMethod() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getNotificationLocale() {
		// TODO Auto-generated method stub
		return null;
	}


	public Boolean getBeNotified() {
		// TODO Auto-generated method stub
		return null;
	}


	public void setDefaultnotificationMethod(Integer method) {
		// TODO Auto-generated method stub
		
	}


	public void setBeNotified(Boolean notify) {
		// TODO Auto-generated method stub
		
	}


	public void setNotificationLacole(String iso) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setRoles(Set<MedooRole> pRoles){
		roles = pRoles;
	}
	
	public Set<MedooRole> getRoles(){
		return roles;
	}
	
	
	
}
