package ru.maks105fm.web;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("#{mainProps['main.clientpasswordlength']}")
	private String clientPasswordLength;
	
	@Value("#{mainProps['main.partnerpasswordlength']}")
	private String partnerPasswordLength;
	
	@Value("#{mainProps['main.adminemails']}")
	private String commaseparatedEmails;
	
	private String[] emails;
	
	@Value("#{mainProps['main.smtphost']}")
	private String smtphost;
	
	@Value("#{mainProps['main.smtpport']}")
    private String smtpport;
	
	@Value("#{mainProps['main.fromusername']}")
    private String fromusername;
	
	@Value("#{mainProps['main.fromuserpassword']}")
    private String fromuserpassword;
	
	public void init() {
		emails = commaseparatedEmails.split(";");
	}
	
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
		

		if (assPartnerid != null && !adminDao.existsPartner(assPartnerid)) {
			model.addAttribute("message", "Партнера с таким ID нет.");
		} else {
			int clientId = adminDao.addClient(name, email, assPartnerid);
			addUser2Client(clientId);
			
			model.addAttribute("message", "Успешно добавлен клиент.");
		}
		
		return listClients(partnerId, page, sortOrder, model);
	}
	
	private void addUser2Client(int clientId) {
		String password = generatePassword(Integer.parseInt(clientPasswordLength));
		String md5Password = DigestUtils.md5Hex(password);
		adminDao.addClientUser(clientId, md5Password);
		
		createClientEmail(clientId, password);
	}
	
	private String generatePassword(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}
	
	private void createClientEmail(int clientId, String password) {
		String clientName = adminDao.getClientName(clientId);
		String emailAddress = adminDao.getClientEmail(clientId);
		
		VelocityContext context = new VelocityContext();
		context.put("ID", clientId);
		context.put("password", password);
		context.put("name", clientName);
		
		Properties vp = new Properties();
		vp.put("resource.loader", "class");
		vp.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine ve = new VelocityEngine(vp);
		
		Template template = null;
		template = ve.getTemplate("toclient.vm", "UTF-8");
		
		StringWriter sw = new StringWriter();
		template.merge(context, sw);
		String emailbody2client = sw.toString();
		
		sendEmail(emailAddress, emailbody2client, "Регистрация клиента в LiteCall");
		
		// email to admins
		context.put("type", "клиент,");
		template = ve.getTemplate("toadmin.vm", "UTF-8");
		sw = new StringWriter();
		template.merge(context, sw);
		String emailbody2admin = sw.toString();
		
		for (int i = 0; i < emails.length; i++) {
			sendEmail(emails[i], emailbody2admin, "Регистрация клиента в LiteCall");
		}
	}
	
	private void sendEmail(String recipient, String body, String subject) {
	    Properties props = System.getProperties();
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", smtphost);
	    props.put("mail.smtp.user", fromusername);
	    props.put("mail.smtp.password", fromuserpassword);
	    props.put("mail.smtp.port", smtpport);
	    props.put("mail.smtp.auth", "true");

	    String[] to = {recipient};

	    Session session = Session.getDefaultInstance(props, null);
	    MimeMessage message = new MimeMessage(session);
	    try {
			message.setFrom(new InternetAddress(fromusername));
			InternetAddress[] toAddress = new InternetAddress[to.length];

		    // To get the array of addresses
		    for( int i=0; i < to.length; i++ ) { // changed from a while loop
		        toAddress[i] = new InternetAddress(to[i]);
		    }
		    System.out.println(Message.RecipientType.TO);

		    for( int i=0; i < toAddress.length; i++) { // changed from a while loop
		        message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		    }
		    message.setSubject(subject);
		    message.setText(body);
		    
		    Transport transport = session.getTransport("smtp");
		    transport.connect(smtphost, fromusername, fromuserpassword);
		    transport.sendMessage(message, message.getAllRecipients());
		    transport.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/rePartnerClients", method = RequestMethod.POST)
	public String rePartner(HttpServletRequest request, Model model) {
		Integer partnerId = request.getParameter("partnerid")== null ? null : Integer.parseInt(request.getParameter("partnerid"));
		Integer rePartnerId = request.getParameter("rePartnerid")== null ? null : Integer.parseInt(request.getParameter("rePartnerid"));
		String paramIds = request.getParameter("ids");
		String[] arrayIds = paramIds.split(";");
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
		
		if (rePartnerId == null || !adminDao.existsPartner(rePartnerId)) {
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
		int partnerId = adminDao.addPartner(name, email);
		addUser2Partner(partnerId);
		
		return listPartners(1, sortType, sortOrder, model);
	}
	
	private void addUser2Partner(int partnerId) {
		String password = generatePassword(Integer.parseInt(partnerPasswordLength));
		String md5Password = new String(DigestUtils.md5(password));
		adminDao.addPartnerUser(partnerId, md5Password);
		
		createPartnerEmail(partnerId, password);
	}
	
	private void createPartnerEmail(int partnerId, String password) {
		String partnerName = adminDao.getPartnerName(partnerId);
		String emailAddress = adminDao.getPartnerEmail(partnerId);
		
		VelocityContext context = new VelocityContext();
		context.put("ID", partnerId);
		context.put("password", password);
		context.put("name", partnerName);
		
		Properties vp = new Properties();
		vp.put("resource.loader", "class");
		vp.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		VelocityEngine ve = new VelocityEngine(vp);
		
		Template template = null;
		template = ve.getTemplate("topartner.vm", "UTF-8");
		
		StringWriter sw = new StringWriter();
		template.merge(context, sw);
		String emailbody2partner = sw.toString();
		
		sendEmail(emailAddress, emailbody2partner, "Регистрация партнера в LiteCall");
		
		// email to admins
		context.put("type", "партнер,");
		template = ve.getTemplate("toadmin.vm", "UTF-8");
		sw = new StringWriter();
		template.merge(context, sw);
		String emailbody2admin = sw.toString();
		
		for (int i = 0; i < emails.length; i++) {
			sendEmail(emails[i], emailbody2admin, "Регистрация партнера в LiteCall");
		}
	}
}
