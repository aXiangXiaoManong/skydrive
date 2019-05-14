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
	 ** �õ�һ��Ŀ¼�������ļ���
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
	 ** �����ļ��е�Idָ�����ļ���
	 */
	public Folio selectOne(int id) {
		return folioMapper.selectOne(id);
	}

	/**
	 ** �����ļ��е�Ids����ļ���
	 */
	public List<Folio> selectByIds(List<Integer> ids) {
		if (ids.size() == 0)
			return null;
		return folioMapper.selectByIds(ids);
	}

	// ���·���������ļ�����
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
	 ** �����ļ��У�folio��
	 */
	public int createFolio(Folio folio) {
		this.getRname(folio, null);
		return this.folioMapper.createFolio(folio);
	}

	/**
	 ** �޸��ļ��ļ��У�folio��
	 */
	public int updateFolio(Folio folio) {
		return this.folioMapper.updateFolio(folio);
	}

	/**
	 ** ����Id��ɾ���ļ���
	 */
	public int deleteFolio(int id) {
		return this.folioMapper.deleteFolio(id);
	}

	/**
	 ** �ļ��ƶ����ļ����ƶ�
	 * 
	 * @param typeId    1Ϊ�ļ���-2Ϊ�ļ�
	 * @param id        ���ƶ����ļ���/�ļ�
	 * @param parentDir Ŀ���ļ�����
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
	 * �ļ����ļ��и��Ʋ���
	 * 
	 * @param typeId    1Ϊ�ļ���-2Ϊ�ļ�
	 * @param id        �����Ƶ��ļ���/�ļ�
	 * @param parentDir Ŀ���ļ�����
	 * @param isOne     �Ƿ�Ϊ��һ��
	 */
	@Transactional
	public void copyFolioAndFile(int userId,int typeId, int id, int parentDir, int isNotShare, boolean isOne) {
		if (typeId == 1) {// �ļ���
			int oldparentDir = id;
			Folio folio = this.folioMapper.selectOne(id);
			int endParentDir = folio.getParentDir();
			folio.setParentDir(parentDir);
			folio.setIsNotShare(isNotShare);

			int olduserId = folio.getUserId();
			folio.setUserId(userId);
			if (isOne) {
				this.getRname(folio, endParentDir == parentDir ? " - ����" : null);
			}

			this.folioMapper.createFolio(folio);

			List<Folio> folios = this.folioMapper.selectByUser(olduserId, oldparentDir, 1, isNotShare);
			List<FileInfo> files = this.fileInfoMapper.selectByUserId(olduserId, oldparentDir, 1, isNotShare);
			List<FolioInfoVo> lists = fileHelper.changeObj(folios, files);
			for (FolioInfoVo f : lists) {
				copyFolioAndFile(userId,f.getType(), f.getId(), folio.getId(), isNotShare);
			}

		} else {// �ļ�
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			fileInfo.setIsNotShare(isNotShare);
			int endParentDir = fileInfo.getParentDir();
			fileInfo.setParentDir(parentDir);
			FileInfo f2 = this.fileInfoMapper.selectOneByFileNmae(fileInfo.getUserId(), fileInfo.getParentDir(),
					fileInfo.getFileName());
			this.fileHelper.getRname(fileInfo, f2, endParentDir == parentDir ? " - ����" : null);
			String oldFileSrc = fileInfo.getFileSrc();
			StringBuffer newFilePath = new StringBuffer(oldFileSrc.substring(0, oldFileSrc.lastIndexOf("/") + 1));
			newFilePath.append(UUID.randomUUID());
			newFilePath.append(".");
			newFilePath.append(fileInfo.getFileType());
			fileInfo.setFileSrc(newFilePath.toString());// �ļ����ϴ���·��
			fileInfo.setUserId(userId);
			this.fileOption.copy(oldFileSrc, newFilePath.toString());
			this.fileInfoMapper.newFile(fileInfo);
		}
	}
	
	/**
	 * ���浽
	 */
	@Transactional
	public void copyFolioAndFile(int userId,int typeId, int id, int parentDir, int isNotShare) {
		if (typeId == 1) {// �ļ���
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

		} else {// �ļ�
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			fileInfo.setIsNotShare(isNotShare);
			int endParentDir = fileInfo.getParentDir();
			fileInfo.setParentDir(parentDir);
			FileInfo f2 = this.fileInfoMapper.selectOneByFileNmae(fileInfo.getUserId(), fileInfo.getParentDir(),
					fileInfo.getFileName());
			this.fileHelper.getRname(fileInfo, f2, endParentDir == parentDir ? " - ����" : null);
			String oldFileSrc = fileInfo.getFileSrc();
			StringBuffer newFilePath = new StringBuffer(oldFileSrc.substring(0, oldFileSrc.lastIndexOf("/") + 1));
			newFilePath.append(UUID.randomUUID());
			newFilePath.append(".");
			newFilePath.append(fileInfo.getFileType());
			fileInfo.setFileSrc(newFilePath.toString());// �ļ����ϴ���·��
			fileInfo.setUserId(userId);
			this.fileOption.copy(oldFileSrc, newFilePath.toString());
			this.fileInfoMapper.newFile(fileInfo);
		}
	}

	/**
	 * �����޸��ļ���Ϣ
	 * 
	 * @param folios
	 * @return
	 */
	@Transactional
	public int updateFolios(List<Folio> folios) {
		return this.folioMapper.updateFolios(folios);
	}

	/**
	 ** �������ļ�/�ļ���ɾ���������վ
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
	 ** �ļ���/�ļ�������ԭ-0929׼������
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
	 ** �ҵ��ҵĸ����������ļ����ж��ٸ���ɾ��
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
	 ** �ҵ�һ�����ļ���,�������ļ�,��Ϊ�´����ĸ��ļ���
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
	 ** ����ļ��������������showName��ֵ
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
	 ** ɾ���ļ����ļ���
	 * 
	 * @param id     �ļ�id���ļ���id
	 * @param typeId 1Ϊ�ļ��з���Ϊ�ļ�
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
