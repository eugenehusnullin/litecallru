package ru.maks105fm.dao;

import java.util.List;
import java.util.Map;

public interface AdminDao {
	List<Map<String, Object>> getClients(int pagesize, int page, boolean sortOrder);

	int getClientsCount();

	void deleteClient(Integer clientId);

	void addClient(String name, String email, Integer partnerId);
}
