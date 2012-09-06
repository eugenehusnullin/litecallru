package ru.maks105fm.dao;

import java.util.List;

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
	public List<String> getQueues(String username) {
		long clientid = jdbcTemplate.queryForLong(
				"select a.id from client a where a.username = ?", username);
		if (clientid == 0) {
			return null;
		}

		return jdbcTemplate.queryForList(
				"select a.name from clientqueue a where a.clientid = ?",
				String.class, clientid);
	}

}
