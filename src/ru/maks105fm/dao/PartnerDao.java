package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface PartnerDao {
	List<Map<String, Object>> getDaylyCustom(long userId, String strFrom, String strTo);

	List<Map<String, Object>> getDaylyPrvMonth(long userId);

	List<Map<String, Object>> getDaylyCurMonth(long userId);

	List<Map<String, Object>> getForDateDetailed(long userId, String strDate);

	long getMoneyFullSumCustom(long userId, String strFrom, String strTo);

	long getMoneyFullSumPrvMonth(long userId);

	long getMoneyFullSumCurMonth(long userId);

	void setAgree(long userId);
	
	boolean getAgree(long userId);
}
