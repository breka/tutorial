package org.imogene.common.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.imogene.notifier.server.Constants;



public class DefaultActor implements ImogActor {
	
	private String login;
	
	private String id;
	
	private Date lastLoginDate;
	
	private String password;
	
	private Date creationDate;
	
	private String creator;
	
	private Date modificationDate;
	
	private String modifiedFrom;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getUploadDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCreationDate(Date date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCreator(String creator) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setId(String id) {		
		this.id = id;
	}

	@Override
	public void setLastModificationDate(Date date) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setModifiedFrom(String terminal) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setModifier(String modifier) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setUploadDate(Date date) {
		// TODO Auto-generated method stub		
	}

	@Override
	public Integer getDefaultNotificationMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNotificationLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean getBeNotified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDefaultnotificationMethod(Integer method) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBeNotified(Boolean notify) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNotificationLacole(String iso) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void setRoles(Set<ImogRole> pRoles){
		roles = pRoles;
	}
	
	public Set<ImogRole> getRoles(){
		return roles;
	}

	@Override
	public String getNotificationData(int method) {
		if(method == Constants.SMSMethod)
			return "0678664038";
		if(method == Constants.MailMethod)
			return "ylapeyre@gmail.com";
		return null;
	}
	
	
	
	
}
