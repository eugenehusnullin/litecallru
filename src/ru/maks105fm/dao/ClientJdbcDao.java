package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

import ru.maks105fm.utils.DateUtils;

public class ClientJdbcDao extends JdbcDao implements ClientDao {
		
	private long getClientId(long userId) {
		return jdbcTemplate.queryForLong(
				"select a.clientid from \"clientuser\" a where a.userid = ?",
				userId);
	}
	
	@Override
	public List<Map<String, Object>> getQueues(long userId) {
		long clientid = getClientId(userId);
		if (clientid == 0) {
			return null;
		}

		return jdbcTemplate.queryForList("select a.name, a.description " +
				"from queue a " +
				"where a.clientid = ? " +
				"and a.deleted = 0" +
				"order by a.name",
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

		String sql = "SELECT to_char(eventdate_msk, 'DD.MM.YY HH24:MI') eventdate, uniqueid, queuename, "
				+ " agent, event, waittime, (((calltime / 60) + 1))*call calltime, call, callerid, "
				+ " row_number() over(order by a.eventdate_msk DESC) rownum FROM cdr_queue_view a"
				+ " WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')"
				+ " ORDER BY a.eventdate_msk DESC LIMIT ? OFFSET ?";

		return jdbcTemplate.queryForList(sql, queueName, strFrom, strTo, limit,
				offset);
	}
	
	@Override
	public List<Map<String, Object>> getQueueLogPrvMonth(String queueName,
			int pagesize, int page) {
		// DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		// String strFrom = df.format(from);
		// String strTo = df.format(to);

		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public List<Map<String, Object>> getQueueLogCurMonth(String queueName,
			int pagesize, int page) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public int getCustomAllCallsCount(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthAllCallsCount(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomAllCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthAllCallsCount(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomAllCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getCustomReceivedCallsCount(String queueName, String from,
			String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getCustomAverageWaitTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT round(avg(a.waittime)) FROM cdr_queue_view a " +
				"WHERE a.queuename = ? AND " +
				"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public long getCustomSumCallTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT sum(((calltime / 60) + 1)) FROM cdr_queue_view a WHERE a.queuename = ? AND " +
				"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthReceivedCallsCount(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomReceivedCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthAverageWaitTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getPrvMonthSumCallTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomSumCallTime(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthReceivedCallsCount(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomReceivedCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthAverageWaitTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getCurMonthSumCallTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomSumCallTime(queueName, strFrom, strTo);
	}
	
	@Override
	public boolean hasUserRights(long userId, String queuename) {
		long clientid = getClientId(userId);
		if (clientid == 0) {
			return false;
		}
		
		String sql = "select count(1) from queue a where a.clientid = ? and a.name = ?";
		long cnt = jdbcTemplate.queryForLong(sql, clientid, queuename);
		
		return cnt != 0;
	}
}
