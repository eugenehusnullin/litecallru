package ru.maks105fm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ru.maks105fm.dao.Dao;
import ru.maks105fm.web.security.UserWithName;

@Controller
public class ClientController {

	public static String PERIOD_CURMONTH = "curmonth";
	public static String PERIOD_PRVMONTH = "prvmonth";
	public static String PERIOD_CUSTOM = "custom";

	@Autowired
	private Dao dao;
	
	private List<Map<String, Object>> periods;
	
	@Value("#{mainProps['main.monitorhost']}")
	private String monitorHost;
	
	public ClientController() {
		periods = new ArrayList<Map<String, Object>>();
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

	@RequestMapping(value = "/")
	public String home(Model model) {
		initDefaultData(model);

		return "common";
	}

	@RequestMapping(value = "/detailedLog", method = RequestMethod.GET)
	public String detailedLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, @RequestParam("page") Integer page, Model model) {
		
		// TODO: check access to queue

		initDefaultData(model);
		model.addAttribute("queue", queueName);
		model.addAttribute("from", strFrom);
		model.addAttribute("to", strTo);
		model.addAttribute("period", period);

		int pagesize = 10;

		if (page == null) {
			page = 1;
		}

		if (period != null && !period.isEmpty()) {
			List<Map<String, Object>> calls = null;
			int callsCount = 0;

			if (period.equals(PERIOD_CUSTOM)) {
				callsCount = dao.getCustomCallsCount(queueName, strFrom, strTo);
				calls = dao.getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);

			} else if (period.equals(PERIOD_PRVMONTH)) {
				callsCount = dao.getPrvMonthCallsCount(queueName);
				calls = dao.getQueueLogPrvMonth(queueName, pagesize, page);

			} else if (period.equals(PERIOD_CURMONTH)) {
				callsCount = dao.getCurMonthCallsCount(queueName);
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

	@RequestMapping(value = "/commonLog", method = RequestMethod.GET)
	public String commonLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, Model model) {
		
		// TODO: check access to queue
		
		initDefaultData(model);
		model.addAttribute("queue", queueName);
		model.addAttribute("from", strFrom);
		model.addAttribute("to", strTo);
		model.addAttribute("period", period);
		
		if (period != null && !period.isEmpty()) {
			int callsCount = 0;
			int receivedCallsCount = 0;
			int averageWaitTime = 0;
			long sumCallTime = 0;

			if (period.equals(PERIOD_CUSTOM)) {
				callsCount = dao.getCustomCallsCount(queueName, strFrom, strTo);
				receivedCallsCount = dao.getCustomReceivedCallsCount(queueName, strFrom, strTo);
				averageWaitTime = dao.getCustomAverageWaitTime(queueName, strFrom, strTo);
				sumCallTime = dao.getCustomSumCallTime(queueName, strFrom, strTo);

			} else if (period.equals(PERIOD_PRVMONTH)) {
				callsCount = dao.getPrvMonthCallsCount(queueName);
				receivedCallsCount = dao.getPrvMonthReceivedCallsCount(queueName);
				averageWaitTime = dao.getPrvMonthAverageWaitTime(queueName);
				sumCallTime = dao.getPrvMonthSumCallTime(queueName);

			} else if (period.equals(PERIOD_CURMONTH)) {
				callsCount = dao.getCurMonthCallsCount(queueName);
				receivedCallsCount = dao.getCurMonthReceivedCallsCount(queueName);
				averageWaitTime = dao.getCurMonthAverageWaitTime(queueName);
				sumCallTime = dao.getCurMonthSumCallTime(queueName);

			} else {
				// error
			}
			
			model.addAttribute("callsCount", callsCount);
			model.addAttribute("receivedCallsCount", receivedCallsCount);
			model.addAttribute("unreceivedCallsCount", callsCount - receivedCallsCount);
			model.addAttribute("averageWaitTime", averageWaitTime);
			model.addAttribute("sumCallTime", sumCallTime);
		}

		return "common";
	}

	private void initDefaultData(Model model) {
		UserWithName user = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username", user.getUsername());
		
		model.addAttribute("humanname", user.getHumanname());
		
		model.addAttribute("monitorhost", monitorHost);

		List<Map<String, Object>> queues = dao.getQueues(user.getUsername());
		model.addAttribute("queues", queues);

		// periods
		model.addAttribute("periods", periods);
	}
}
