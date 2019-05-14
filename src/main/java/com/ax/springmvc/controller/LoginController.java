package com.ax.springmvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ax.springmvc.pojo.User;
import com.ax.springmvc.service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {
	@Autowired
	private UserService userService;

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home/login";
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public Object login(String phone, String pwd, HttpSession session, HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userService.login(phone, pwd);
		if (user == null) {
			map.put("status", 201);
			map.put("msg", "µÇÂ½Ê§°Ü£¡£¡£¡");
		} else {
			session.setAttribute("user", user);
			map.put("status", 200);
			map.put("msg", "µÇÂ½³É¹¦");
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Object register(User user, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.userService.register(user) > 0) {
			session.setAttribute("user", user);
			map.put("status", 200);
			map.put("msg", "×¢²á³É¹¦£¬²¢×Ô¶¯µÇÂ½");
		} else {
			map.put("status", 201);
			map.put("msg", "×¢²áÊ§°Ü£¡£¡£¡");
		}
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "/checkPhone", method = RequestMethod.GET)
	public int checkPhone(String phone) {
		if (this.userService.queryUser(phone) == null) {
			return 0;
		}
		return 1;
	}
}
