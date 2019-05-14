package com.ax.springmvc.util.chcekfilename;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ax.springmvc.mapper.FolioMapper;
import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;
import com.ax.springmvc.vo.FolioInfoVo;

@Service
public class FileHelper {
	@Autowired
	private FolioMapper folioMapper;

	/**
	 * 检查文件重名问题更改showName的值
	 * 
	 * @param folioName
	 * @return
	 */
	public void getRname(FileInfo f, FileInfo f2, String str) {
		if (str != null) {
			if (f.getShowName().lastIndexOf(str) < 0) {
				String showName = f.getShowName().substring(0, f.getShowName().lastIndexOf("."));
				f.setFileName(showName + str);
				f.setShowName(showName + str);
			}
		}
		String hz = "." + f.getFileType();
		if (f2 == null) {
			int index2 = f.getFileName().lastIndexOf(")");

			if (index2 == f.getFileName().length() - 1 - 1 - f.getFileType().length()) {
				int index1 = f.getFileName().lastIndexOf("(");
				if (index1 + 1 < index2) {
					f.setFileName(f.getFileName().substring(0, index1) + hz);
				}
			}
		} else {
			f.setShowName(f2.getShowName());
			int index2 = f.getShowName().lastIndexOf(")");
			boolean flg = true;
			if (index2 == f.getShowName().length() - 1 - 1 - f.getFileType().length()) {
				int index1 = f.getShowName().lastIndexOf("(");
				if (index1 + 1 < index2) {
					String str_num = f.getShowName().substring(index1 + 1, index2);
					try {
						int rname = Integer.parseInt(str_num) + 1;
						String folioName = f.getShowName().substring(0, index1);
						f.setFileName(folioName + "." + hz);
						f.setShowName(folioName + "(" + rname + ")" + hz);
						flg = false;
					} catch (NumberFormatException e) {
					}
				}
			}
			if (flg) {
				f.setShowName(f.getShowName().substring(0, f.getShowName().lastIndexOf(".")) + "(2)" + hz);
			}
		}
	}

	/**
	 ** 将文件集合和文件夹集合合并成功一个集合
	 */
	@Transactional
	public List<FolioInfoVo> changeObj(List<Folio> list, List<FileInfo> list2) {
		List<FolioInfoVo> folioInfoVoList = new ArrayList<FolioInfoVo>();

		if (list != null)
			for (Folio f : list) {
				folioInfoVoList.add(new FolioInfoVo(f.getId(), f.getFolioName(), f.getShowName(), f.getParentDir(),
						f.getCreateTime(), f.getFileUpdateTime(), f.getUserId(), "-", f.getChildren(), "", "fa-folder",
						1));
			}

		if (list2 != null)
			for (FileInfo f : list2) {
				folioInfoVoList.add(new FolioInfoVo(f.getId(), f.getFileName(), f.getShowName(), f.getParentDir(),
						f.getFileUpdateTime(), f.getFileUpdateTime(), f.getUserId(), f.getSize(), 0, f.getFileType(),
						getClazz(f.getFileType()), 2));
			}

		return folioInfoVoList;
	}

	public FolioInfoVo changeObj(Folio f) {
		return new FolioInfoVo(f.getId(), f.getFolioName(), f.getShowName(), f.getParentDir(), f.getCreateTime(),
				f.getFileUpdateTime(), f.getUserId(), "-", f.getChildren(), "", "fa-folder", 1);
	}

	public FolioInfoVo changeObj(FileInfo f) {
		return new FolioInfoVo(f.getId(), f.getFileName(), f.getShowName(), f.getParentDir(), f.getFileUpdateTime(),
				f.getFileUpdateTime(), f.getUserId(), f.getSize(), 0, f.getFileType(), getClazz(f.getFileType()), 2);
	}

	/**
	 ** 更具文件类型得到前台页面的类样式
	 */
	private String getClazz(String str) {
		if (".jpg .bmp .png .gif .tiff .raw .jpeg".toLowerCase().indexOf(str.toLowerCase()) >= 0)
			return "fa-file-image-o";
		if (".mp3 .wma .wav .ra .aac .mid .ogg .m4a".toLowerCase().indexOf(str.toLowerCase()) >= 0)
			return "fa-file-audio-o";
		if (".mp4 .mpg .avi .rm .mov .asf .wmv .flv .3gp .awv .f4v".toLowerCase().indexOf(str.toLowerCase()) >= 0)
			return "fa-file-video-o";
		return "fa-file";
	}
}
