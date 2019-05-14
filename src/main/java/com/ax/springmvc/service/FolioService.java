package com.ax.springmvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ax.springmvc.mapper.FileInfoMapper;
import com.ax.springmvc.mapper.FolioMapper;
import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;
import com.ax.springmvc.util.chcekfilename.FileHelper;
import com.ax.springmvc.util.file.FileOption;
import com.ax.springmvc.util.orderby.FolioTimeAsc;
import com.ax.springmvc.util.orderby.FolioTimeDesc;
import com.ax.springmvc.vo.FolioInfoVo;

@Service
public class FolioService {

	@Autowired
	private FolioMapper folioMapper;

	@Autowired
	private FileInfoMapper fileInfoMapper;

	private FileOption fileOption = new FileOption();
	private FileHelper fileHelper = new FileHelper();

	/**
	 ** 得到一个目录下所有文件夹
	 */
	public List<Folio> selectByUser(int userId, int parentDir, String order, int isNotDel, int isNotShare) {
		List<Folio> list = folioMapper.selectByUser(userId, parentDir, isNotDel, isNotShare);
		if (order == null || order.equals("timeAsc")) {
			list.sort(new FolioTimeAsc());
		} else if (order.equals("timeDesc")) {
			list.sort(new FolioTimeDesc());
		}
		return list;
	}

	/**
	 ** 更具文件夹的Id指定的文件夹
	 */
	public Folio selectOne(int id) {
		return folioMapper.selectOne(id);
	}

	/**
	 ** 更具文件夹的Ids多个文件夹
	 */
	public List<Folio> selectByIds(List<Integer> ids) {
		if (ids.size() == 0)
			return null;
		return folioMapper.selectByIds(ids);
	}

	// 存放路径导航的文件集合
	private List<Folio> folios = null;

	private void getFolio(int id) {
		Folio f = folioMapper.selectOne(id);
		if (f == null) {
			return;
		}
		if (f.getParentDir() > 0) {
			getFolio(f.getParentDir());
		}
		folios.add(f);
	}

	public List<Folio> getFolios(int userId, int parentDir) {
		folios = new ArrayList<Folio>();
		getFolio(parentDir);
		return folios;
	}

	/**
	 ** 创建文件夹（folio）
	 */
	public int createFolio(Folio folio) {
		this.getRname(folio, null);
		return this.folioMapper.createFolio(folio);
	}

	/**
	 ** 修改文件文件夹（folio）
	 */
	public int updateFolio(Folio folio) {
		return this.folioMapper.updateFolio(folio);
	}

	/**
	 ** 根据Id的删除文件夹
	 */
	public int deleteFolio(int id) {
		return this.folioMapper.deleteFolio(id);
	}

	/**
	 ** 文件移动和文件夹移动
	 * 
	 * @param typeId    1为文件夹-2为文件
	 * @param id        被移动的文件夹/文件
	 * @param parentDir 目标文件夹下
	 */
	public int moveFolioAndFile(int typeId, int id, int parentDir) {
		if (typeId == 1) {
			Folio folio = folioMapper.selectOne(id);
			folio.setParentDir(parentDir);
			this.getRname(folio, null);
			return this.folioMapper.updateFolio(folio);
		} else {
			FileInfo fileInfo = fileInfoMapper.selectOne(id);
			fileInfo.setParentDir(parentDir);
			return this.fileInfoMapper.updateFileInfo(fileInfo);
		}
	}

	/**
	 * 文件和文件夹复制操作
	 * 
	 * @param typeId    1为文件夹-2为文件
	 * @param id        被复制的文件夹/文件
	 * @param parentDir 目标文件夹下
	 * @param isOne     是否为第一次
	 */
	@Transactional
	public void copyFolioAndFile(int userId,int typeId, int id, int parentDir, int isNotShare, boolean isOne) {
		if (typeId == 1) {// 文件夹
			int oldparentDir = id;
			Folio folio = this.folioMapper.selectOne(id);
			int endParentDir = folio.getParentDir();
			folio.setParentDir(parentDir);
			folio.setIsNotShare(isNotShare);

			int olduserId = folio.getUserId();
			folio.setUserId(userId);
			if (isOne) {
				this.getRname(folio, endParentDir == parentDir ? " - 副本" : null);
			}

			this.folioMapper.createFolio(folio);

			List<Folio> folios = this.folioMapper.selectByUser(olduserId, oldparentDir, 1, isNotShare);
			List<FileInfo> files = this.fileInfoMapper.selectByUserId(olduserId, oldparentDir, 1, isNotShare);
			List<FolioInfoVo> lists = fileHelper.changeObj(folios, files);
			for (FolioInfoVo f : lists) {
				copyFolioAndFile(userId,f.getType(), f.getId(), folio.getId(), isNotShare);
			}

		} else {// 文件
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			fileInfo.setIsNotShare(isNotShare);
			int endParentDir = fileInfo.getParentDir();
			fileInfo.setParentDir(parentDir);
			FileInfo f2 = this.fileInfoMapper.selectOneByFileNmae(fileInfo.getUserId(), fileInfo.getParentDir(),
					fileInfo.getFileName());
			this.fileHelper.getRname(fileInfo, f2, endParentDir == parentDir ? " - 副本" : null);
			String oldFileSrc = fileInfo.getFileSrc();
			StringBuffer newFilePath = new StringBuffer(oldFileSrc.substring(0, oldFileSrc.lastIndexOf("/") + 1));
			newFilePath.append(UUID.randomUUID());
			newFilePath.append(".");
			newFilePath.append(fileInfo.getFileType());
			fileInfo.setFileSrc(newFilePath.toString());// 文件的上传新路径
			fileInfo.setUserId(userId);
			this.fileOption.copy(oldFileSrc, newFilePath.toString());
			this.fileInfoMapper.newFile(fileInfo);
		}
	}
	
	/**
	 * 保存到
	 */
	@Transactional
	public void copyFolioAndFile(int userId,int typeId, int id, int parentDir, int isNotShare) {
		if (typeId == 1) {// 文件夹
			int oldparentDir = id;
			Folio folio = this.folioMapper.selectOne(id);
			folio.setParentDir(parentDir);
			folio.setIsNotShare(isNotShare);

			int olduserId = folio.getUserId();
			folio.setUserId(userId);

			this.folioMapper.createFolio(folio);

			List<Folio> folios = this.folioMapper.selectByUser(olduserId, oldparentDir, 1, 2);
			List<FileInfo> files = this.fileInfoMapper.selectByUserId(olduserId, oldparentDir, 1, 2);
			List<FolioInfoVo> lists = fileHelper.changeObj(folios, files);
			for (FolioInfoVo f : lists) {
				copyFolioAndFile(userId,f.getType(), f.getId(), folio.getId(), isNotShare, false);
			}

		} else {// 文件
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			fileInfo.setIsNotShare(isNotShare);
			int endParentDir = fileInfo.getParentDir();
			fileInfo.setParentDir(parentDir);
			FileInfo f2 = this.fileInfoMapper.selectOneByFileNmae(fileInfo.getUserId(), fileInfo.getParentDir(),
					fileInfo.getFileName());
			this.fileHelper.getRname(fileInfo, f2, endParentDir == parentDir ? " - 副本" : null);
			String oldFileSrc = fileInfo.getFileSrc();
			StringBuffer newFilePath = new StringBuffer(oldFileSrc.substring(0, oldFileSrc.lastIndexOf("/") + 1));
			newFilePath.append(UUID.randomUUID());
			newFilePath.append(".");
			newFilePath.append(fileInfo.getFileType());
			fileInfo.setFileSrc(newFilePath.toString());// 文件的上传新路径
			fileInfo.setUserId(userId);
			this.fileOption.copy(oldFileSrc, newFilePath.toString());
			this.fileInfoMapper.newFile(fileInfo);
		}
	}

	/**
	 * 批量修改文件信息
	 * 
	 * @param folios
	 * @return
	 */
	@Transactional
	public int updateFolios(List<Folio> folios) {
		return this.folioMapper.updateFolios(folios);
	}

	/**
	 ** 批量将文件/文件夹删除进入回收站
	 */
	@Transactional
	public void delFolioAndFile(Integer[] typeId, Integer[] id, int isNotDel) {
		if (typeId.length == 0)
			throw new RuntimeException("array NullException...ax");
		List<Integer> folioIds = new ArrayList<Integer>();
		List<Integer> fileIds = new ArrayList<Integer>();
		for (int i = 0; i < id.length; i++) {
			if (typeId[i] == 1) {
				folioIds.add(id[i]);
			} else {
				fileIds.add(id[i]);
			}
		}
		if (folioIds.size() > 0) {
			this.folioMapper.updateFolisTodel(folioIds, isNotDel);
		}
		if (fileIds.size() > 0)
			this.fileInfoMapper.updateFileTodel(fileIds, isNotDel);
	}

	/**
	 ** 文件夹/文件批量还原-0929准备放弃
	 */
	public void restoreFolioOrFile(Integer typeId, Integer id) {
		if (typeId == 1) {
			Folio folio = this.folioMapper.selectOne(id);
			if (folio.getParentDir() > 0) {
				Integer[] num = { 0 };
				this.findParentIsDelNum(folio.getParentDir(), num);
				this.findParentDir(folio, num);
			}
			folio.setIsNotDel(1);
			this.getRname(folio, null);
			this.folioMapper.updateFolio(folio);
		} else {
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			if (fileInfo.getParentDir() > 0) {
				Integer[] num = { 0 };
				this.findParentIsDelNum(fileInfo.getParentDir(), num);

				if (num[0] > 0) {
					Folio folio = this.folioMapper.selectOne(fileInfo.getParentDir());
					folio.setIsNotDel(1);
					this.createFolio(folio);
					fileInfo.setParentDir(folio.getId());

					this.findParentDir(folio, num);
				}
			}
			fileInfo.setIsNotDel(1);
			this.fileHelper.getRname(fileInfo, this.fileInfoMapper.selectOneByFileNmae(fileInfo.getUserId(),
					fileInfo.getParentDir(), fileInfo.getFileName()), null);
			this.fileInfoMapper.updateFileInfo(fileInfo);
		}
	}

	/**
	 ** 找到我的父父代代的文件夹有多少个被删除
	 */
	private void findParentIsDelNum(int parentDir, Integer[] num) {
		if (parentDir == 0)
			return;
		Folio folio = this.folioMapper.selectOne(parentDir);
		if (folio.getIsNotDel() == 2)
			num[0] = num[0] + 1;
		findParentIsDelNum(folio.getParentDir(), num);
	}

	/**
	 ** 找到一个父文件夹,并将父文件,给为新创建的父文件夹
	 */
	private void findParentDir(Folio childrenFolio, Integer[] num) {
		if (childrenFolio.getParentDir() == 0 || num[0] <= 0)
			return;
		Folio folio = this.folioMapper.selectOne(childrenFolio.getParentDir());
		if (folio.getIsNotDel() == 2) {
			num[0] = num[0] - 1;
			folio.setIsNotDel(1);
		}
		this.createFolio(folio);
		childrenFolio.setParentDir(folio.getId());
		findParentDir(folio, num);
	}

	/**
	 ** 检查文件夹重名问题更改showName的值
	 * 
	 * @param folioName
	 * @return
	 */
	public void getRname(Folio f, String str) {
		if (str != null) {
			if (f.getShowName().lastIndexOf(str) < 0) {
				f.setFolioName(f.getShowName() + str);
				f.setShowName(f.getShowName() + str);
			}
		}
		Folio f2 = this.folioMapper.selectOneByFolioNmae(f.getUserId(), f.getParentDir(), f.getFolioName());
		if (f2 == null) {
			int index2 = f.getFolioName().lastIndexOf(")");
			if (index2 == f.getFolioName().length() - 1) {
				int index1 = f.getFolioName().lastIndexOf("(");
				if (index1 + 1 < index2) {
					f.setFolioName(f.getFolioName().substring(0, index1));
				}
			}
		} else {
			f.setShowName(f2.getShowName());
			int index2 = f.getShowName().lastIndexOf(")");
			boolean flg = true;
			if (index2 == f.getShowName().length() - 1) {
				int index1 = f.getShowName().lastIndexOf("(");
				if (index1 + 1 < index2) {
					String str_num = f.getShowName().substring(index1 + 1, index2);
					try {
						int rname = Integer.parseInt(str_num) + 1;
						String folioName = f.getShowName().substring(0, index1);
						f.setFolioName(folioName);
						f.setShowName(folioName + "(" + rname + ")");
						flg = false;
					} catch (NumberFormatException e) {
					}
				}
			}
			if (flg) {
				f.setShowName(f.getShowName() + "(2)");
			}
		}
	}

	/**
	 ** 删除文件或文件夹
	 * 
	 * @param id     文件id或文件夹id
	 * @param typeId 1为文件夹否则为文件
	 * @param isOne
	 */
	@Transactional
	public void delFolioOrFile(int id, int typeId, boolean isOne, int isNotShare, List<String> filePath) {
		if (typeId == 1) {
			Folio folio = this.folioMapper.selectOne(id);

			List<Folio> folios = this.folioMapper.selectByUser(folio.getUserId(), folio.getId(), 1, isNotShare);
			List<FileInfo> files = this.fileInfoMapper.selectByUserId(folio.getUserId(), folio.getId(), 1, isNotShare);
			List<FolioInfoVo> lists = fileHelper.changeObj(folios, files);
			for (FolioInfoVo f : lists) {
				delFolioOrFile(f.getId(), f.getType(), false, isNotShare, filePath);
			}
			if (!isOne)
				this.folioMapper.deleteFolio(id);
		} else {
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			filePath.add(fileInfo.getFileSrc());

			if (!isOne)
				this.fileInfoMapper.deleteFileInfo(id);
		}
	}
}
