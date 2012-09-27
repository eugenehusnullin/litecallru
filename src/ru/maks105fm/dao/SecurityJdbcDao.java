package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public class SecurityJdbcDao extends JdbcDao implements SecurityDao {
	@Override
	public Map<String, Object> getUser(String username) {
		String sql = "select id, username, password, enabled, usertype from \"user\" where username = ?";

		return jdbcTemplate.queryForMap(sql, username);
	}
	
	@Override
	public List<Map<String, Object>> getUserRoles(long userId) {
		String sql = "select a.role from userrole a where a.userid = ?";

		return jdbcTemplate.queryForList(sql, userId);
	}

	@Override
	public String getNormalname(long userId, String usertype) {
		String normalname = "";
		String sql = "";
		
		if (usertype.equals("client")) {
			sql = "select a.name from client a, clientuser b where a.id = b.clientid and b.userid = ?";
		} else if (usertype.equals("partner")) {
			sql = "select a.name from partner a, partneruser b where a.id = b.partnerid and b.userid = ?";
		} else if (usertype.equals("admin")) {
			sql = "select a.name from admin a, adminuser b where a.id = b.adminid and b.userid = ?";
		}
		
		if (sql != "") {
			normalname = (String) jdbcTemplate.queryForObject(sql, String.class, userId);
		}
		
		return normalname;
	}

}
