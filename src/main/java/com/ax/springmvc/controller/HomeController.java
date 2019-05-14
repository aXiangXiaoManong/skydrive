package com.ax.springmvc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ax.springmvc.pojo.User;

@Controller
@RequestMapping("/home")
public class HomeController {

	@RequestMapping("/index")
	public String index(HttpSession session) {
		if(session.getAttribute("user")==null)
			return "sky/login";
		return "sky/index";
	}
	
	@RequestMapping("/redirectIndex")
	public String redirectIndex(HttpSession session) {
		return "redirect:/home/index";
	}

	@RequestMapping("/login")
	public String login() {
		return "sky/login";
	}

	@RequestMapping("/share")
	public String share(HttpSession session) {
		if(session.getAttribute("user")==null)
			return "sky/login";
		return "sky/share";
	}
	
	@RequestMapping("/msgInfo")
	public String msgInfo() {
		return "sky/msgInfo";
	}
	
	@RequestMapping("/aaa")
	public String messsageInfo() {
		return "sky/msgInfo";
	}
}
