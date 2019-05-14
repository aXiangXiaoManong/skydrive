package com.ax.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ax.springmvc.mapper.FileInfoMapper;
import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.util.orderby.FileTimeAsc;
import com.ax.springmvc.util.orderby.FileTimeDesc;

@Service
public class FileInfoService {

	@Autowired
	private FileInfoMapper fileInfoMapper;

	/**
	 ** 查询一个目录下的所有文件
	 */
	public List<FileInfo> selectByUserId(int userId, int parentDir, String order, int isNotDel, int isNotShare) {
		List<FileInfo> list = this.fileInfoMapper.selectByUserId(userId, parentDir, isNotDel, isNotShare);
		if (order == null || order.equals("timeAsc")) {
			list.sort(new FileTimeAsc());
		} else if (order.equals("timeDesc")) {
			list.sort(new FileTimeDesc());
		}
		return list;
	}

	/**
	 ** 按文件类型进行查询
	 */
	public List<FileInfo> selectByType(int userId, String fileType, String order, int isNotShare) {
		String[] fileTypes = fileType.split(",");
		List<FileInfo> list = this.fileInfoMapper.selectByType(userId, fileTypes, isNotShare);
		if (order == null || order.equals("timeAsc")) {
			list.sort(new FileTimeAsc());
		} else if (order.equals("timeDesc")) {
			list.sort(new FileTimeDesc());
		}
		return list;
	}

	/**
	 ** 修改文件信息
	 */
	public int updateFileInfo(FileInfo fileInfo) {
		return this.fileInfoMapper.updateFileInfo(fileInfo);
	}

	/**
	 ** 根据文件Id得到文件
	 */
	public FileInfo selectOne(int id) {
		return this.fileInfoMapper.selectOne(id);
	}

	/**
	 ** 更具文件的Ids得到多个文件
	 */
	public List<FileInfo> selectOne(List<Integer> ids) {
		return fileInfoMapper.selectByIds(ids);
	}

	/**
	 ** 根据Id的删除文件
	 */
	public int deleteFileInfo(int id) {
		return this.fileInfoMapper.deleteFileInfo(id);
	}

	/**
	 ** 批量上传建文件
	 */
	@Transactional
	public int fileUpLoad(List<FileInfo> fileInfos) {
		if (fileInfos == null || fileInfos.size() == 0)
			return 0;
		return this.fileInfoMapper.fileUpLoad(fileInfos);
	}

	/**
	 * 批量修改文件
	 * 
	 * @param fileInfos
	 * @return
	 */
	@Transactional
	public int updateFileInfos(List<FileInfo> fileInfos) {
		return this.fileInfoMapper.updateFileInfos(fileInfos);
	}

	public FileInfo selectOneByFileNmae(int userId, int parentDir, String fileName) {
		return this.fileInfoMapper.selectOneByFileNmae(userId, parentDir, fileName);
	}

	public List<FileInfo> listByLikeFileName(int userId, String fileName) {
		return this.fileInfoMapper.listByLikeFileName(userId, fileName);
	}
}
