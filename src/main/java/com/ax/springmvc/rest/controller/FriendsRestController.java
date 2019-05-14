package com.ax.springmvc.rest.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ax.springmvc.pojo.Friends;
import com.ax.springmvc.pojo.User;
import com.ax.springmvc.service.FileShareService;
import com.ax.springmvc.service.FriendsService;
import com.ax.springmvc.service.UserService;

@Controller
@ResponseBody
@RequestMapping("/rest/friend")
public class FriendsRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private FriendsService friendsService;
	@Autowired
	private FileShareService fileShareService;

	/**
	 ** 一个用户的好友
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Object selectUsers(HttpSession session) {
		return this.friendsService.selectUsers(((User) session.getAttribute("user")).getId());
	}

	/**
	 ** 寻找朋友
	 */
	@RequestMapping(value = "/{phone}", method = RequestMethod.GET)
	public Object findFriends(@PathVariable String phone, HttpSession session) {
		User user = (User) session.getAttribute("user");
		User friend = this.userService.selectOneByPhone(phone.trim());
		Map<String, Object> map = new HashMap<String, Object>();
		if (phone.equals(user.getPhone())) {
			map.put("status", 201);
			map.put("msg", "自己就不要加自己了！");
		} else if (this.friendsService.checkUser(user.getId(), phone) != null) {
			map.put("status", 201);
			map.put("msg", "该用户已添加过了！");
		} else if (friend == null) {
			map.put("status", 201);
			map.put("msg", "没有找到你搜索的用户，检查下输入的帐号吧");
		} else {
			map.put("status", 200);
			map.put("msg", "查找成功");
			map.put("user", friend);
		}
		return map;
	}

	/**
	 ** 添加好友
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Object addFriend(String phone, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		User friend = this.userService.selectOneByPhone(phone);
		int userId = ((User) session.getAttribute("user")).getId();
		if (this.friendsService.addFriend(new Friends(userId, friend.getId())) > 0) {
			map.put("status", 200);
			map.put("msg", "用户" + friend.getPhone() + "(" + friend.getNickname() + ")已成为您的好友！");
		} else {
			map.put("status", 201);
			map.put("msg", "添加失败！");
		}
		return map;
	}

	/**
	 ** 添加好友
	 */
	@RequestMapping(value = "/{friendId}", method = RequestMethod.DELETE)
	public Object delFriend(@PathVariable Integer friendId, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();
		int userId = ((User) session.getAttribute("user")).getId();
		if (this.friendsService.delFriend(userId, friendId) > 0) {
			map.put("status", 200);
			map.put("msg", "好友关系解除成功！");
		} else {
			map.put("status", 201);
			map.put("msg", "好友关系解除失败！");
		}
		return map;
	}
	
}
