package org.imogene.lib.common.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.imogene.lib.common.role.ImogRole;
import org.imogene.lib.common.sync.entity.SynchronizableEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ImogActorImpl extends ImogActor {

	private String login;

	private String password;

	private String notificationLocale;

	private Integer notificationMethod;

	private Boolean beNotified;

	private Map<String, String> notificationData;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SyncEntities", joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "synchronizable_id", referencedColumnName = "id"))
	private List<SynchronizableEntity> synchronizables;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "Roles", joinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private List<ImogRole> roles;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDate;

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getNotificationLocale() {
		return notificationLocale;
	}

	@Override
	public void setNotificationLocale(String notificationLocale) {
		this.notificationLocale = notificationLocale;
	}

	@Override
	public Integer getNotificationMethod() {
		return notificationMethod;
	}

	@Override
	public void setNotificationMethod(Integer notificationMethod) {
		this.notificationMethod = notificationMethod;
	}

	@Override
	public Boolean getBeNotified() {
		return beNotified;
	}

	@Override
	public void setBeNotified(Boolean beNotified) {
		this.beNotified = beNotified;
	}

	@Override
	public Map<String, String> getNotificationData() {
		return notificationData;
	}

	@Override
	public void setNotificationData(Map<String, String> notificationData) {
		this.notificationData = notificationData;
	}

	@Override
	public String getNotificationDataMethodName(String method) {
		return (String) notificationData.get(method);
	}

	@Override
	public List<SynchronizableEntity> getSynchronizables() {
		return synchronizables;
	}

	@Override
	public void setSynchronizables(List<SynchronizableEntity> synchronizables) {
		this.synchronizables = synchronizables;
	}

	@Override
	public void addSynchronizable(SynchronizableEntity synchronizable) {
		this.synchronizables.add(synchronizable);
	}

	@Override
	public List<ImogRole> getRoles() {
		return roles;
	}

	@Override
	public void setRoles(List<ImogRole> roles) {
		this.roles = roles;
	}

	@Override
	public void addRole(ImogRole role) {
		this.roles.add(role);
	}

	@Override
	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	@Override
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Override
	public boolean isAssignedRole(String id) {
		final List<ImogRole> roles = getRoles();
		if (roles != null) {
			for (ImogRole role : roles) {
				if (role.getId().equals(id)) {
					return true;
				}
			}
		}
		return false;
	}

}
