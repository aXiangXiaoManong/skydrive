package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ax.springmvc.pojo.ShareUser;

public interface ShareUserMapper {
	/**
	 ** ��ӱ�������
	 */
	int addShareUsers(@Param("shareUsers") List<ShareUser> shareUsers);

	/**
	 ** ���߷����¼�������õ���������û�
	 */
	List<ShareUser> queryByFileShareId(@Param("fileSharIds") List<Integer> fileSharIds);

	/**
	 ** ���ݱ�������û�id�õ��������û�
	 */
	List<ShareUser> queryByBeUserId(@Param("beuserIds") List<Integer> beuserIds);
}
