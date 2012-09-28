package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

import ru.maks105fm.utils.DateUtils;

public class PartnerJdbcDao extends JdbcDao implements PartnerDao {
	private int getPartnerId(long userId) {
		String sql = "select a.partnerid from partneruser a where a.userid = ?";
		return jdbcTemplate.queryForInt(sql, userId);
	}
	
	@Override
	public List<Map<String, Object>> getDaylyCustom(long userId, String from, String to) {
		int partnerId = getPartnerId(userId);
		
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";
		
		String sql = "select date_trunc('day', a.eventdate_utc) date, " +
					"sum(a.calltime) calltime, sum(a.calltime) moneysum " +
				"from cdr_partner_view a " +
				"where a.partnerid = ? and " +
					"a.eventdate_utc >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') and " +
					"a.eventdate_utc <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') " +
				"group by date_trunc('day', a.eventdate_utc ) " +
				"order by date_trunc('day', a.eventdate_utc )";
		
		return jdbcTemplate.queryForList(sql, partnerId, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getDaylyPrvMonth(long userId) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);
		
		return getDaylyCustom(userId, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getDaylyCurMonth(long userId) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);
		
		return getDaylyCustom(userId, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getForDateDetailed(long userId, String date) {
		int partnerId = getPartnerId(userId);
		
		String strDate = date + " 00:00:00";
		
		String sql = "select b.name clientname, a.calltime, a.moneysum " +
				"from ( " +
				"select a.clientid, sum(a.calltime) calltime, sum(a.calltime) moneysum " +
				"from cdr_partner_view a " +
				"where a.partnerid = ? " +
				"and date_trunc('day', a.eventdate_utc) = to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') " +
				"group by a.clientid " +
				") a, client b " +
				"where a.clientid = b.id " +
				"order by b.name";
		
		return jdbcTemplate.queryForList(sql, partnerId, strDate);
	}

	
}
