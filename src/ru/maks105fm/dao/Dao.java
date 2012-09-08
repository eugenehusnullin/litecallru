package ru.maks105fm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Dao {
	List<Map<String, Object>> getQueues(String username);
	
	List<Map<String, Object>> getQueueLog(String queuename, Date from, Date to);
}
