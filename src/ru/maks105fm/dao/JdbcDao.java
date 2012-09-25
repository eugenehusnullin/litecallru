package ru.maks105fm.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcDao implements Dao {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	@Required
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;

		jdbcTemplate = new JdbcTemplate(this.dataSource);
	}
	
	private long getClientidByUsername(String username) {
		return jdbcTemplate.queryForLong(
				"select a.clientid from \"user\" a where a.username = ?",
				username);
	}

	@Override
	public List<Map<String, Object>> getQueues(String username) {
		long clientid = getClientidByUsername(username);
		if (clientid == 0) {
			return null;
		}

		return jdbcTemplate.queryForList("select a.name, a.description"
				+ " from queue a where a.clientid = ?" + " order by a.name",
				clientid);
	}

	@Override
	public List<Map<String, Object>> getQueueLogCustom(String queueName,
			String from, String to, int pagesize, int page) {

		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		// calculate pagination
		int offset = (page - 1) * pagesize;
		int limit = pagesize;

		String sql = "SELECT to_char(eventdate, 'DD.MM.YY HH24:MI') eventdate, eventdate eventdate_utc, uniqueid, queuename, "
				+ " agent, event, waittime, (((calltime / 60) + 1))*call calltime, call, callerid, "
				+ " row_number() over(order by a.eventdate DESC) rownum FROM cdr_queue_view a"
				+ " WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')"
				+ " ORDER BY a.eventdate DESC LIMIT ? OFFSET ?";

		return jdbcTemplate.queryForList(sql, queueName, strFrom, strTo, limit,
				offset);
	}

	private Date getStartOfMonth(int add) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, add);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		return c.getTime();
	}

	private String getStartOfMonthStr(int add) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(getStartOfMonth(add));
	}

	private Date getEndOfMonth(int add) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, add);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		return c.getTime();
	}

	private String getEndOfMonthStr(int add) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(getEndOfMonth(add));
	}

	@Override
	public List<Map<String, Object>> getQueueLogPrvMonth(String queueName,
			int pagesize, int page) {
		// DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		// String strFrom = df.format(from);
		// String strTo = df.format(to);

		String strFrom = getStartOfMonthStr(-1);
		String strTo = getEndOfMonthStr(-1);

		return getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public List<Map<String, Object>> getQueueLogCurMonth(String queueName,
			int pagesize, int page) {
		String strFrom = getStartOfMonthStr(0);
		String strTo = getEndOfMonthStr(0);

		return getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public int getCustomCallsCount(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthCallsCount(String queueName) {
		String strFrom = getStartOfMonthStr(-1);
		String strTo = getEndOfMonthStr(-1);

		return getCustomCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthCallsCount(String queueName) {
		String strFrom = getStartOfMonthStr(0);
		String strTo = getEndOfMonthStr(0);

		return getCustomCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getCustomReceivedCallsCount(String queueName, String from,
			String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getCustomAverageWaitTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT round(avg(a.waittime)) FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public long getCustomSumCallTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT sum(((calltime / 60) + 1)) FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthReceivedCallsCount(String queueName) {
		String strFrom = getStartOfMonthStr(-1);
		String strTo = getEndOfMonthStr(-1);

		return getCustomReceivedCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthAverageWaitTime(String queueName) {
		String strFrom = getStartOfMonthStr(-1);
		String strTo = getEndOfMonthStr(-1);

		return getCustomAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getPrvMonthSumCallTime(String queueName) {
		String strFrom = getStartOfMonthStr(-1);
		String strTo = getEndOfMonthStr(-1);

		return getCustomSumCallTime(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthReceivedCallsCount(String queueName) {
		String strFrom = getStartOfMonthStr(0);
		String strTo = getEndOfMonthStr(0);

		return getCustomReceivedCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthAverageWaitTime(String queueName) {
		String strFrom = getStartOfMonthStr(0);
		String strTo = getEndOfMonthStr(0);

		return getCustomAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getCurMonthSumCallTime(String queueName) {
		String strFrom = getStartOfMonthStr(0);
		String strTo = getEndOfMonthStr(0);

		return getCustomSumCallTime(queueName, strFrom, strTo);
	}

	@Override
	public Map<String, Object> getUserByUsername(String username) {
		String sql = "select username, password, enabled, name from \"user\" where username = ?";

		return jdbcTemplate.queryForMap(sql, username);
	}

	@Override
	public Map<String, Object> getPartnerByUsername(String username) {
		String sql = "select username, password, enabled, name from \"partner\" where username = ?";

		return jdbcTemplate.queryForMap(sql, username);
	}
	
	private int getPartneridByName(String name) {
		String sql = "select a.id from partner a where a.username = ?";
		return jdbcTemplate.queryForInt(sql, name);
	}

	@Override
	public List<Map<String, Object>> getPartnerByDayCustom(String partnerUsername, String from, String to) {
		int partnerId = getPartneridByName(partnerUsername);
		
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
	public List<Map<String, Object>> getPartnerByDayPrvMonth(String partnerUsername) {
		String strFrom = getStartOfMonthStr(-1);
		String strTo = getEndOfMonthStr(-1);
		
		return getPartnerByDayCustom(partnerUsername, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getPartnerByDayCurMonth(
			String partnerUsername) {
		String strFrom = getStartOfMonthStr(0);
		String strTo = getEndOfMonthStr(0);
		
		return getPartnerByDayCustom(partnerUsername, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getPartnerByClient(String partnerUsername,
			String date) {
		int partnerId = getPartneridByName(partnerUsername);
		
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

	@Override
	public boolean hasUserRights(String username, String queuename) {
		long clientid = getClientidByUsername(username);
		if (clientid == 0) {
			return false;
		}
		
		String sql = "select count(1) from queue a where a.clientid = ? and a.name = ?";
		long cnt = jdbcTemplate.queryForLong(sql, clientid, queuename);
		
		return cnt != 0;
	}
}
