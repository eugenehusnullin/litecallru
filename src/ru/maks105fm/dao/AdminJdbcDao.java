package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public class AdminJdbcDao extends JdbcDao implements AdminDao {

	@Override
	public List<Map<String, Object>> getClients(int pagesize,
			int page, boolean sortOrder) {
		String sort = sortOrder ? "asc" : "desc";
		
		String sql = "select a.id, a.name, a.partnerid, (select sum(a1.calltime) from cdr_partner_view a1 where a1.clientid = a.id) calltime " +
						"from client a " +
						"where a.deleted = 0 " +
						"order by calltime " + sort +
						" LIMIT ? OFFSET ?";
		
		
		
		// calculate pagination
		int offset = (page - 1) * pagesize;
		int limit = pagesize;
				
		return jdbcTemplate.queryForList(sql, limit, offset);
	}

	@Override
	public int getClientsCount() {
		String sql = "select count(1) from client a where a.deleted = 0";
		return jdbcTemplate.queryForInt(sql);
	}

	@Override
	public void deleteClient(Integer clientId) {
		String sql = "update client set deleted = 1 where id = ?";
		jdbcTemplate.update(sql, clientId);
	}

	@Override
	public void addClient(String name, String email, Integer partnerId) {
		String sql = "insert into client (name, email, partnerid) values (?, ?, ?)";
		jdbcTemplate.update(sql, name, email, partnerId);
	}
}
