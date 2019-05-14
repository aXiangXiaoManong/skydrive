package com.ax.springmvc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ax.springmvc.pojo.ShareFile;

public interface ShareFileMapper {
	/**
	 ** 添加分享文件和文件信息
	 */
	int addShareFiles(@Param("shareFiles") List<ShareFile> shareFiles);

	/**
	 ** 更具文件分享记录的主键批量得到分享文件的信息
	 */
	List<ShareFile> queryByFileShareId(@Param("fileShareIds") List<Integer> fileShareIds);
	
	/**
	 ** 更具文件分享记录的主键批量得到分享文件的信息按文件夹类型排序
	 */
	@Select("select * from shareFile where fileShareId=#{fileShareId} ORDER BY folioType ASC")
	List<ShareFile> queryByFileShareIdOrderFolioType(@Param("fileShareId") Integer fileShareId);
}
