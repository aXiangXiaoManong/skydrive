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
	 ** ��ѯһ��Ŀ¼�µ������ļ�
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
	 ** ���ļ����ͽ��в�ѯ
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
	 ** �޸��ļ���Ϣ
	 */
	public int updateFileInfo(FileInfo fileInfo) {
		return this.fileInfoMapper.updateFileInfo(fileInfo);
	}

	/**
	 ** �����ļ�Id�õ��ļ�
	 */
	public FileInfo selectOne(int id) {
		return this.fileInfoMapper.selectOne(id);
	}

	/**
	 ** �����ļ���Ids�õ�����ļ�
	 */
	public List<FileInfo> selectOne(List<Integer> ids) {
		return fileInfoMapper.selectByIds(ids);
	}

	/**
	 ** ����Id��ɾ���ļ�
	 */
	public int deleteFileInfo(int id) {
		return this.fileInfoMapper.deleteFileInfo(id);
	}

	/**
	 ** �����ϴ����ļ�
	 */
	@Transactional
	public int fileUpLoad(List<FileInfo> fileInfos) {
		if (fileInfos == null || fileInfos.size() == 0)
			return 0;
		return this.fileInfoMapper.fileUpLoad(fileInfos);
	}

	/**
	 * �����޸��ļ�
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
