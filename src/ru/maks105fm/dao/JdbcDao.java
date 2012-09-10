package ru.maks105fm.dao;

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
		
		return jdbcTemplate.queryForList("select a.name, a.description" +
				" from queue a where a.clientid = ?" +
				" order by a.name",
				clientid);
	}

	@Override
	public List<Map<String, Object>> getQueueLog(String queuename, String from, String to) {
		String sql = "SELECT eventdate, uniqueid, queuename, agent, event, waittime, calltime, call FROM cdr_queue_view a" +
				" WHERE a.queuename = ? AND" +
				" a.eventdate >= to_timestamp(?, 'dd.mm.yyyy') AND" +
				" a.eventdate <= to_timestamp(?, 'dd.mm.yyyy')" +
				" ORDER BY a.eventdate DESC";
		
		return jdbcTemplate.queryForList(sql, queuename, from, to);
	}
}
