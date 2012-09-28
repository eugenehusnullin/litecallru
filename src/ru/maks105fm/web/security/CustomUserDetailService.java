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

import ru.maks105fm.dao.SecurityDao;

public class CustomUserDetailService implements UserDetailsService {

	private SecurityDao securityDao;

	@Required
	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails user = null;

		Map<String, Object> userMap = securityDao.getUser(username);
		if (userMap != null && userMap.size() >= 3) {
			long userId = (Long) userMap.get("id");
			String usertype = (String) userMap.get("usertype");
			
			if (securityDao.isUserOwnerDeleted(userId, usertype)) {
				throw new UsernameNotFoundException("Error: user owner deleted.");
			}
			
			String normalname = securityDao.getNormalname(userId, usertype);			
			List<Map<String, Object>> userRoles = securityDao.getUserRoles(userId);
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

			for (Map<String, Object> role : userRoles) {
				SimpleGrantedAuthority sga = new SimpleGrantedAuthority(
						(String) role.get("role"));
				authorities.add(sga);
			}

			user = new UserWithName(username, (String) userMap.get("password"),
					(Integer) userMap.get("enabled") == 1, true, true, true,
					authorities, normalname, userId, usertype);
		} else {
			throw new UsernameNotFoundException("Error in retrieving user");
		}

		return user;
	}

}
