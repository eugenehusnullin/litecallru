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
import ru.maks105fm.web.security.SuccessAuthHandler;
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
	
	private void initDefaultData(Model model) {
		UserWithName user = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute("username", user.getUsername());
		
		model.addAttribute("humanname", user.getHumanname());
		
		model.addAttribute("monitorhost", monitorHost);

		List<Map<String, Object>> queues = clientDao.getQueues(user.getId());
		model.addAttribute("queues", queues);

		// periods
		model.addAttribute("periods", periods);
		
		model.addAttribute("allowDownload", user.getAuthorities().contains(SuccessAuthHandler.AuthAllowDownload));
	}

	@RequestMapping(value = "/")
	public String home(Model model) {
		initDefaultData(model);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> queues = (List<Map<String, Object>>) model.asMap().get("queues");
		
		if (queues != null && queues.size() > 0) {
			return commonLog((String) queues.get(0).get("name"), null, null, PERIOD_CURMONTH, model);
		} else {
			return "common";
		}
	}
	
	private boolean checkAccessToQueue(String queueName) {
		UserWithName user = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return clientDao.hasUserRights(user.getId(), queueName);
	}

	@RequestMapping(value = "/detailedLog", method = RequestMethod.GET)
	public String detailedLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, @RequestParam("page") Integer page, Model model) {
		
		if (queueName != null && queueName != "") {
			if (!checkAccessToQueue(queueName)) {
				return "";
			}
		}

		initDefaultData(model);
		model.addAttribute("queue", queueName);
		model.addAttribute("from", strFrom);
		model.addAttribute("to", strTo);
		model.addAttribute("period", period);

		int pagesize = 50;

		if (page == null) {
			page = 1;
		}

		if (period != null && !period.isEmpty()) {
			List<Map<String, Object>> calls = null;
			int callsCount = 0;
			
			
			int innerTypeId = getChannelInnerTypeId(queueName, model);
			if (innerTypeId == 1) {
				if (period.equals(PERIOD_CUSTOM)) {
					callsCount = clientDao.getCustomQueueCalls(queueName, strFrom, strTo);
					calls = clientDao.getQueueLogCustom(queueName, strFrom, strTo, pagesize, page);
	
				} else if (period.equals(PERIOD_PRVMONTH)) {
					callsCount = clientDao.getPrvMonthQueueCalls(queueName);
					calls = clientDao.getQueueLogPrvMonth(queueName, pagesize, page);
	
				} else if (period.equals(PERIOD_CURMONTH)) {
					callsCount = clientDao.getCurMonthQueueCalls(queueName);
					calls = clientDao.getQueueLogCurMonth(queueName, pagesize, page);
	
				} else {
					// error
				}
			} else if (innerTypeId == 2) {
				if (period.equals(PERIOD_CUSTOM)) {
					callsCount = clientDao.getCustomOutCalls(queueName, strFrom, strTo);
					calls = clientDao.getOutLogCustom(queueName, strFrom, strTo, pagesize, page);
	
				} else if (period.equals(PERIOD_PRVMONTH)) {
					callsCount = clientDao.getPrvMonthOutCalls(queueName);
					calls = clientDao.getOutLogPrvMonth(queueName, pagesize, page);
	
				} else if (period.equals(PERIOD_CURMONTH)) {
					callsCount = clientDao.getCurMonthOutCalls(queueName);
					calls = clientDao.getOutLogCurMonth(queueName, pagesize, page);
	
				} else {
					// error
				}
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
	
	private int getChannelInnerTypeId(String queueName, Model model) {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> queues = (List<Map<String, Object>>) model.asMap().get("queues");
		
		for (Map<String, Object> map : queues) {
			if (((String)map.get("name")).equals(queueName)) {
				return (Integer) map.get("innertypeid");
			}
		}
		
		return 0;		
	}

	@RequestMapping(value = "/commonLog", method = RequestMethod.GET)
	public String commonLog(@RequestParam("queue") String queueName,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, Model model) {
		
		if (queueName != null && queueName != "") {
			if (!checkAccessToQueue(queueName)) {
				return "";
			}
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

			int innerTypeId = getChannelInnerTypeId(queueName, model);
			if (innerTypeId == 1) {
				if (period.equals(PERIOD_CUSTOM)) {
					callsCount = clientDao.getCustomQueueCalls(queueName, strFrom, strTo);
					receivedCallsCount = clientDao.getCustomReceivedQueueCalls(queueName, strFrom, strTo);
					averageWaitTime = clientDao.getCustomQueueAverageWaitTime(queueName, strFrom, strTo);
					sumCallTime = clientDao.getCustomQueueSumCallTime(queueName, strFrom, strTo);
	
				} else if (period.equals(PERIOD_PRVMONTH)) {
					callsCount = clientDao.getPrvMonthQueueCalls(queueName);
					receivedCallsCount = clientDao.getPrvMonthReceivedQueueCalls(queueName);
					averageWaitTime = clientDao.getPrvMonthQueueAverageWaitTime(queueName);
					sumCallTime = clientDao.getPrvMonthQueueSumCallTime(queueName);
	
				} else if (period.equals(PERIOD_CURMONTH)) {
					callsCount = clientDao.getCurMonthQueueCalls(queueName);
					receivedCallsCount = clientDao.getCurMonthReceivedQueueCalls(queueName);
					averageWaitTime = clientDao.getCurMonthQueueAverageWaitTime(queueName);
					sumCallTime = clientDao.getCurMonthQueueSumCallTime(queueName);
	
				} else {
					// error
				}
			} else if (innerTypeId == 2) {
				if (period.equals(PERIOD_CUSTOM)) {
					callsCount = clientDao.getCustomOutCalls(queueName, strFrom, strTo);
					receivedCallsCount = clientDao.getCustomReceivedOutCalls(queueName, strFrom, strTo);
					averageWaitTime = clientDao.getCustomOutAverageWaitTime(queueName, strFrom, strTo);
					sumCallTime = clientDao.getCustomOutSumCallTime(queueName, strFrom, strTo);
	
				} else if (period.equals(PERIOD_PRVMONTH)) {
					callsCount = clientDao.getPrvMonthOutCalls(queueName);
					receivedCallsCount = clientDao.getPrvMonthReceivedOutCalls(queueName);
					averageWaitTime = clientDao.getPrvMonthOutAverageWaitTime(queueName);
					sumCallTime = clientDao.getPrvMonthOutSumCallTime(queueName);
	
				} else if (period.equals(PERIOD_CURMONTH)) {
					callsCount = clientDao.getCurMonthOutCalls(queueName);
					receivedCallsCount = clientDao.getCurMonthReceivedOutCalls(queueName);
					averageWaitTime = clientDao.getCurMonthOutAverageWaitTime(queueName);
					sumCallTime = clientDao.getCurMonthOutSumCallTime(queueName);
	
				} else {
					// error
				}
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
}
