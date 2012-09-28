package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface PartnerDao {
	List<Map<String, Object>> getDaylyCustom(long userId,
			String strFrom, String strTo);

	List<Map<String, Object>> getDaylyPrvMonth(long userId);

	List<Map<String, Object>> getDaylyCurMonth(long userId);

	List<Map<String, Object>> getForDateDetailed(long userId, String strDate);
}
