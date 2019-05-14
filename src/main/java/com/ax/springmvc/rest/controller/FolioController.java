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

	// ����ѯ
	@RequestMapping(value = "/{parentDir}", method = RequestMethod.GET)
	public Map<String, Object> selectByUser(@PathVariable Integer parentDir, String order, Integer isNotDel,
			HttpSession session, HttpServletRequest req) {
		int userId = ((User) session.getAttribute("user")).getId();// �û�id
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
	 ** ���ļ����Ͳ�ѯ�ļ�
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> selectByUser(String fileType, String order, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();// �û�id
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("files",
				this.fileHelper.changeObj(null, this.fileInfoService.selectByType(userId, fileType, order, 1)));
		map.put("catalog", new ArrayList<Folio>());
		return map;
	}

	/**
	 ** �õ�ָ��Ŀ¼�µ��ļ���
	 */
	@RequestMapping(value = "/getFolio", method = RequestMethod.GET)
	public List<FolioInfoVo> getFolio(Integer parentDir, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();// �û�id
		return this.fileHelper.changeObj(folioService.selectByUser(userId, parentDir, null, 1, 1), null);
	}

	/**
	 ** �����ļ���
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
			map.put("msg", "�����ɹ�!");
		} else {
			map.put("status", 201);
			map.put("msg", "����ʧ��!");
		}
		return map;
	}

	/**
	 ** ɾ��������վ�ļ��к��ļ�
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public Map<String, Object> delFolioAndFile(Integer[] id, Integer[] typeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.folioService.delFolioAndFile(typeId, id, 2);
			map.put("status", 200);
			map.put("msg", "ɾ���ɹ����ѽ������վ!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "ɾ��ʧ��!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** ��ԭ�ļ��к��ļ�
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
			map.put("msg", "��ԭ�ɹ�!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "��ԭʧ��!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** ������
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
			map.put("msg", "�������ɹ�!");
		} else {
			map.put("status", 201);
			map.put("msg", "������ʧ��!");
		}

		return map;
	}

	/**
	 ** �ļ���/�ļ������ƶ�
	 */
	@RequestMapping(value = "/moves", method = RequestMethod.PUT)
	public Map<String, Object> moveFolioAndFiles(Integer[] typeId, Integer[] id, Integer parentDir) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (typeId != null) {
			for (int i = 0; i < typeId.length; i++) {
				this.folioService.moveFolioAndFile(typeId[i], id[i], parentDir);
			}
			map.put("status", 200);
			map.put("msg", "�ļ��ƶ��ɹ�!");
		} else {
			map.put("status", 201);
			map.put("msg", "�ļ��ƶ�ʧ��!");
		}
		return map;
	}

	/**
	 ** �ļ���/�ļ���������
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
			map.put("msg", "�ļ����Ƴɹ�!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "�ļ�����ʧ��!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** �ļ���/�ļ�������ԭ
	 */
	@RequestMapping(value = "/restore", method = RequestMethod.POST)
	public Object restoreFolioOrFile(Integer[] typeId, Integer[] id) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			for (int i = 0; i < typeId.length; i++) {

			}
			map.put("status", 200);
			map.put("msg", "�ļ���ԭ�ɹ�!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "�ļ���ԭʧ��!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** �ļ���/�ļ�����ɾ��
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
			map.put("msg", "�ļ�����ɾ���ɹ�!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "�ļ�����ɾ��ʧ��!");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** ����
	 */
	@RequestMapping(value = "/listByLikeFileName/{fileName}", method = RequestMethod.GET)
	public Object listByLikeFileName(HttpSession session, @PathVariable String fileName) {
		int userId = ((User) session.getAttribute("user")).getId();// �û�id
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("files", this.fileHelper.changeObj(null, this.fileInfoService.listByLikeFileName(userId, fileName)));
		map.put("catalog", new ArrayList<Folio>());
		return map;
	}

}
