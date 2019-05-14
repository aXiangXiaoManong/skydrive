package com.ax.springmvc.rest.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;
import com.ax.springmvc.pojo.User;
import com.ax.springmvc.service.FileInfoService;
import com.ax.springmvc.service.FolioService;
import com.ax.springmvc.util.chcekfilename.FileHelper;
import com.ax.springmvc.vo.FolioInfoVo;

@RestController
@RequestMapping("/rest/folio")
public class FolioController {
	@Autowired
	private FolioService folioService;
	@Autowired
	private FileInfoService fileInfoService;

	private FileHelper fileHelper = new FileHelper();

	// 主查询
	@RequestMapping(value = "/{parentDir}", method = RequestMethod.GET)
	public Map<String, Object> selectByUser(@PathVariable Integer parentDir, String order, Integer isNotDel,
			HttpSession session, HttpServletRequest req) {
		int userId = ((User) session.getAttribute("user")).getId();// 用户id
		isNotDel = isNotDel == null ? 1 : isNotDel;
		session.setAttribute("skyParentDir", parentDir);

		List<FolioInfoVo> folioInfoVos = this.fileHelper.changeObj(
				folioService.selectByUser(userId, parentDir, order, isNotDel, 1),
				fileInfoService.selectByUserId(userId, parentDir, order, isNotDel, 1));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", folioInfoVos);
		map.put("catalog", folioService.getFolios(userId, parentDir));
		return map;
	}

	/**
	 ** 按文件类型查询文件
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> selectByUser(String fileType, String order, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();// 用户id
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("files",
				this.fileHelper.changeObj(null, this.fileInfoService.selectByType(userId, fileType, order, 1)));
		map.put("catalog", new ArrayList<Folio>());
		return map;
	}

	/**
	 ** 得到指定目录下的文件夹
	 */
	@RequestMapping(value = "/getFolio", method = RequestMethod.GET)
	public List<FolioInfoVo> getFolio(Integer parentDir, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();// 用户id
		return this.fileHelper.changeObj(folioService.selectByUser(userId, parentDir, null, 1, 1), null);
	}

	/**
	 ** 创建文件夹
	 * 
	 * @param parentDir
	 * @param folioName
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public Map<String, Object> createFolio(Integer parentDir, String folioName, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		Map<String, Object> map = new HashMap<String, Object>();

		if (this.folioService.createFolio(new Folio(parentDir, folioName, userId, 1)) > 0) {
			map.put("status", 200);
			map.put("msg", "创建成功!");
		} else {
			map.put("status", 201);
			map.put("msg", "创建失败!");
		}
		return map;
	}

	/**
	 ** 删除到回收站文件夹和文件
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Map<String, Object> delFolioAndFile(Integer[] id, Integer[] typeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.folioService.delFolioAndFile(typeId, id, 2);
			map.put("status", 200);
			map.put("msg", "删除成功，已进入回收站!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "删除失败!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** 还原文件夹和文件
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.PUT)
	public Map<String, Object> restoreFolioAndFile(Integer[] id, Integer[] typeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			/*
			 * for (int i = 0; i < typeId.length; i++) {
			 * this.folioService.restoreFolioOrFile(typeId[i], id[i]); }
			 */
			this.folioService.delFolioAndFile(typeId, id, 1);
			map.put("status", 200);
			map.put("msg", "还原成功!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "还原失败!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** 重命名
	 */
	@RequestMapping(value = "/{typeId}", method = RequestMethod.PUT)
	public Object updateFolioAndFile(String fileName, Integer parentDir, Integer id, @PathVariable Integer typeId,
			HttpServletRequest req) {
		Map<String, Object> map = new HashMap<String, Object>();
		int res = 0;
		if (typeId == 1) {
			Folio folio = folioService.selectOne(id);
			folio.setFolioName(fileName);
			this.folioService.getRname(folio, null);
			res = this.folioService.updateFolio(folio);
		} else {
			FileInfo fileInfo = fileInfoService.selectOne(id);
			fileInfo.setFileName(fileName);
			fileInfo.setShowName(fileName);
			this.fileHelper.getRname(fileInfo, null, null);
			res = this.fileInfoService.updateFileInfo(fileInfo);
		}
		if (res > 0) {
			map.put("status", 200);
			map.put("msg", "重命名成功!");
		} else {
			map.put("status", 201);
			map.put("msg", "重命名失败!");
		}

		return map;
	}

	/**
	 ** 文件夹/文件批量移动
	 */
	@RequestMapping(value = "/moves", method = RequestMethod.PUT)
	public Map<String, Object> moveFolioAndFiles(Integer[] typeId, Integer[] id, Integer parentDir) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (typeId != null) {
			for (int i = 0; i < typeId.length; i++) {
				this.folioService.moveFolioAndFile(typeId[i], id[i], parentDir);
			}
			map.put("status", 200);
			map.put("msg", "文件移动成功!");
		} else {
			map.put("status", 201);
			map.put("msg", "文件移动失败!");
		}
		return map;
	}

	/**
	 ** 文件夹/文件批量复制
	 */
	@RequestMapping(value = "/copys", method = RequestMethod.POST)
	public Map<String, Object> copyFolioAndFiles(Integer[] typeId, Integer[] id, Integer parentDir,
			HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (int i = 0; i < typeId.length; i++) {
				this.folioService.copyFolioAndFile(userId, typeId[i], id[i], parentDir, 1, true);
			}
			map.put("status", 200);
			map.put("msg", "文件复制成功!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "文件复制失败!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** 文件夹/文件批量还原
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public Object restoreFolioOrFile(Integer[] typeId, Integer[] id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (int i = 0; i < typeId.length; i++) {

			}
			map.put("status", 200);
			map.put("msg", "文件还原成功!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "文件还原失败!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** 文件夹/文件彻底删除
	 */
	@RequestMapping(value = "/{id}/{typeId}", method = RequestMethod.DELETE)
	public Object delFolioOrFile(@PathVariable Integer id, @PathVariable Integer typeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<String> filePath = new ArrayList<String>();
			this.folioService.delFolioOrFile(id, typeId, false, 1, filePath);
			for (String str : filePath) {
				File file = new File(str);
				if (file.exists())
					file.delete();
			}
			map.put("status", 200);
			map.put("msg", "文件彻底删除成功!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "文件彻底删除失败!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** 搜索
	 */
	@RequestMapping(value = "/listByLikeFileName/{fileName}", method = RequestMethod.GET)
	public Object listByLikeFileName(HttpSession session, @PathVariable String fileName) {
		int userId = ((User) session.getAttribute("user")).getId();// 用户id
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", this.fileHelper.changeObj(null, this.fileInfoService.listByLikeFileName(userId, fileName)));
		map.put("catalog", new ArrayList<Folio>());
		return map;
	}

}
