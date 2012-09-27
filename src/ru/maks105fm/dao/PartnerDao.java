package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface PartnerDao {
	List<Map<String, Object>> getPartnerByDayCustom(long userId,
			String strFrom, String strTo);

	List<Map<String, Object>> getPartnerByDayPrvMonth(long userId);

	List<Map<String, Object>> getPartnerByDayCurMonth(long userId);

	List<Map<String, Object>> getClientsForDate(long userId, String strDate);
}
