package ru.maks105fm.web.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ru.maks105fm.dao.Dao;

public class CustomUserDetailService implements UserDetailsService {

	private Dao dao;

	@Required
	public void setDao(Dao dao) {
		this.dao = dao;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = null;

		Map<String, Object> userMap = dao.getUser(username);
		if (userMap != null && userMap.size() >= 3) {
			long userId = (Long) userMap.get("id");
			
			List<Map<String, Object>> userRoles = dao.getUserRoles(userId);
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			for (Map<String, Object> role : userRoles) {
				SimpleGrantedAuthority sga = new SimpleGrantedAuthority(
						(String) role.get("role"));
				authorities.add(sga);
			}

			String usertype = (String) userMap.get("usertype");
			String normalname = dao.getNormalname(userId, usertype);

			user = new UserWithName(username, (String) userMap.get("password"),
					(Integer) userMap.get("enabled") == 1, true, true, true,
					authorities, normalname, userId, usertype);
		} else {
			throw new UsernameNotFoundException("Error in retrieving user");
		}

		return user;
	}

}
