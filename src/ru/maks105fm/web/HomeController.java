package ru.maks105fm.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.maks105fm.dao.Dao;

@Controller
public class HomeController {

	@Autowired
	Dao dao;

	@RequestMapping(value = "/")
	public String home(Model model) {
		initDefaultData(model);

		return "home";
	}

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String request(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom,
			@RequestParam("to") String strTo, Model model) {
		
		initDefaultData(model);
		model.addAttribute("queue", queueName);
		model.addAttribute("from", strFrom);
		model.addAttribute("to", strTo);

		// TODO: check access to queue

		// DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
		// Date dateFrom = df.parse(strFrom);
		// Date dateTo = df.parse(strTo);

		List<Map<String, Object>> queueLog = dao.getQueueLog(queueName,
				strFrom, strTo);
		model.addAttribute("queueLog", queueLog);

		return "home";
	}

	private void initDefaultData(Model model) {
		model.addAttribute("username", "user1");

		List<Map<String, Object>> queues = dao.getQueues("user1");
		model.addAttribute("queues", queues);
	}

}
