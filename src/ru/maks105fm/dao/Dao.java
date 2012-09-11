package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface Dao {
	List<Map<String, Object>> getQueues(String username);

	List<Map<String, Object>> getQueueLogCustom(String queuename, String from, String to, int pagesize, int page);

	List<Map<String, Object>> getQueueLogPrvMonth(String queueName, int pagesize, int page);

	List<Map<String, Object>> getQueueLogCurMonth(String queueName, int pagesize, int page);

	int getQueueLogCustomCallsCount(String queueName, String strFrom, String strTo);

	int getQueueLogPrvMonthCallsCount(String queueName);

	int getQueueLogCurMonthCallsCount(String queueName);
}
