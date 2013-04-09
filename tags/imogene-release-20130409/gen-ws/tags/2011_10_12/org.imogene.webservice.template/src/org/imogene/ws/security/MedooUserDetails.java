package org.imogene.ws.security;

import java.util.ArrayList;
import java.util.Collection;

import org.imogene.ws.entity.MedooActor;
import org.imogene.ws.entity.MedooRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;



@SuppressWarnings("serial")
public class MedooUserDetails implements UserDetails {
	
	MedooActor actor;

	public MedooUserDetails(MedooActor actor) {
		this.actor = actor;

	}

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		for(MedooRole role : actor.getRoles()){
			ga.add(new GrantedAuthorityImpl(role.getId()));
		}
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
	
	public MedooActor getMedooActor(){
		return actor;
	}

	
	
}
