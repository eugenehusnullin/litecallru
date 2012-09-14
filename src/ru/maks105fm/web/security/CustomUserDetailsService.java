package ru.maks105fm.web.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.maks105fm.dao.Dao;

public class CustomUserDetailsService implements UserDetailsService {
	
	private Dao dao;
	
	@Required
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = null;
		
		Map<String, Object> userMap = dao.getUserByUsername(username);
		if (userMap != null && userMap.size() >= 3) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_USER");
			authorities.add(sga);
			
			
			user = new User(username, (String) userMap.get("password"), 
					(Integer) userMap.get("enabled") == 1, true, true, true, authorities);
		} else {
			throw new UsernameNotFoundException("Error in retrieving user");
		}
		
		return user;
	}

}
