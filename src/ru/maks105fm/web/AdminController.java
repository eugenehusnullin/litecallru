package ru.maks105fm.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.maks105fm.dao.AdminDao;
import ru.maks105fm.web.security.UserWithName;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private static int PAGESIZE_CLIENTS = 5;
	private static int PAGESIZE_PHONES = 5;
	private static int PAGESIZE_PARTNERS = 5;

	@Autowired
	private AdminDao adminDao;
	
	private void initDefaultData(Model model) {
		UserWithName user = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username", user.getUsername());
		model.addAttribute("humanname", user.getHumanname());
	}
	
	@RequestMapping(value = "/")
	public String home(Model model) {
		return defaultListPartners(model);
	}
	
	@RequestMapping(value = "/defaultClients")
	public String defaultListClients(Model model) {
		return listClients(null, 1, -1, model);
	}
	
	@RequestMapping(value = "/clients")
	public String listClients(@RequestParam("partnerId") Integer partnerId,
			@RequestParam("page") Integer page,			
			@RequestParam("sortOrder") Integer sortOrder, Model model) {
		
		initDefaultData(model);
		model.addAttribute("partnerId", partnerId);
		
		if (page == null) {
			page = 1;
		}
		
		Object clients = null;
		int clientsCount = 0;
		
		if (partnerId == null) {
			clients = adminDao.getClients(PAGESIZE_CLIENTS, page, sortOrder > 0);
			clientsCount = adminDao.getClientsCount();
		} else {
			clients = adminDao.getPartnerClients(partnerId, PAGESIZE_CLIENTS, page, sortOrder > 0);		
			clientsCount = adminDao.getPartnerClientsCount(partnerId);
			
			String partnerName = adminDao.getPartnerName(partnerId);
			model.addAttribute("partnerName", partnerName);
		}
		
		int pagesCount = clientsCount / PAGESIZE_CLIENTS;
		if (clientsCount % PAGESIZE_CLIENTS != 0) {
			pagesCount++;
		}
		
		model.addAttribute("clients", clients);
		model.addAttribute("clientsCount", clientsCount);
		model.addAttribute("curPage", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("sortOrder", sortOrder);

		return "clients";
	}
	
	@RequestMapping(value = "/searchClient")
	public String searchClients(@RequestParam("id") Integer id,			
			@RequestParam("sortOrder") Integer sortOrder, Model model) {
		
		initDefaultData(model);
		
		List<Map<String, Object>> clients = adminDao.getClient(id);
		int clientsCount = clients.size();
		int page = clients.size();
		int pagesCount = clients.size();
		
		
		model.addAttribute("clients", clients);
		model.addAttribute("clientsCount", clientsCount);
		model.addAttribute("curPage", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("sortOrder", sortOrder);

		return "clients";
	}
	
	@RequestMapping(value = "/deleteClient")
	public String deleteClient(@RequestParam("partnerId") Integer partnerId,
			@RequestParam("id") Integer clientId, Model model) {
		adminDao.deleteClient(clientId);
		return listClients(partnerId, 1, 1, model);
	}
	
	@RequestMapping(value = "/changeSortClients", method = RequestMethod.GET)
	public String changeSortClients(@RequestParam("partnerId") Integer partnerId,
			@RequestParam("selectedSort") Integer selectedSort, Model model) {		
		return listClients(partnerId, 1, selectedSort, model);
	}
	
	@RequestMapping(value = "/addClient", method = RequestMethod.POST)
	public String addClient(@RequestParam("partnerId") Integer partnerId,
			@RequestParam("name") String name, @RequestParam("email") String email, 
			@RequestParam("assPartnerid") Integer assPartnerid,
			@RequestParam("page") Integer page,			
			@RequestParam("sortOrder") Integer sortOrder, Model model) {
		
		if (!adminDao.existsPartner(assPartnerid)) {
			model.addAttribute("message", "Партнера с таким ID нет.");
		} else {
			adminDao.addClient(name, email, assPartnerid);
			model.addAttribute("message", "Успешно добавлен клиент.");
		}
		
		return listClients(partnerId, page, sortOrder, model);
	}
	
	@RequestMapping(value = "/rePartnerClients", method = RequestMethod.POST)
	public String rePartner(HttpServletRequest request, Model model) {
		Integer partnerId = request.getParameter("partnerid")== null ? null : Integer.parseInt(request.getParameter("partnerid"));
		int rePartnerId = Integer.parseInt(request.getParameter("rePartnerid"));
		String paramIds = request.getParameter("ids");
		String[] arrayIds = paramIds.split(";");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
		
		if (!adminDao.existsPartner(rePartnerId)) {
			model.addAttribute("message", "Партнера с таким ID нет.");
		} else {
			for (int i = 0; i < arrayIds.length; i++) {
				if (arrayIds[i] != null && arrayIds[i].length() > 0) {
					int clientId = Integer.parseInt(arrayIds[i]);
					adminDao.rePartner(rePartnerId, clientId);
				}
			}
			model.addAttribute("message", "Успешно изменен партнер.");
		}
		
		return listClients(partnerId, page, sortOrder, model);
	}
	
	@RequestMapping(value = "/phones" )
	public String listPhones(@RequestParam("clientId") Integer clientId, @RequestParam("page") Integer page, Model model) {
		initDefaultData(model);
		model.addAttribute("clientId", clientId);
		
		if (page == null) {
			page = 1;
		}
		
		Object phones = adminDao.getPhonesCurMonth(clientId, PAGESIZE_PHONES, page);
		int phonesCount = adminDao.getPhonesCount(clientId);
		
		int pagesCount = phonesCount / PAGESIZE_PHONES;
		if (phonesCount % PAGESIZE_PHONES != 0) {
			pagesCount++;
		}
		
		model.addAttribute("phones", phones);
		model.addAttribute("phonesCount", phonesCount);
		model.addAttribute("curPage", page);
		model.addAttribute("pagesCount", pagesCount);
		
		String clientName = adminDao.getClientName(clientId);
		model.addAttribute("clientName", clientName);

		return "phones";
	}
	
	@RequestMapping(value = "/addPhone", method = RequestMethod.POST)
	public String addPhone(@RequestParam("clientId") Integer clientId, 
			@RequestParam("description") String description, 
			@RequestParam("typedescr") String typedescr, 
			@RequestParam("tariff") String strTariff,
			@RequestParam("page") Integer page, Model model) {
		
		BigDecimal tariff = new BigDecimal(strTariff);
		adminDao.addPhone(clientId, description, typedescr, tariff);
		return listPhones(clientId, 1, model);
	}
	
	@RequestMapping(value = "/deletePhone")
	public String deletePhone(@RequestParam("id") Integer phoneId, @RequestParam("clientId") Integer clientId, Model model) {
		adminDao.deletePhone(phoneId);
		return listPhones(clientId, 1, model);
	}
	
	@RequestMapping(value = "/deletePhones", method = RequestMethod.POST)
	public String deletePhones(HttpServletRequest request, Model model) {
		int clientId = Integer.parseInt(request.getParameter("clientId"));		
		String paramIds = request.getParameter("ids");
		String[] arrayIds = paramIds.split(";");
		
		for (int i = 0; i < arrayIds.length; i++) {
			if (arrayIds[i] != null && arrayIds[i].length() > 0) {
				int phoneId = Integer.parseInt(arrayIds[i]);
				adminDao.deletePhone(phoneId);
			}
		}
		
		return listPhones(clientId, 1, model);
	}
	
	@RequestMapping(value = "/defaultPartners")
	public String defaultListPartners(Model model) {
		return listPartners(1, 1, -1, model);
	}
	
	@RequestMapping(value = "/partners")
	public String listPartners(@RequestParam("page") Integer page, 
			@RequestParam("sortType") Integer sortType, 
			@RequestParam("sortOrder") Integer sortOrder, Model model) {
		initDefaultData(model);
		
		if (page == null) {
			page = 1;
		}
		
		Object partners = adminDao.getPartners(PAGESIZE_PARTNERS, page, sortType, sortOrder > 0);
		int partnersCount = adminDao.getPartnersCount();
		
		int pagesCount = partnersCount / PAGESIZE_PARTNERS;
		if (partnersCount % PAGESIZE_PARTNERS != 0) {
			pagesCount++;
		}
		
		model.addAttribute("partners", partners);
		model.addAttribute("partnersCount", partnersCount);
		model.addAttribute("curPage", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortOrder", sortOrder);

		return "partners";
	}
	
	@RequestMapping(value = "/searchPartner")
	public String searchPartner(@RequestParam("id") Integer id,
			@RequestParam("sortType") Integer sortType, 
			@RequestParam("sortOrder") Integer sortOrder, Model model) {
		
		initDefaultData(model);
		
		List<Map<String, Object>> partners = adminDao.getPartner(id);
		int partnersCount = partners.size();		
		int page = partners.size();
		int pagesCount = partners.size();
		
		model.addAttribute("partners", partners);
		model.addAttribute("partnersCount", partnersCount);
		model.addAttribute("curPage", page);
		model.addAttribute("pagesCount", pagesCount);
		model.addAttribute("sortType", sortType);
		model.addAttribute("sortOrder", sortOrder);

		return "partners";
	}
	
	@RequestMapping(value = "/changeSortPartners", method = RequestMethod.GET)
	public String changeSortPartners(@RequestParam("sortType") Integer sortType,
			@RequestParam("sortOrder") Integer sortOrder,
			Model model) {		
		return listPartners(1, sortType, sortOrder, model);
	}
	
	@RequestMapping(value = "/deletePartner")
	public String deletePartner(@RequestParam("id") Integer partnerId,
			@RequestParam("sortType") Integer sortType, 
			@RequestParam("sortOrder") Integer sortOrder,
			Model model) {
		adminDao.deletePartner(partnerId);
		return listPartners(1, sortType, sortOrder, model);
	}
	
	@RequestMapping(value = "/addPartner", method = RequestMethod.POST)
	public String addPartner(@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("sortType") Integer sortType, 
			@RequestParam("sortOrder") Integer sortOrder,
			Model model) {
		adminDao.addPartner(name, email);
		return listPartners(1, sortType, sortOrder, model);
	}	
}
