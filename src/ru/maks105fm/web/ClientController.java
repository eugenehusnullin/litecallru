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

import ru.maks105fm.dao.ClientDao;
import ru.maks105fm.web.security.UserWithName;

@Controller
@RequestMapping("/client")
public class ClientController {

	public static String PERIOD_CURMONTH = "curmonth";
	public static String PERIOD_PRVMONTH = "prvmonth";
	public static String PERIOD_CUSTOM = "custom";

	@Autowired
	private ClientDao clientDao;
	
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
	
	private boolean checkAccessToQueue(String queueName) {
		UserWithName user = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return clientDao.hasUserRights(user.getId(), queueName);
	}

	@RequestMapping(value = "/detailedLog", method = RequestMethod.GET)
	public String detailedLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, @RequestParam("page") Integer page, Model model) {
		
		if (!checkAccessToQueue(queueName)) {
			return "";
		}

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
				callsCount = clientDao.getCustomCallsCount(queueName, strFrom, strTo);
				calls = clientDao.getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);

			} else if (period.equals(PERIOD_PRVMONTH)) {
				callsCount = clientDao.getPrvMonthCallsCount(queueName);
				calls = clientDao.getQueueLogPrvMonth(queueName, pagesize, page);

			} else if (period.equals(PERIOD_CURMONTH)) {
				callsCount = clientDao.getCurMonthCallsCount(queueName);
				calls = clientDao.getQueueLogCurMonth(queueName, pagesize, page);

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
		
		if (!checkAccessToQueue(queueName)) {
			return "";
		}
		
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
				callsCount = clientDao.getCustomCallsCount(queueName, strFrom, strTo);
				receivedCallsCount = clientDao.getCustomReceivedCallsCount(queueName, strFrom, strTo);
				averageWaitTime = clientDao.getCustomAverageWaitTime(queueName, strFrom, strTo);
				sumCallTime = clientDao.getCustomSumCallTime(queueName, strFrom, strTo);

			} else if (period.equals(PERIOD_PRVMONTH)) {
				callsCount = clientDao.getPrvMonthCallsCount(queueName);
				receivedCallsCount = clientDao.getPrvMonthReceivedCallsCount(queueName);
				averageWaitTime = clientDao.getPrvMonthAverageWaitTime(queueName);
				sumCallTime = clientDao.getPrvMonthSumCallTime(queueName);

			} else if (period.equals(PERIOD_CURMONTH)) {
				callsCount = clientDao.getCurMonthCallsCount(queueName);
				receivedCallsCount = clientDao.getCurMonthReceivedCallsCount(queueName);
				averageWaitTime = clientDao.getCurMonthAverageWaitTime(queueName);
				sumCallTime = clientDao.getCurMonthSumCallTime(queueName);

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

		List<Map<String, Object>> queues = clientDao.getQueues(user.getId());
		model.addAttribute("queues", queues);

		// periods
		model.addAttribute("periods", periods);
	}
}
