package com.ax.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ax.springmvc.mapper.FriendsMapper;
import com.ax.springmvc.pojo.Friends;
import com.ax.springmvc.pojo.User;

@Service
public class FriendsService {

	@Autowired
	private FriendsMapper friendsMapper;

	/**
	 ** �õ�һ���û������к���
	 */
	public List<User> selectUsers(int userId) {
		return this.friendsMapper.selectUsers(userId);
	}

	/**
	 ** ��Ӻ���ǰȷ���Ƿ�Ϊ����
	 */
	public Friends checkUser(int userId, String phone) {
		return this.friendsMapper.checkUser(userId, phone);
	}

	/**
	 ** ��Ӻ���
	 */
	public int addFriend(Friends friends) {
		return this.friendsMapper.addFriend(friends);
	};

	/**
	 ** ɾ������
	 */
	public int delFriend(int userId, int friendId) {
		return this.friendsMapper.delFriend(userId, friendId);
	};
}
