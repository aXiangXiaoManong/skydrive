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
	 ** ��½
	 */
	public User login(String phone, String pwd) {
		return this.userMapper.login(phone, pwd);
	}

	/**
	 ** ע��
	 */
	public int register(User user) {
		return this.userMapper.register(user);
	}

	/**
	 ** ��Ӻ���ǰ��Ѱ�Һ���
	 */
	public User queryUser(String phone) {
		return this.userMapper.queryUser(phone);

	}

	/**
	 ** �����û���id�õ�һ���û�
	 */
	public User selectOne(int id) {
		return this.userMapper.selectOne(id);
	}

	/**
	 ** �����û����˺ŵõ�һ���û�
	 */
	public User selectOneByPhone(String phone) {
		return this.userMapper.selectOneByPhone(phone);
	}

	/**
	 ** �޸��û���Ϣ
	 */
	public int updateUser(User user) {
		return this.userMapper.updateUser(user);
	}
}
