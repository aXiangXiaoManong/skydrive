package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ax.springmvc.pojo.ShareFile;

public interface ShareFileMapper {
	/**
	 ** ��ӷ����ļ����ļ���Ϣ
	 */
	int addShareFiles(@Param("shareFiles") List<ShareFile> shareFiles);

	/**
	 ** �����ļ������¼�����������õ������ļ�����Ϣ
	 */
	List<ShareFile> queryByFileShareId(@Param("fileShareIds") List<Integer> fileShareIds);
	
	/**
	 ** �����ļ������¼�����������õ������ļ�����Ϣ���ļ�����������
	 */
	@Select("select * from shareFile where fileShareId=#{fileShareId} ORDER BY folioType ASC")
	List<ShareFile> queryByFileShareIdOrderFolioType(@Param("fileShareId") Integer fileShareId);
}
