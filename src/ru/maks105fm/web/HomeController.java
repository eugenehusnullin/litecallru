package ru.maks105fm.web;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
		String username = "user1";
		model.addAttribute("username", username);

		List<String> queues = dao.getQueues(username);
		model.addAttribute("queues", queues);

		return "home";
	}

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String request(@RequestParam("queue") String queue,
			@RequestParam("from") Date from, @RequestParam("to") Date to) {
		
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

}
