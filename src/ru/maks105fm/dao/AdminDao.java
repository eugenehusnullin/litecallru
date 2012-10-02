package ru.maks105fm.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AdminDao {
	List<Map<String, Object>> getClients(int pagesize, int page, boolean sortOrder);

	int getClientsCount();

	void deleteClient(Integer clientId);

	void addClient(String name, String email, Integer partnerId);

	void rePartner(int partnerId, int clientId);

	List<Map<String, Object>> getPhonesCurMonth(int clientId, int pagesize, int page);

	int getPhonesCount(int clientId);

	void addPhone(int clientId, String description, String typedescr, BigDecimal tariff);

	void deletePhone(int phoneId);
}
