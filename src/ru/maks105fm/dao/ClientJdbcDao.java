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

		return jdbcTemplate.queryForList("select a.name, a.description, a.innertypeid " +
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
				+ " row_number() over(order by a.eventdate_msk DESC) rownum " +
				"FROM cdr_queue_view a"
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
	public int getCustomQueueCalls(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) " +
				"FROM cdr_queue_view a " +
				"WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthQueueCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomQueueCalls(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthQueueCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomQueueCalls(queueName, strFrom, strTo);
	}

	@Override
	public int getCustomReceivedQueueCalls(String queueName, String from,
			String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) " +
				"FROM cdr_queue_view a WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getCustomQueueAverageWaitTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT round(avg(a.waittime)) " +
				"FROM cdr_queue_view a " +
				"WHERE a.queuename = ? AND " +
				"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public long getCustomQueueSumCallTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT sum(((calltime / 60) + 1)*call) " +
				"FROM cdr_queue_view a " +
				"WHERE a.queuename = ? AND " +
				"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthReceivedQueueCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomReceivedQueueCalls(queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthQueueAverageWaitTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomQueueAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getPrvMonthQueueSumCallTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomQueueSumCallTime(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthReceivedQueueCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomReceivedQueueCalls(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthQueueAverageWaitTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomQueueAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getCurMonthQueueSumCallTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomQueueSumCallTime(queueName, strFrom, strTo);
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

	@Override
	public int getCustomOutCalls(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) " +
				"FROM cdr_out_view a " +
				"WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getOutLogCustom(String queueName, String from, String to, int pagesize,
			Integer page) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		// calculate pagination
		int offset = (page - 1) * pagesize;
		int limit = pagesize;

		String sql = "SELECT to_char(eventdate_msk, 'DD.MM.YY HH24:MI') eventdate, uniqueid, queuename, "
				+ " agent, event, waittime, (((calltime / 60) + 1))*call calltime, call, callerid, "
				+ " row_number() over(order by a.eventdate_msk DESC) rownum " +
				"FROM cdr_out_view a"
				+ " WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')"
				+ " ORDER BY a.eventdate_msk DESC LIMIT ? OFFSET ?";

		return jdbcTemplate.queryForList(sql, queueName, strFrom, strTo, limit,
				offset);
	}

	@Override
	public int getPrvMonthOutCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomOutCalls(queueName, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getOutLogPrvMonth(String queueName, int pagesize, Integer page) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getOutLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public int getCurMonthOutCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomOutCalls(queueName, strFrom, strTo);
	}

	@Override
	public List<Map<String, Object>> getOutLogCurMonth(String queueName, int pagesize, Integer page) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getOutLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public int getCustomReceivedOutCalls(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) " +
				"FROM cdr_out_view a " +
				"WHERE a.queuename = ? AND"
				+ " a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getCustomOutAverageWaitTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT round(avg(a.waittime)) " +
				"FROM cdr_out_view a " +
				"WHERE a.queuename = ? AND " +
				"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public long getCustomOutSumCallTime(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT sum(((calltime / 60) + 1)*call) " +
				"FROM cdr_out_view a " +
				"WHERE a.queuename = ? AND " +
				"a.eventdate_msk >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.eventdate_msk <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND " +
				"a.call = 1";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthReceivedOutCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomReceivedOutCalls(queueName, strFrom, strTo);
	}

	@Override
	public int getPrvMonthOutAverageWaitTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomOutAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getPrvMonthOutSumCallTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(-1);
		String strTo = DateUtils.getEndOfMonthStr(-1);

		return getCustomOutSumCallTime(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthReceivedOutCalls(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomReceivedOutCalls(queueName, strFrom, strTo);
	}

	@Override
	public int getCurMonthOutAverageWaitTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomOutAverageWaitTime(queueName, strFrom, strTo);
	}

	@Override
	public long getCurMonthOutSumCallTime(String queueName) {
		String strFrom = DateUtils.getStartOfMonthStr(0);
		String strTo = DateUtils.getEndOfMonthStr(0);

		return getCustomOutSumCallTime(queueName, strFrom, strTo);
	}
}
