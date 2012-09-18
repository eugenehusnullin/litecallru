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

public class CustomPartnerService implements UserDetailsService {

	private Dao dao;

	@Required
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = null;
		
		Map<String, Object> partnerMap = dao.getPartnerByUsername(username);
		if (partnerMap != null && partnerMap.size() >= 3) {
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_PARTNER");
			authorities.add(sga);
			
			user = new User(username, (String) partnerMap.get("password"), 
					(Integer) partnerMap.get("enabled") == 1, true, true, true, authorities);
		} else {
			throw new UsernameNotFoundException("Error in retrieving user");
		}
		return user;
	}
}
