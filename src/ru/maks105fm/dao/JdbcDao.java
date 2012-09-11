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

	@Override
	public List<Map<String, Object>> getQueues(String username) {
		long clientid = jdbcTemplate.queryForLong(
				"select a.clientid from \"user\" a where a.username = ?", username);
		if (clientid == 0) {
			return null;
		}

		return jdbcTemplate.queryForList("select a.name, a.description"
				+ " from queue a where a.clientid = ?" + " order by a.name", clientid);
	}

	@Override
	public List<Map<String, Object>> getQueueLogCustom(String queueName, String from, String to,
			int pagesize, int page) {

		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		// calculate pagination
		int offset = (page - 1) * pagesize;
		int limit = pagesize;

		String sql = "SELECT eventdate, uniqueid, queuename, agent, event, waittime, calltime, call FROM cdr_queue_view a"
				+ " WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')"
				+ " ORDER BY a.eventdate DESC LIMIT ? OFFSET ?";

		return jdbcTemplate.queryForList(sql, queueName, strFrom, strTo, limit, offset);
	}

	@Override
	public List<Map<String, Object>> getQueueLogPrvMonth(String queueName, int pagesize, int page) {
		Calendar c;

		c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		Date from = c.getTime();

		c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		Date to = c.getTime();

		// DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		// String strFrom = df.format(from);
		// String strTo = df.format(to);

		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String strFrom = df.format(from);
		String strTo = df.format(to);

		return getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public List<Map<String, Object>> getQueueLogCurMonth(String queueName, int pagesize, int page) {
		Calendar c;

		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		Date from = c.getTime();

		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		Date to = c.getTime();

		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String strFrom = df.format(from);
		String strTo = df.format(to);

		return getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	}

	@Override
	public int getQueueLogCustomCallsCount(String queueName, String from, String to) {
		String strFrom = from + " 00:00:00";
		String strTo = to + " 23:59:59";

		String sql = "SELECT count(1) FROM cdr_queue_view a" + " WHERE a.queuename = ? AND"
				+ " a.eventdate >= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS') AND"
				+ " a.eventdate <= to_timestamp(?, 'dd.mm.yyyy HH24:MI:SS')";

		return jdbcTemplate.queryForInt(sql, queueName, strFrom, strTo);
	}

	@Override
	public int getQueueLogPrvMonthCallsCount(String queueName) {
		Calendar c;

		c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		Date from = c.getTime();

		c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		Date to = c.getTime();

		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String strFrom = df.format(from);
		String strTo = df.format(to);

		return getQueueLogCustomCallsCount(queueName, strFrom, strTo);
	}

	@Override
	public int getQueueLogCurMonthCallsCount(String queueName) {
		Calendar c;

		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		Date from = c.getTime();

		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, c.getActualMaximum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMaximum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMaximum(Calendar.SECOND));
		Date to = c.getTime();

		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		String strFrom = df.format(from);
		String strTo = df.format(to);

		return getQueueLogCustomCallsCount(queueName, strFrom, strTo);
	}
}
