package ru.maks105fm.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
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

	@Autowired
	Comparator<String> comparator;

	@RequestMapping(value = "/")
	public String home(Model model) {
		initDefaultData(model);

		return "home";
	}

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String request(@RequestParam("queue") String queue,
			@RequestParam("from") String strFrom, @RequestParam("to") String strTo, Model model) {
		initDefaultData(model);
		
		// TODO: check access to queue
		
		try {
			DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
		
			// TODO: check strFrom and strTo
			Date dateFrom = df.parse(strFrom);
			Date dateTo = df.parse(strTo);
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "home";
	}

	@RequestMapping(value = "/compare", method = RequestMethod.GET)
	public String compare(@RequestParam("input1") String input1,
			@RequestParam("input2") String input2, Model model) {
		int result = comparator.compare(input1, input2);
		String inEnglish = (result < 0) ? "less than"
				: (result > 0 ? "greater than" : "equal to");
		String output = "According to our Comporator, '" + input1 + "' is "
				+ inEnglish + " '" + input2;

		model.addAttribute("output", output);
		return "compareResult";
	}
	
	private void initDefaultData(Model model) {
		model.addAttribute("username", "user1");

		List<Map<String, Object>> queues = dao.getQueues("user1"); 
		model.addAttribute("queues", queues);
	}

}
