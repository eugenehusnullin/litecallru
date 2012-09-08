package ru.maks105fm.dao;

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
		
		return jdbcTemplate.queryForList("select a.name, a.description from queue a where a.clientid = ?",
				clientid);

//		return jdbcTemplate.queryForList(
//				"select a.description from clientqueue a where a.clientid = ?",
//				String.class, clientid);
	}

	@Override
	public List<Map<String, Object>> getQueueLog(String queuename, Date from,
			Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
