package ru.maks105fm.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
	
	@RequestMapping(value = "/")
	public String root(Model model) {
		return "";
	}
}
