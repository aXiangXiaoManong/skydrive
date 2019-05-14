package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ax.springmvc.pojo.ShareUser;

public interface ShareUserMapper {
	/**
	 ** 添加被分享人
	 */
	int addShareUsers(@Param("shareUsers") List<ShareUser> shareUsers);

	/**
	 ** 更具分享记录的主键得到被分享的用户
	 */
	List<ShareUser> queryByFileShareId(@Param("fileSharIds") List<Integer> fileSharIds);

	/**
	 ** 根据被分享的用户id得到被分享用户
	 */
	List<ShareUser> queryByBeUserId(@Param("beuserIds") List<Integer> beuserIds);
}
