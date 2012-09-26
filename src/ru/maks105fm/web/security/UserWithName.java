package ru.maks105fm.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings("serial")
public class UserWithName extends User {
	
	private long id;
	private String humanname;
	private String usertype;

	public UserWithName(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			String humanname, long id, String usertype) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		
		this.humanname = humanname;
		this.id = id;
		this.usertype = usertype;
	}

	public String getHumanname() {
		return humanname;
	}
	
	public long getId() {
		return id;
	}
	
	public String getUsertype() {
		return usertype;
	}
}
