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
	 ** 得到一个用户的所有好友
	 */
	public List<User> selectUsers(int userId) {
		return this.friendsMapper.selectUsers(userId);
	}

	/**
	 ** 添加好友前确定是否为好友
	 */
	public Friends checkUser(int userId, String phone) {
		return this.friendsMapper.checkUser(userId, phone);
	}

	/**
	 ** 添加好友
	 */
	public int addFriend(Friends friends) {
		return this.friendsMapper.addFriend(friends);
	};

	/**
	 ** 删除好友
	 */
	public int delFriend(int userId, int friendId) {
		return this.friendsMapper.delFriend(userId, friendId);
	};
}
