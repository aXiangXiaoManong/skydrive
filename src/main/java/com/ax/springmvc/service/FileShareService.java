package com.ax.springmvc.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ax.springmvc.mapper.FileInfoMapper;
import com.ax.springmvc.mapper.FileShareMapper;
import com.ax.springmvc.mapper.FolioMapper;
import com.ax.springmvc.mapper.ShareFileMapper;
import com.ax.springmvc.mapper.ShareUserMapper;
import com.ax.springmvc.mapper.UserMapper;
import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.FileShare;
import com.ax.springmvc.pojo.Folio;
import com.ax.springmvc.pojo.ShareFile;
import com.ax.springmvc.pojo.ShareUser;
import com.ax.springmvc.pojo.User;
import com.ax.springmvc.util.chcekfilename.FileHelper;
import com.ax.springmvc.util.file.FileOption;
import com.ax.springmvc.util.orderby.FileShareSessionTimeDesc;
import com.ax.springmvc.util.orderby.FileShareTimeAsc;
import com.ax.springmvc.util.orderby.FileShareTimeDesc;
import com.ax.springmvc.vo.FileShareSession;
import com.ax.springmvc.vo.FileShareVo;
import com.ax.springmvc.vo.FolioInfoVo;

@Service
public class FileShareService {
	@Autowired
	private FileShareMapper fileShareMapper;
	@Autowired
	private ShareUserMapper shareUserMapper;

	@Autowired
	private ShareFileMapper shareFileMapper;

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private FileInfoMapper fileInfoMapper;
	@Autowired
	private FolioMapper folioMapper;
	@Autowired
	private FolioService folioService;

	private FileHelper fileHelper = new FileHelper();
	private FileOption fileOption = new FileOption();

	/**
	 ** 分享多个文件
	 */
	@Transactional
	public int addShareFile(FileShare fileShare, Integer shareDay, Integer[] userId, Integer[] id, Integer[] typeId) {
		try {
			this.fileShareMapper.addShareFile(fileShare, shareDay);// 添加分享记录
			List<ShareUser> shareUsers = new ArrayList<ShareUser>();// 添加被分享人
			for (Integer itr : userId)
				shareUsers.add(new ShareUser(itr, fileShare.getId()));
			this.shareUserMapper.addShareUsers(shareUsers);

			List<ShareFile> shareFiles = new ArrayList<ShareFile>();
			for (int i = 0; i < typeId.length; i++) {// 分享的文件夹或文件
				this.shareFolioAndFile(typeId[i], id[i], 0, 2, shareFiles, true);
				shareFiles.get(i).setFileShareId(fileShare.getId());
			}
			this.shareFileMapper.addShareFiles(shareFiles);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 ** 得到该用户的分享明细
	 * 
	 * @param userId
	 * @return
	 */
	public List<FileShareVo> fileShareDetail(int userId) {
		List<FileShareVo> fileShareVos = new ArrayList<FileShareVo>();
		List<FileShare> fileShares = this.fileShareMapper.queryByUserIdAndIsEff(userId, 1);// 该用户所有的分享记录
		for (FileShare f : fileShares) {
			List<ShareFile> shareFiles = this.shareFileMapper.queryByFileShareIdOrderFolioType(f.getId());
			List<FolioInfoVo> folioInfoVos = this.getFolioInfoVoList(shareFiles);
			fileShareVos.add(new FileShareVo(f.getId(), f.getUserId(), f.getIsNotEffective(), f.getSharingTime(),
					f.getOutTime(), f.getDownloadNum(), folioInfoVos));
		}
		return fileShareVos;
	}

	/**
	 ** 查看分享的文件信息
	 * 
	 * @return
	 */
	public List<FolioInfoVo> showShareFileInfo(int userId, int fileShareId) {
		List<FolioInfoVo> folioInfoVos = new ArrayList<FolioInfoVo>();
		List<ShareFile> shareFiles = this.shareFileMapper.queryByFileShareIdOrderFolioType(fileShareId);
		for (ShareFile f : shareFiles) {
			if (f.getFolioType() == 1)
				folioInfoVos.add(this.fileHelper.changeObj(this.folioMapper.selectOne(f.getFileId())));
			else
				folioInfoVos.add(this.fileHelper.changeObj(this.fileInfoMapper.selectOne(f.getFileId())));
		}
		return folioInfoVos;
	}

	/**
	 ** 得到该用户的会话记录
	 * 
	 * @param userId
	 * @param beUserId
	 * @return
	 */
	public List<FileShareSession> queryAll(int userId) {
		Map<Integer, FileShareSession> fileShareSessions = new HashMap<Integer, FileShareSession>();// 储存会话记录的集合

		List<FileShare> fileShares = this.fileShareMapper.queryByToUserId(userId);
		List<FileShare> fileShares2 = this.fileShareMapper.queryByBeUserId(userId);
		fileShares.addAll(fileShares2);

		fileShares.sort(new FileShareTimeDesc());
		List<Integer> fileSharIds = new ArrayList<Integer>();// 储存分享记录的Id

		Map<Integer, Integer> beuserIds = new HashMap<Integer, Integer>();
		for (FileShare fileShare : fileShares) {
			fileSharIds.add(fileShare.getId());
			beuserIds.put(fileShare.getUserId(), fileShare.getUserId());
		}
		List<ShareUser> shareUsers = this.shareUserMapper.queryByFileShareId(fileSharIds);// 被分享的用户
		for (ShareUser shareUser : shareUsers) {
			beuserIds.put(shareUser.getBeUserId(), shareUser.getBeUserId());
		}
		beuserIds.remove(userId);
		List<Integer> userIds = beuserIds.entrySet().stream().sorted(Comparator.comparing(e -> e.getKey()))
				.map(e -> new Integer(e.getValue())).collect(Collectors.toList());
		// 按分享记录Id分组,得到每一次分享的用户
		List<User> users = this.userMapper.queryByUserIds(userIds);// 用户集合
		for (User u : users) {
			for (FileShare f : fileShares) {
				if (u.getId() == f.getUserId() || u.getId() == f.getShareUser().getBeUserId())
					if (!fileShareSessions.containsKey(u.getId())) {
						fileShareSessions.put(u.getId(),
								new FileShareSession(f.getId(), f.getUserId(), f.getIsNotEffective(),
										f.getSharingTime(), f.getOutTime(), f.getDownloadNum(), u.getId(),
										u.getNickname(), u.getHeadImg(), u.getPhone(), u.getPwd()));
						break;
					}
			}
		}
		// 将Map转成List集合
		List<FileShareSession> list = fileShareSessions.entrySet().stream()
				.sorted(Comparator.comparing(e -> e.getKey())).map(e -> new FileShareSession(e.getValue()))
				.collect(Collectors.toList());
		list.sort(new FileShareSessionTimeDesc());
		return list;
	}

	/**
	 ** 当前用户和另一为会话的用户
	 */
	public List<FileShareSession> queryByUserIdAndBeUserId(int userId, int beUserId) {
		List<FileShareSession> fileShareSessions = new ArrayList<FileShareSession>();// 储存会话记录的集合
		List<FileShare> fileShares = this.fileShareMapper.queryByUserIdAndBeUserId(userId, beUserId);// 单前用户和会话用户的文件分享记录
		fileShares.sort(new FileShareTimeAsc());// 按时间升序排序

		List<Integer> fileShareIds = new ArrayList<Integer>();// 储存文件分享的主键
		for (FileShare f : fileShares) {
			fileShareIds.add(f.getId());
		}
		List<ShareFile> shareFiles = this.shareFileMapper.queryByFileShareId(fileShareIds);// 分享文件的集合
		List<FolioInfoVo> folioInfoVos = getFolioInfoVoList(shareFiles);

		// 更具文件分享记录的主键进分组
		Map<Integer, List<ShareFile>> shareFilesa = shareFiles.stream()
				.collect(Collectors.groupingBy(ShareFile::getFileShareId));

		User currentUser = this.userMapper.selectOne(userId);// 当前前用户
		User beUser = this.userMapper.selectOne(beUserId);// 被分享的一个用户
		for (FileShare fs : fileShares) {
			List<FolioInfoVo> folioInfoVoList = new ArrayList<FolioInfoVo>();// 储存每一次分享的文件夹和文件
			List<ShareFile> shareFileLists = shareFilesa.get(fs.getId());
			if (shareFileLists != null)
				for (ShareFile shareFile : shareFileLists) {
					for (FolioInfoVo f : folioInfoVos) {
						if (f.getType() == shareFile.getFolioType() && f.getId() == shareFile.getFileId()) {
							folioInfoVoList.add(f);
							// folioInfoVos.remove(f);
						}
					}
				}
			User u = fs.getUserId() == userId ? currentUser : beUser;
			FileShareSession fileShareSession = new FileShareSession(fs.getId(), fs.getUserId(), fs.getIsNotEffective(),
					fs.getSharingTime(), fs.getOutTime(), fs.getDownloadNum(), u.getId(), u.getNickname(),
					u.getHeadImg(), u.getPhone(), u.getPwd());
			fileShareSession.setFolioInfoVos(folioInfoVoList);
			fileShareSessions.add(fileShareSession);
		}
		return fileShareSessions;
	}

	private List<FolioInfoVo> getFolioInfoVoList(List<ShareFile> shareFiles) {
		List<Folio> folios = new ArrayList<Folio>();// 文件夹集合
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();// 文件集合
		for (ShareFile f : shareFiles) {
			if (f.getFolioType() == 1)
				folios.add(this.folioMapper.selectOne(f.getFileId()));
			else
				fileInfos.add(this.fileInfoMapper.selectOne(f.getFileId()));
		}
		List<FolioInfoVo> folioInfoVos = fileHelper.changeObj(folios, fileInfos);
		return folioInfoVos;
	}

	/**
	 ** 文件和文件夹分享操作
	 * 
	 * @param typeId    1为文件夹-2为文件
	 * @param id        被复制的文件夹/文件
	 * @param parentDir 目标文件夹下
	 * @param isOne     是否为第一次
	 */
	@Transactional
	private void shareFolioAndFile(int typeId, int id, int parentDir, int isNotShare, List<ShareFile> shareFiles,
			boolean isOne) {
		if (typeId == 1) {// 文件夹
			int oldparentDir = id;
			Folio folio = this.folioMapper.selectOne(id);
			folio.setParentDir(parentDir);
			folio.setIsNotShare(isNotShare);

			this.folioMapper.createFolio(folio);
			if (isOne)
				shareFiles.add(new ShareFile(typeId, folio.getId(), 0));

			List<Folio> folios = this.folioMapper.selectByUser(folio.getUserId(), oldparentDir, 1, 1);
			List<FileInfo> files = this.fileInfoMapper.selectByUserId(folio.getUserId(), oldparentDir, 1, 1);
			List<FolioInfoVo> lists = this.fileHelper.changeObj(folios, files);
			for (FolioInfoVo f : lists) {
				shareFolioAndFile(f.getType(), f.getId(), folio.getId(), isNotShare, shareFiles, false);
			}
		} else {// 文件
			FileInfo fileInfo = this.fileInfoMapper.selectOne(id);
			fileInfo.setIsNotShare(isNotShare);
			fileInfo.setParentDir(parentDir);
			String oldFileSrc = fileInfo.getFileSrc();
			StringBuffer newFilePath = new StringBuffer(oldFileSrc.substring(0, oldFileSrc.lastIndexOf("/") + 1));
			newFilePath.append(UUID.randomUUID());
			newFilePath.append(".");
			newFilePath.append(fileInfo.getFileType());
			fileInfo.setFileSrc(newFilePath.toString());// 文件的上传新路径

			this.fileOption.copy(oldFileSrc, newFilePath.toString());
			this.fileInfoMapper.newFile(fileInfo);
			if (isOne)
				shareFiles.add(new ShareFile(typeId, fileInfo.getId(), 0));
		}
	}

	/**
	 ** 保存到自己的硬盘
	 */
	@Transactional
	public synchronized void saveToDisk(int folioId, int fileShareId, int userId) {
		List<ShareFile> shareFiles = this.shareFileMapper.queryByFileShareIdOrderFolioType(fileShareId);
		List<FolioInfoVo> folioInfoVos = this.getFolioInfoVoList(shareFiles);
		for (FolioInfoVo f : folioInfoVos) {
			this.folioService.copyFolioAndFile(userId, f.getType(), f.getId(), folioId, 1);
		}
		FileShare fileShare = this.fileShareMapper.queryOne(fileShareId);
		fileShare.setDownloadNum(fileShare.getDownloadNum() + 1);
		this.fileShareMapper.update(fileShare);
	}

	/**
	 ** 取消分享
	 */
	@Transactional
	public void delShareFile(int fileShareId) {
		List<ShareFile> shareFiles = this.shareFileMapper.queryByFileShareIdOrderFolioType(fileShareId);
		List<FolioInfoVo> folioInfoVos = this.getFolioInfoVoList(shareFiles);
		List<String> filePath = new ArrayList<String>();
		for (FolioInfoVo f : folioInfoVos) {
			this.folioService.delFolioOrFile(f.getId(), f.getType(), true, 2, filePath);
		}
		for (String str : filePath) {
			File file = new File(str);
			if (file.exists())
				file.delete();
		}
		FileShare fileShare = this.fileShareMapper.queryOne(fileShareId);
		fileShare.setIsNotEffective(2);
		this.fileShareMapper.update(fileShare);
	}
}
