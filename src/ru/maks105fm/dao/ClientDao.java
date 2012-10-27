package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface ClientDao {
	List<Map<String, Object>> getQueues(long userId);

	List<Map<String, Object>> getQueueLogCustom(String queuename, String from, String to, int pagesize, int page);

	List<Map<String, Object>> getQueueLogPrvMonth(String queueName, int pagesize, int page);

	List<Map<String, Object>> getQueueLogCurMonth(String queueName, int pagesize, int page);

	int getCustomQueueCalls(String queueName, String strFrom, String strTo);

	int getPrvMonthQueueCalls(String queueName);

	int getCurMonthQueueCalls(String queueName);

	int getCustomReceivedQueueCalls(String queueName, String strFrom, String strTo);

	int getCustomQueueAverageWaitTime(String queueName, String strFrom, String strTo);

	long getCustomQueueSumCallTime(String queueName, String strFrom, String strTo);

	int getPrvMonthReceivedQueueCalls(String queueName);

	int getPrvMonthQueueAverageWaitTime(String queueName);

	long getPrvMonthQueueSumCallTime(String queueName);

	int getCurMonthReceivedQueueCalls(String queueName);

	int getCurMonthQueueAverageWaitTime(String queueName);

	long getCurMonthQueueSumCallTime(String queueName);
	
	boolean hasUserRights(long userId, String queuename);

	int getCustomOutCalls(String queueName, String from, String to);

	List<Map<String, Object>> getOutLogCustom(String queueName, String from, String to, int pagesize, Integer page);

	int getPrvMonthOutCalls(String queueName);

	List<Map<String, Object>> getOutLogPrvMonth(String queueName, int pagesize, Integer page);

	int getCurMonthOutCalls(String queueName);

	List<Map<String, Object>> getOutLogCurMonth(String queueName, int pagesize, Integer page);

	int getCustomReceivedOutCalls(String queueName, String from, String to);

	int getCustomOutAverageWaitTime(String queueName, String from, String to);

	long getCustomOutSumCallTime(String queueName, String from, String to);

	int getPrvMonthReceivedOutCalls(String queueName);

	int getPrvMonthOutAverageWaitTime(String queueName);

	long getPrvMonthOutSumCallTime(String queueName);

	int getCurMonthReceivedOutCalls(String queueName);

	int getCurMonthOutAverageWaitTime(String queueName);

	long getCurMonthOutSumCallTime(String queueName);
}
