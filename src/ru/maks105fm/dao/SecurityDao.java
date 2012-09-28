package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface SecurityDao {
	Map<String, Object> getUser(String username);
	
	List<Map<String, Object>> getUserRoles(long userId);
	
	String getNormalname(long userId, String usertype);

	boolean isUserOwnerDeleted(long userId, String usertype);
}
