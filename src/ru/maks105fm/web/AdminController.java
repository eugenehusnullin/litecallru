package ru.maks105fm.web;

import java.math.BigDecimal;

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

	@Autowired
	private AdminDao adminDao;
	
	private void initDefaultData(Model model) {
		UserWithName user = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username", user.getUsername());
		model.addAttribute("humanname", user.getHumanname());
	}
	
	@RequestMapping(value = "/")
	public String home(Model model) {
		return listClients(1, -1, model);
	}
	
	@RequestMapping(value = "/clients")
	public String listClients(@RequestParam("page") Integer page, @RequestParam("sortOrder") Integer sortOrder, Model model) {
		initDefaultData(model);
		
		if (page == null) {
			page = 1;
		}
		
		Object clients = adminDao.getClients(PAGESIZE_CLIENTS, page, sortOrder > 0);
		
		int clientsCount = adminDao.getClientsCount();
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
	
	@RequestMapping(value = "/deleteClient")
	public String deleteClient(@RequestParam("id") Integer clientId, Model model) {
		adminDao.deleteClient(clientId);
		return listClients(1, 1, model);
	}
	
	@RequestMapping(value = "/changeSortClient", method = RequestMethod.GET)
	public String changeSortClient(@RequestParam("selectedSort") Integer selectedSort, Model model) {		
		return listClients(1, selectedSort, model);
	}
	
	@RequestMapping(value = "/addClient", method = RequestMethod.POST)
	public String addClient(@RequestParam("name") String name, @RequestParam("email") String email, 
			@RequestParam("partnerid") Integer partnerId, Model model) {
		adminDao.addClient(name, email, partnerId);
		return listClients(1, -1, model);
	}
	
	@RequestMapping(value = "/rePartnerClients", method = RequestMethod.POST)
	public String rePartner(HttpServletRequest request, Model model) {		
		int partnerId = Integer.parseInt(request.getParameter("partnerid"));		
		String paramIds = request.getParameter("ids");
		String[] arrayIds = paramIds.split(";");
		
		for (int i = 0; i < arrayIds.length; i++) {
			if (arrayIds[i] != null && arrayIds[i].length() > 0) {
				int clientId = Integer.parseInt(arrayIds[i]);
				adminDao.rePartner(partnerId, clientId);
			}
		}
		
		return listClients(1, -1, model);
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

		return "phones";
	}
	
	@RequestMapping(value = "/addPhone", method = RequestMethod.POST)
	public String addPhone(@RequestParam("clientId") Integer clientId, 
			@RequestParam("description") String description, 
			@RequestParam("typedescr") String typedescr, 
			@RequestParam("tariff") String strTariff, Model model) {
		
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
}
