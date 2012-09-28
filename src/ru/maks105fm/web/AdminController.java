package ru.maks105fm.web;

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
}
