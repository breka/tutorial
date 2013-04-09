package org.imogene.synchro.security;

import java.util.ArrayList;
import java.util.Collection;

import org.imogene.common.data.SynchronizableUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@SuppressWarnings("serial")
public class MedooUserDetails implements UserDetails {
	
	SynchronizableUser actor;

	public MedooUserDetails(SynchronizableUser actor) {
		this.actor = actor;

	}

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		/*for(MedooRole role : actor.getRoles()){
			ga.add(new GrantedAuthorityImpl(role.getId()));
		}*/
		return ga;
	}

	public String getPassword() {
		return actor.getPassword();
	}

	public String getUsername() {
		return actor.getLogin();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
	
	public SynchronizableUser getMedooActor(){
		return actor;
	}

	
	
}
