package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ax.springmvc.pojo.Friends;
import com.ax.springmvc.pojo.User;

public interface FriendsMapper {
	/**
	 ** �õ�һ���û������к���
	 */
	@Select("select * from user where id in (select friendId from friends where userId=#{userId})")
	List<User> selectUsers(@Param("userId") int userId);

	/**
	 ** ��Ӻ���ǰȷ���Ƿ�Ϊ����
	 */
	@Select("SELECT * FROM friends WHERE userId=#{userId} and friendId = (SELECT id FROM `user` WHERE phone = #{phone})")
	Friends checkUser(@Param("userId") int userId, @Param("phone") String phone);

	/**
	 ** ��Ӻ���
	 */
	@Insert("insert into friends values(0,#{userId},now(),#{friendId})")
	int addFriend(Friends friends);

	/**
	 ** ɾ������
	 */
	@Delete("delete from friends where userId=#{userId} and friendId=#{friendId}")
	int delFriend(@Param("userId") int userId, @Param("friendId") int friendId);
}
