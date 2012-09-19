package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface Dao {
	List<Map<String, Object>> getQueues(String username);

	List<Map<String, Object>> getQueueLogCustom(String queuename, String from, String to, int pagesize, int page);

	List<Map<String, Object>> getQueueLogPrvMonth(String queueName, int pagesize, int page);

	List<Map<String, Object>> getQueueLogCurMonth(String queueName, int pagesize, int page);

	int getCustomCallsCount(String queueName, String strFrom, String strTo);

	int getPrvMonthCallsCount(String queueName);

	int getCurMonthCallsCount(String queueName);

	int getCustomReceivedCallsCount(String queueName, String strFrom, String strTo);

	int getCustomAverageWaitTime(String queueName, String strFrom, String strTo);

	long getCustomSumCallTime(String queueName, String strFrom, String strTo);

	int getPrvMonthReceivedCallsCount(String queueName);

	int getPrvMonthAverageWaitTime(String queueName);

	long getPrvMonthSumCallTime(String queueName);

	int getCurMonthReceivedCallsCount(String queueName);

	int getCurMonthAverageWaitTime(String queueName);

	long getCurMonthSumCallTime(String queueName);
	
	Map<String, Object> getUserByUsername(String username);
	
	Map<String, Object> getPartnerByUsername(String username);

	List<Map<String, Object>> getPartnerByDayCustom(String partnerUsername,
			String strFrom, String strTo);

	List<Map<String, Object>> getPartnerByDayPrvMonth(String partnerUsername);

	List<Map<String, Object>> getPartnerByDayCurMonth(String partnerUsername);

	List<Map<String, Object>> getPartnerByClient(String partnerUsername, String strDate);
}
