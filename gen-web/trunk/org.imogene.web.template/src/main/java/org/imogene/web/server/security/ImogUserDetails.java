package org.imogene.web.server.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.imogene.lib.common.entity.ImogActor;
import org.imogene.lib.common.role.ImogRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;



@SuppressWarnings("serial")
public class ImogUserDetails implements UserDetails {
	
	ImogActor actor;

	public ImogUserDetails(ImogActor actor) {
		this.actor = actor;

	}

	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> ga = new ArrayList<GrantedAuthority>();
		for(ImogRole role : actor.getRoles()){
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
//		Date passwordExpirationDate = actor.getPasswordExpirationDate();
//		if(passwordExpirationDate!=null)
//			return passwordExpirationDate.before(new Date());
//		else
//			return false;
		
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
	
	public ImogActor getImogActor(){
		return actor;
	}		
}
