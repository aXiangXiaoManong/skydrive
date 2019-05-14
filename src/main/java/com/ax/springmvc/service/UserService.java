package com.ax.springmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ax.springmvc.mapper.UserMapper;
import com.ax.springmvc.pojo.User;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 ** 登陆
	 */
	public User login(String phone, String pwd) {
		return this.userMapper.login(phone, pwd);
	}

	/**
	 ** 注册
	 */
	public int register(User user) {
		return this.userMapper.register(user);
	}

	/**
	 ** 添加好友前的寻找好友
	 */
	public User queryUser(String phone) {
		return this.userMapper.queryUser(phone);

	}

	/**
	 ** 更具用户的id得到一个用户
	 */
	public User selectOne(int id) {
		return this.userMapper.selectOne(id);
	}

	/**
	 ** 更具用户的账号得到一个用户
	 */
	public User selectOneByPhone(String phone) {
		return this.userMapper.selectOneByPhone(phone);
	}

	/**
	 ** 修改用户信息
	 */
	public int updateUser(User user) {
		return this.userMapper.updateUser(user);
	}
}
