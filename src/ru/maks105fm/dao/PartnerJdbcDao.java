package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Required;

import ru.maks105fm.utils.DateUtils;

public class PartnerJdbcDao extends JdbcDao implements PartnerDao {
	
	private AdminDao adminDao;
	
	@Required
	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}
	
	private int getPartnerId(long userId) {
		String sql = "select a.partnerid from partneruser a where a.userid = ?";
		return jdbcTemplate.queryForInt(sql, userId);
	}
	
	@Override
	public List<Map<String, Object>> getDaylyCustom(long userId, String from, String to) {
		int partnerId = getPartnerId(userId);
		
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";
		
		String sql = "select date_trunc('day', a.eventdate_msk) date, " +
					"sum(a.calltime) calltime, sum(a.calltime) moneysum " +
				"from cdr_partner_view a " +
				"where a.partnerid = ? and " +
					"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') and " +
					"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') " +
				"group by date_trunc('day', a.eventdate_msk ) " +
				"order by date_trunc('day', a.eventdate_msk )";
		
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
				"and date_trunc('day', a.eventdate_msk) = to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') " +
				"group by a.clientid " +
				") a, client b " +
				"where a.clientid = b.id " +
				"order by b.name";
		
		return jdbcTemplate.queryForList(sql, partnerId, strDate);
	}

	@Override
	public long getMoneyFullSumCustom(long userId, String from, String to) {
		int partnerId = getPartnerId(userId);
		
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";
		
		String sql = "select sum(a.calltime) " +
				"from cdr_partner_view a " +
				"where a.partnerid = ? and " +
					"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') and " +
					"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";
		
		return jdbcTemplate.queryForLong(sql, partnerId, strFrom, strTo);
	}

	@Override
	public long getMoneyFullSumPrvMonth(long userId) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);
		
		return getMoneyFullSumCustom(userId, strFrom, strTo);
	}

	@Override
	public long getMoneyFullSumCurMonth(long userId) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);
		
		return getMoneyFullSumCustom(userId, strFrom, strTo);
	}

	@Override
	public void setAgree(long userId) {
		int partnerId = getPartnerId(userId);
		String sql = "insert into partneragree (partnerId) values(?)";
		jdbcTemplate.update(sql, partnerId);
		
		adminDao.addUserrole(userId, "ROLE_PARTNER");
	}

	@Override
	public boolean getAgree(long userId) {
		int partnerId = getPartnerId(userId);
		String sql = "select count(1) from partneragree a where a.partnerId = ?";
		return jdbcTemplate.queryForInt(sql, partnerId) == 1;
	}

	
}
