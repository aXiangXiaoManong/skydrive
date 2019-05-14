package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.ax.springmvc.pojo.User;

public interface UserMapper {
	/**
	 ** ��½
	 */
	@Select("select * from user where `phone`=#{phone} and pwd = #{pwd}")
	User login(@Param("phone") String phone, @Param("pwd") String pwd);

	/**
	 ** ע��
	 */
	int register(User user);

	/**
	 ** ��Ӻ���ǰ��Ѱ�Һ���
	 */
	@Select("select * from user where phone=#{phone}")
	User queryUser(@Param("phone") String phone);

	/**
	 ** �����û���id�õ�һ���û�
	 */
	@Select("select * from user where id = #{id}")
	User selectOne(@Param("id") int id);

	/**
	 ** �����û����˺ŵõ�һ���û�
	 */
	@Select("select * from user where phone = #{phone}")
	User selectOneByPhone(@Param("phone") String phone);

	/**
	 ** �޸��û���Ϣ
	 */
	@Update("update user set nickname=#{nickname},headImg=#{headImg},phone=#{phone},pwd=#{pwd} where id = #{id}")
	int updateUser(User user);

	/**
	 ** �����õ��û�����
	 * @return
	 */
	List<User> queryByUserIds(@Param("userIds") List<Integer> userIds);
}
