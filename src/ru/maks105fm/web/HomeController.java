package ru.maks105fm.web;

import java.util.ArrayList;
import java.util.HashMap;
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

	public static String PERIOD_CURMONTH = "curmonth";
	public static String PERIOD_PRVMONTH = "prvmonth";
	public static String PERIOD_CUSTOM = "custom";

	@Autowired
	Dao dao;

	@RequestMapping(value = "/")
	public String home(Model model) {
		initDefaultData(model);

		return "detailed";
	}

	@RequestMapping(value = "/detailedLog", method = RequestMethod.GET)
	public String detailedLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, @RequestParam("page") Integer page, Model model) {

		initDefaultData(model);
		model.addAttribute("queue", queueName);
		model.addAttribute("from", strFrom);
		model.addAttribute("to", strTo);
		model.addAttribute("period", period);

		int pagesize = 10;

		if (page == null) {
			page = 1;
		}

		// TODO: check access to queue

		if (period != null && !period.isEmpty()) {
			List<Map<String, Object>> calls = null;
			int callsCount = 0;

			if (period.equals(PERIOD_CUSTOM)) {
				callsCount = dao.getQueueLogCustomCallsCount(queueName, strFrom, strTo);
				calls = dao.getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);

			} else if (period.equals(PERIOD_PRVMONTH)) {
				callsCount = dao.getQueueLogPrvMonthCallsCount(queueName);
				calls = dao.getQueueLogPrvMonth(queueName, pagesize, page);

			} else if (period.equals(PERIOD_CURMONTH)) {
				callsCount = dao.getQueueLogCurMonthCallsCount(queueName);
				calls = dao.getQueueLogCurMonth(queueName, pagesize, page);

			} else {
				// error
			}

			int pagesCount = callsCount / pagesize;
			if (callsCount % pagesize != 0) {
				pagesCount++;
			}

			model.addAttribute("calls", calls);
			model.addAttribute("callsCount", callsCount);
			model.addAttribute("curPage", page);
			model.addAttribute("pagesCount", pagesCount);
		}

		return "detailed";
	}

	@RequestMapping(value = "/simpleLog", method = RequestMethod.GET)
	public String simpleLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, Model model) {

		return "home";
	}

	private void initDefaultData(Model model) {
		model.addAttribute("username", "user1");

		List<Map<String, Object>> queues = dao.getQueues("user1");
		model.addAttribute("queues", queues);

		// periods
		List<Map<String, Object>> periods = new ArrayList<Map<String, Object>>();
		model.addAttribute("periods", periods);
		Map<String, Object> pi = new HashMap<String, Object>();
		periods.add(pi);
		pi.put("name", PERIOD_PRVMONTH);
		pi.put("description", "За предыдущий месяц");

		pi = new HashMap<String, Object>();
		periods.add(pi);
		pi.put("name", PERIOD_CURMONTH);
		pi.put("description", "За текущий месяц");

		pi = new HashMap<String, Object>();
		periods.add(pi);
		pi.put("name", PERIOD_CUSTOM);
		pi.put("description", "За произвольный период");

		
	}

}
