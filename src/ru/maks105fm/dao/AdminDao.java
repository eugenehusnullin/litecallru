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

	List<Map<String, Object>> getPartners(int pagesize, int page, int sortType, boolean sortOrder);

	int getPartnersCount();

	void deletePartner(int partnerId);

	void addPartner(String name, String email);

	List<Map<String, Object>> getPartnerClients(int partnerId, int pagesize, int page, boolean sortOrder);

	int getPartnerClientsCount(int partnerId);

	boolean existsPartner(int partnerId);

	List<Map<String, Object>> getPartner(int id);

	List<Map<String, Object>> getClient(int id);

	String getClientName(int clientId);

	String getPartnerName(int partnerId);
}
