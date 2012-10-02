package ru.maks105fm.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ru.maks105fm.utils.DateUtils;

public class AdminJdbcDao extends JdbcDao implements AdminDao {

	@Override
	public List<Map<String, Object>> getClients(int pagesize,
			int page, boolean sortOrder) {
		String sort = sortOrder ? "asc" : "desc";
		
		String sql = "select a.id, a.name, a.partnerid, (select sum(a1.calltime) from cdr_partner_view a1 where a1.clientid = a.id) calltime " +
						"from client a " +
						"where a.deleted = 0 " +
						"order by calltime " + sort + ", id " +
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

	@Override
	public void rePartner(int partnerId, int clientId) {
		String sql = "update client set partnerid = ? where id = ?";
		jdbcTemplate.update(sql, partnerId, clientId);
	}

	@Override
	public List<Map<String, Object>> getPhonesCurMonth(int clientId, int pagesize,
			int page) {
		String sql = "select a.id, a.description, a.typedescr, a.tariff, " +
				"(select sum(a1.calltime) from cdr_partner_view a1 where a1.queueid = a.id " +
				"and a1.eventdate_utc >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') " +
				"and a1.eventdate_utc <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') " +
				") calltime " +
				"from queue a " +
				"where a.clientid = ? And a.deleted = 0 " +
				"order by calltime desc, a.description " +
				"LIMIT ? OFFSET ?";
		
		// calculate pagination
		int offset = (page - 1) * pagesize;
		int limit = pagesize;
		
		String startOfCurmonth = DateUtils.getStartOfMonthStr(0);
		String endOfCurmonth = DateUtils.getEndOfMonthStr(0);
				
		return jdbcTemplate.queryForList(sql, startOfCurmonth, endOfCurmonth, clientId, limit, offset);
	}

	@Override
	public int getPhonesCount(int clientId) {
		String sql = "select count(1) from queue a where a.clientid = ? And a.deleted = 0";
		return jdbcTemplate.queryForInt(sql, clientId);
	}

	@Override
	public void addPhone(int clientId, String description, String typedescr,
			BigDecimal tariff) {
		String sql = "insert into queue (name, description, clientid, typedescr, tariff) values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, description, description, clientId, typedescr, tariff);
	}

	@Override
	public void deletePhone(int phoneId) {
		String sql = "update queue set deleted = 1 where id = ?";
		jdbcTemplate.update(sql, phoneId);
	}
}
