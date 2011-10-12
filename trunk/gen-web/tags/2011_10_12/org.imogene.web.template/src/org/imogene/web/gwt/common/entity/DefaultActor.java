package org.imogene.web.gwt.common.entity;

import java.util.Date;
import java.util.Set;
import java.util.HashSet;


public class DefaultActor implements ImogActor {
	
	private String id;
	private Date creationDate;	
	private String creator;	
	private Date modificationDate;
	private String modifiedFrom;
	
	private String login;
	private String password;	
	private Date lastLoginDate;
	private Set<ImogRole> roles = new HashSet<ImogRole>();
	
	

	@Override
	public Date getLastLoginDate() {		
		return lastLoginDate;
	}

	@Override
	public String getLogin() {		
		return login;
	}
	
	public void setLogin(String pLogin){
		login = pLogin;
	}

	@Override
	public String getPassword() {		
		return password;
	}

	@Override
	public void setLastLoginDate(Date date) {
		lastLoginDate = date;		
	}

	@Override
	public void setPassword(String pass) {		
		password = pass;
	}

	@Override
	public Date getCreationDate() {		
		return creationDate;
	}

	@Override
	public String getCreator() {		
		return creator;
	}

	@Override
	public String getDisplayValue() {		
		return login;
	}

	@Override
	public String getId() {		
		return id;
	}

	@Override
	public Date getLastModificationDate() {		
		return modificationDate;
	}

	@Override
	public String getModifiedFrom() {		
		return modifiedFrom;
	}

	@Override
	public String getModifier() {
		return null;
	}

	@Override
	public Date getUploadDate() {
		return null;
	}
	
	@Override
	public Integer getDefaultNotificationMethod() {
		return null;
	}

	@Override
	public String getNotificationLocale() {
		return null;
	}

	@Override
	public Boolean getBeNotified() {
		return null;
	}

	@Override
	public void setCreationDate(Date date) {	
		creationDate = date;
	}

	@Override
	public void setCreator(String creator) {
		this.creator = creator;	
	}

	@Override
	public void setId(String id) {		
		this.id = id;
	}

	@Override
	public void setLastModificationDate(Date date) {
		modificationDate = date;
	}

	@Override
	public void setModifiedFrom(String terminal) {
		modifiedFrom = terminal;		
	}
	
	public void setRoles(Set<ImogRole> pRoles){
		roles = pRoles;
	}
	
	public Set<ImogRole> getRoles(){
		return roles;
	}

	@Override
	public void setModifier(String modifier) {
	}

	@Override
	public void setUploadDate(Date date) {	
	}

	@Override
	public void setDefaultnotificationMethod(Integer method) {
	}

	@Override
	public void setBeNotified(Boolean notify) {
	}

	@Override
	public void setNotificationLacole(String iso) {
	}


	
	
	
	
	
}
