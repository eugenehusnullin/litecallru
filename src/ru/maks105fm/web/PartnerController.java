package ru.maks105fm.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.maks105fm.dao.Dao;
import ru.maks105fm.web.security.UserWithName;

@Controller
@RequestMapping("/partner")
public class PartnerController {

	public static String PERIOD_CURMONTH = "curmonth";
	public static String PERIOD_PRVMONTH = "prvmonth";
	public static String PERIOD_CUSTOM = "custom";

	@Autowired
	private Dao dao;
	
	private List<Map<String, Object>> periods;
	
	@RequestMapping(value = "/")
	public String home(Model model) {
		UserWithName partner = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addAttribute("username", partner.getUsername());
		model.addAttribute("humanname", partner.getHumanname());
		model.addAttribute("periods", periods);

		return "byday";
	}
	
	public PartnerController() {
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
	
	@RequestMapping(value = "/byDay")
	public String logByDay(@RequestParam("from") String strFrom, @RequestParam("to") String strTo,
			@RequestParam("period") String period, Model model) {
		UserWithName partner = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addAttribute("username", partner.getUsername());
		model.addAttribute("humanname", partner.getHumanname());
		model.addAttribute("periods", periods);
		model.addAttribute("from", strFrom);
		model.addAttribute("to", strTo);
		model.addAttribute("period", period);
		
		if (period != null && !period.isEmpty()) {
			List<Map<String, Object>> days = null;

			if (period.equals(PERIOD_CUSTOM)) {
				days = dao.getPartnerByDayCustom(partner.getUsername(), strFrom, strTo);

			} else if (period.equals(PERIOD_PRVMONTH)) {
				days = dao.getPartnerByDayPrvMonth(partner.getUsername());

			} else if (period.equals(PERIOD_CURMONTH)) {
				days = dao.getPartnerByDayCurMonth(partner.getUsername());

			} else {
				// error
			}

			model.addAttribute("days", days);
		}
		
		return "byday";
	}
	
	@RequestMapping(value = "/byClient")
	public String logByClient(@RequestParam("date") String strDate, Model model) {
		UserWithName partner = (UserWithName)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		model.addAttribute("username", partner.getUsername());
		model.addAttribute("humanname", partner.getHumanname());
		model.addAttribute("periods", periods);
		model.addAttribute("date", strDate);
		
		List<Map<String, Object>> clients = dao.getPartnerByClient(partner.getUsername(), strDate);
		
		model.addAttribute("clients", clients);
		
		return "byclient";
	}
}