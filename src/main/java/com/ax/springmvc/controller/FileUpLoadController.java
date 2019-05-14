package com.ax.springmvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;
import com.ax.springmvc.pojo.User;
import com.ax.springmvc.service.FileInfoService;
import com.ax.springmvc.service.FolioService;
import com.ax.springmvc.service.UserService;
import com.ax.springmvc.util.chcekfilename.FileHelper;

@Controller
@RequestMapping("/upload")
public class FileUpLoadController {
	// private static final boolean String = false;

	@Autowired
	private FileInfoService fileInfoService;
	@Autowired
	private FolioService folioService;
	@Autowired
	private UserService userService;

	private FileHelper fileName = new FileHelper();

	/*
	 ** ���ϴ��ļ� - ����spring�ṩ���ϴ��ļ��ķ���
	 */
	@RequestMapping(value = "/springUpload", method = RequestMethod.POST)
	public String springUpload(HttpSession session, @RequestParam("html5uploader") MultipartFile[] multipartfiles)
			throws IllegalStateException, IOException {
		List<FileInfo> fileInfos = fileUpLoad(session, multipartfiles);
		if (this.fileInfoService.fileUpLoad(fileInfos) > 0) {
			session.setAttribute("skyMsg", "�ϴ��ɹ���");
		} else {
			session.setAttribute("skyMsg", "�ϴ�ʧ�ܣ�");
		}
		return "redirect:/home/index";
	}

	/**
	 ** ajax�ļ��ϴ�
	 */
	@ResponseBody
	@RequestMapping(value = "/springUploadAjax", method = RequestMethod.POST)
	public Object springUploadAjax(HttpSession session, @RequestParam("html5uploader") MultipartFile[] multipartfiles)
			throws IllegalStateException, IOException {
		List<FileInfo> fileInfos = fileUpLoad(session, multipartfiles);
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.fileInfoService.fileUpLoad(fileInfos) > 0) {
			map.put("status", 200);
			map.put("msg", "�ϴ��ɹ���");
		} else {
			map.put("status", 201);
			map.put("msg", "�ϴ�ʧ�ܣ�");
		}
		return map;
	}

	/**
	 ** ���ϴ��ļ���
	 */
	@RequestMapping(value = "/folioUpload", method = RequestMethod.POST)
	public String folioUpload(HttpSession session,
			@RequestParam("html5uploader") CommonsMultipartFile[] commonsMultipartFiles)
			throws IllegalStateException, IOException {

		List<FileInfo> fileInfos = FolioToUpLoad(session, commonsMultipartFiles);
		if (this.fileInfoService.fileUpLoad(fileInfos) > 0) {
			session.setAttribute("skyMsg", "�ϴ��ɹ���");
		} else {
			session.setAttribute("skyMsg", "�ϴ�ʧ�ܣ�");
		}
		return "redirect:/home/index";
	}

	/**
	 ** ajax�ϴ��ļ���
	 */
	@ResponseBody
	@RequestMapping(value = "/folioUploadAjax", method = RequestMethod.POST)
	public Object folioUploadAjax(HttpSession session,
			@RequestParam("html5uploader") CommonsMultipartFile[] commonsMultipartFiles)
			throws IllegalStateException, IOException {

		List<FileInfo> fileInfos = FolioToUpLoad(session, commonsMultipartFiles);
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.fileInfoService.fileUpLoad(fileInfos) > 0) {
			map.put("status", 200);
			map.put("msg", "�ϴ��ɹ���");
		} else {
			map.put("status", 201);
			map.put("msg", "�ϴ�ʧ�ܣ�");
		}
		return map;
	}

	/**
	 ** �ļ�����
	 */
	@RequestMapping(value = "/downLoad", method = RequestMethod.GET)
	public void fileDowmLoad(Integer[] id, Integer[] typeId, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		for (int i = 0; i < typeId.length; i++) {
			if (typeId[i] == 1) {

			} else {
				FileInfo fileInfo = this.fileInfoService.selectOne(id[i]);
				this.downLoad(fileInfo.getFileSrc(), fileInfo.getShowName(), request, response);
			}
		}
		// return "home/index";
	}

	private void downLoad(String path, String realname, HttpServletRequest request, HttpServletResponse response) {
		// �õ�Ҫ���ص��ļ�
		File file = new File(path);
		// ����ļ�������
		if (!file.exists()) {
			request.setAttribute("message", "��Ҫ���ص���Դ�ѱ�ɾ������");
		} else {
			try {
				response.setContentType("application/octet-stream");
				response.setHeader("content-disposition",
						"attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
				response.setHeader("Content-Length", String.valueOf(10));
				FileInputStream in = new FileInputStream(path);// ��ȡҪ���ص��ļ�,���浽�ļ�������
				OutputStream out = response.getOutputStream();// ���������
				byte[] buffer = new byte[1024];// ����������
				int len = 0;
				// ѭ�����������е����ݶ�ȡ������������
				while ((len = in.read(buffer)) > 0)
					out.write(buffer, 0, len);// ��������������ݵ������,ʵ���ļ�����

				in.close();// �ر��ļ�������
				out.close();// �ر������
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "downLoad/test", method = RequestMethod.GET)
	public Object toDownLoad(Integer[] id, Integer[] typeId, HttpServletRequest request)
			throws IOException, ServletException {
		List<ResponseEntity<byte[]>> fileList = new ArrayList<ResponseEntity<byte[]>>();
		for (int i = 0; i < typeId.length; i++) {
			if (typeId[i] == 1) {

			} else {
				FileInfo fileInfo = this.fileInfoService.selectOne(id[i]);
				File f = new File(fileInfo.getFileSrc());
				byte[] body = null;
				InputStream is = new FileInputStream(f);
				body = new byte[is.available()];
				is.read(body);
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "attchement;filename=" + f.getName());
				HttpStatus httpStatus = HttpStatus.OK;
				ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, httpStatus);
				fileList.add(entity);
			}
		}
		return fileList;
	}

	@ResponseBody
	@RequestMapping(value = "/updateUserInfo/{nickname}", method = RequestMethod.POST)
	public Object updateUserInfo(@PathVariable String nickname, HttpServletRequest req, HttpSession session)
			throws IllegalStateException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String savePath = "E:/studyInfo/skyUpLoadFile/";
		// ����ǰ�����ĳ�ʼ���� CommonsMutipartResolver ���ಿ�ֽ�������
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(session.getServletContext());
		if (multipartResolver.isMultipart(req)) {
			// ��request��ɶಿ��request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
			// ��ȡmultiRequest �����е��ļ���
			Iterator iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// һ�α��������ļ�
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String originalFilename = file.getOriginalFilename();
					String hz = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
					String fileName = "headImg/" + UUID.randomUUID() + "." + hz;

					String path = savePath + fileName;

					if (!new File(savePath + "headImg/").exists())
						new File(savePath + "headImg/").mkdir();
					// �ϴ�
					file.transferTo(new File(path));

					User user = (User) session.getAttribute("user");
					String oldHeadImg = user.getHeadImg();
					user.setHeadImg(fileName);
					user.setNickname(nickname);
					new File(savePath + oldHeadImg).delete();// �ڰ���������ɾ��
					if (this.userService.updateUser(user) > 0) {
						System.out.println(((User) session.getAttribute("user")).getHeadImg());
						map.put("status", 200);
						map.put("msg", "�޸ĳɹ���");
						map.put("user", user);
					} else {
						map.put("status", 201);
						map.put("msg", "�޸�ʧ�ܣ�");
					}
				}
			}
		}
		return map;
	}

	// �õ�һ�����ļ���
	private Folio checkFilePath(List<Folio> folios, String path, int parentDir, int userId) {
		if (folios.size() < 1) {
			Folio f = getFolio(path, parentDir, userId);
			folios.add(f);
			return f;
		} else {
			for (Folio f : folios) {
				if (path.lastIndexOf("/") > 0 && path.substring(0, path.lastIndexOf("/")).equals(f.getFolioName())) {
					parentDir = f.getId();// ���ĸ��ļ��е�Id
				}
			}
			for (Folio f : folios) {
				if (f.getFolioName().equals(path)) {
					return f;// �Ƿ��������ļ�
				}
			}
			Folio f = getFolio(path.substring(path.lastIndexOf("/") + 1), parentDir, userId);
			f.setFolioName(path);// �����ļ������Ƴ��֣��汲�ļ������뵱ǰ�ļ�����ͬ������������ĳ�·����
			folios.add(f);
			return f;
		}
	}

	// �����ļ���ȥ���ݿ�
	private Folio getFolio(String folioName, int parentDir, int userId) {
		Folio folio = new Folio(parentDir, folioName, userId, 1);
		this.folioService.createFolio(folio);
		return folio;
	}

	// �ļ��ϴ�
	private List<FileInfo> fileUpLoad(HttpSession session, MultipartFile[] multipartfiles) throws IOException {
		int userId = ((User) session.getAttribute("user")).getId();
		Integer parentDir = Integer.parseInt(session.getAttribute("skyParentDir") + "");
		String savePath = "E:/studyInfo/skyUpLoadFile/";
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
		if (multipartfiles != null && multipartfiles.length != 0) {
			if (null != multipartfiles && multipartfiles.length > 0) {
				// �����������ļ�
				for (MultipartFile file : multipartfiles) {
					String originalFilename = file.getOriginalFilename();
					String hz = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
					String fileName = UUID.randomUUID() + "." + hz;
					String path = savePath + fileName;
					file.transferTo(new File(path));

					FileInfo fileinfo = new FileInfo(userId, originalFilename, path, parentDir, hz, file.getSize() + "",
							1, 1);
					FileInfo f2 = this.fileInfoService.selectOneByFileNmae(fileinfo.getUserId(),
							fileinfo.getParentDir(), fileinfo.getFileName());
					this.fileName.getRname(fileinfo, f2, null);
					fileInfos.add(fileinfo);
				}
			}
		}
		return fileInfos;
	}

	// �ϴ��ļ���
	private List<FileInfo> FolioToUpLoad(HttpSession session, CommonsMultipartFile[] commonsMultipartFiles)
			throws IOException {
		String savePath = "E:/studyInfo/skyUpLoadFile/";
		List<Folio> parentsList = new ArrayList<Folio>();// �ļ���
		int userId = ((User) session.getAttribute("user")).getId();
		Integer parentDir = Integer.parseInt(session.getAttribute("skyParentDir") + "");
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();// �ļ�������

		if (commonsMultipartFiles != null && commonsMultipartFiles.length > 0) {
			for (CommonsMultipartFile com : commonsMultipartFiles) {
				FileItem fitem = com.getFileItem();
				String originalFilename = com.getOriginalFilename();
				String hz = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
				String fileName = UUID.randomUUID() + "." + hz;
				String path = savePath + fileName;
				com.transferTo(new File(path));

				String fileNmae = fitem.getName();
				String parentsPath = fileNmae.substring(0, fileNmae.lastIndexOf("/"));

				Folio folio = checkFilePath(parentsList, parentsPath, parentDir, userId);
				FileInfo fileInfo = new FileInfo(userId, originalFilename, path, folio.getId(), hz, com.getSize() + "",
						1, 1);
				FileInfo f2 = this.fileInfoService.selectOneByFileNmae(fileInfo.getUserId(), fileInfo.getParentDir(),
						fileInfo.getFileName());
				this.fileName.getRname(fileInfo, f2, null);
				fileInfos.add(fileInfo);
			}
		}
		return fileInfos;
	}
}
