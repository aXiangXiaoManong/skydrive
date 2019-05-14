package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.User;

public interface UserMapper {
	/**
	 ** 登陆
	 */
	@Select("select * from user where `phone`=#{phone} and pwd = #{pwd}")
	User login(@Param("phone") String phone, @Param("pwd") String pwd);

	/**
	 ** 注册
	 */
	int register(User user);

	/**
	 ** 添加好友前的寻找好友
	 */
	@Select("select * from user where phone=#{phone}")
	User queryUser(@Param("phone") String phone);

	/**
	 ** 更具用户的id得到一个用户
	 */
	@Select("select * from user where id = #{id}")
	User selectOne(@Param("id") int id);

	/**
	 ** 更具用户的账号得到一个用户
	 */
	@Select("select * from user where phone = #{phone}")
	User selectOneByPhone(@Param("phone") String phone);

	/**
	 ** 修改用户信息
	 */
	@Update("update user set nickname=#{nickname},headImg=#{headImg},phone=#{phone},pwd=#{pwd} where id = #{id}")
	int updateUser(User user);

	/**
	 ** 批量得到用户集合
	 * @return
	 */
	List<User> queryByUserIds(@Param("userIds") List<Integer> userIds);
}
