package com.ax.springmvc.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ax.springmvc.pojo.FileInfo;
import com.ax.springmvc.pojo.Folio;
import com.ax.springmvc.service.FileInfoService;
import com.ax.springmvc.service.FolioService;
import com.ax.springmvc.util.chcekfilename.FileHelper;
import com.ax.springmvc.util.file.FileOption;
import com.ax.springmvc.util.file.ZipCompressorByAnt;
import com.ax.springmvc.vo.FolioInfoVo;

@Controller
@RequestMapping("/download")
public class DownFileController {
	@Autowired
	private FileInfoService fileInfoService;
	@Autowired
	private FolioService folioService;

	private FileHelper fileHelper = new FileHelper();
	private FileOption fileOption = new FileOption();

	// ��ͨjava�ļ����ط��������������п��
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void downloadFilesTest(@RequestParam Integer[] id, @RequestParam Integer[] typeId,
			HttpServletRequest request, HttpServletResponse res) throws IOException {
		List<ResponseEntity<byte[]>> fileList = new ArrayList<ResponseEntity<byte[]>>();
		String zipName = "";
		boolean flog = true;
		// ������Ҫ���ص��ļ�·���ļ���
		List<String> filePaths = new ArrayList<String>();
		for (int i = 0; i < typeId.length; i++) {
			if (typeId[i] == 1) {

			} else {
				FileInfo fileInfo = this.fileInfoService.selectOne(id[i]);
				if (flog) {
					zipName = fileInfo.getShowName().substring(0, fileInfo.getShowName().lastIndexOf("."));
					if (id.length > 1)
						zipName += "�ȶ���ļ�";
					zipName += ".zip";
					flog = false;
				}
				filePaths.add(fileInfo.getFileSrc());
			}
		}

		// �������ж���ļ�����ѹ����������������ļ�
		// ����ѹ���ļ���Ҫ�Ŀյ�zip��
		String zipBasePath = "E:\\studyInfo\\skyUpLoadFile\\dowonLoad";
		String zipFilePath = zipBasePath + File.separator + zipName;

		// ѹ���ļ�
		File zip = new File(zipFilePath);
		if (!zip.exists()) {
			zip.createNewFile();
		}
		// ����zip�ļ������
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		this.zipFile(zipBasePath, zipName, zipFilePath, filePaths, zos);
		zos.close();

		byte[] body = null;
		InputStream is = new FileInputStream(zip);
		body = new byte[is.available()];
		is.read(body);
		is.close();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-disposition", "attachment;filename=" + zipName);// �������ص�ѹ���ļ�����
		// res.setHeader("Content-disposition", "attachment;filename=" + zipName);//
		// �������ص�ѹ���ļ�����

		// ���������ļ�д���ͻ��ˣ�����ķ���ͬ�ϣ�ʹ�û��������
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
		byte[] buff = new byte[bis.available()];
		bis.read(buff);
		bis.close();

		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
		fileList.add(entity);
		// return null;
	}

	/**
	 * ѹ���ļ�
	 * 
	 * @param zipBasePath ��ʱѹ���ļ�����·��
	 * @param zipName     ��ʱѹ���ļ�����
	 * @param zipFilePath ��ʱѹ���ļ�����·��
	 * @param filePaths   ��Ҫѹ�����ļ�·������
	 * @throws IOException
	 */
	private String zipFile(String zipBasePath, String zipName, String zipFilePath, List<String> filePaths,
			ZipOutputStream zos) throws IOException {
		// ѭ����ȡ�ļ�·�����ϣ���ȡÿһ���ļ���·��
		for (String filePath : filePaths) {
			File inputFile = new File(filePath); // �����ļ�·�������ļ�
			if (inputFile.exists()) { // �ж��ļ��Ƿ����
				if (inputFile.isFile()) { // �ж��Ƿ������ļ��������ļ���
					// ������������ȡ�ļ�
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));

					// ���ļ�д��zip�ڣ������ļ����д��
					zos.putNextEntry(new ZipEntry(inputFile.getName()));

					// д���ļ��ķ�����ͬ��
					int size = 0;
					byte[] buffer = new byte[1024]; // ���ö�ȡ���ݻ����С
					while ((size = bis.read(buffer)) > 0) {
						zos.write(buffer, 0, size);
					}
					// �ر����������
					zos.closeEntry();
					bis.close();

				} else { // ������ļ��У���ʹ����ٵķ�����ȡ�ļ���д��zip
					try {
						File[] files = inputFile.listFiles();
						List<String> filePathsTem = new ArrayList<String>();
						for (File fileTem : files) {
							filePathsTem.add(fileTem.toString());
						}
						return zipFile(zipBasePath, zipName, zipFilePath, filePathsTem, zos);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String dload(@RequestParam Integer[] id, @RequestParam Integer[] typeId, HttpServletRequest request,
			HttpServletResponse res) throws IOException {
		String temporaryPath = "E:\\studyInfo\\skyUpLoadFile\\dowonLoad";// ��ʱ·��
		String nodeName = "file";// ��ʱ�ļ�����

		File file = new File(temporaryPath + File.separator + nodeName);
		if (!file.exists()) {
			file.mkdir();
		}

		// ����ļ��Ĵ��·��
		ZipCompressorByAnt zc = new ZipCompressorByAnt(temporaryPath + "/file.zip");
		// ��Ҫ������ļ�·��
		zc.compress(temporaryPath + File.separator + nodeName);
		// String contentType = "application/octet-stream";
		try {
			// ����ѹ����
			// download(request, res, "upload/file.zip",
			// contentType,encodeChineseDownloadFileName(request, "file.zip"));
			this.downLoad(temporaryPath + "/file.zip", "file.zip", request, res);
		} catch (Exception e) {
			// request.getSession().setAttribute("msg", "��������");
			e.printStackTrace();
		}
		// ���ԭѹ��������,��ɾ��
		File file1 = new File(temporaryPath + "/file.zip");
		if (file1.exists()) {
			file1.delete();
		}
		return "sky/index";
	}

	public void fileload(int userId, Integer[] id, Integer[] typeId, String temporaryPath, String nodeName)
			throws IOException {
		if (id != null && id.length > 0) {
			// if(!new File(temporaryPath).exists())
			// new File(temporaryPath).mkdir();

			// ������Ҫ���ص��ļ�·���ļ���
			Map<String, String> filePaths = new HashMap<String, String>();
			List<String> folioPaths = new ArrayList<String>();
			for (int i = 0; i < typeId.length; i++) {
				if (typeId[i] == 1) {// �ļ���
					Folio folio = this.folioService.selectOne(id[i]);
					// ��ǰ�ļ�������
					String currFileName = temporaryPath + File.separator + nodeName + File.separator
							+ folio.getShowName();
					folioPaths.add(currFileName);

					Integer[] ids = {};
					Integer[] typeIds = {};
					List<Folio> folios = this.folioService.selectByUser(userId, folio.getId(), null, 1, 1);
					for (int j = 0; j < folios.size(); j++) {
						ids[j] = folios.get(j).getId();
						typeIds[j] = 1;
					}
					List<FileInfo> fileInfos = this.fileInfoService.selectByUserId(userId, folio.getId(), null, 1, 1);
					for (int j2 = 0; j2 < fileInfos.size(); j2++) {
						ids[j2 + folios.size()] = folios.get(j2).getId();
						typeIds[j2 + folios.size()] = 2;
					}
					this.fileload(userId, ids, typeIds, temporaryPath + File.separator + nodeName, folio.getShowName());
				} else {// �ļ�
					FileInfo fileInfo = this.fileInfoService.selectOne(id[i]);
					filePaths.put(fileInfo.getFileSrc(), fileInfo.getShowName());
				}
			}

			for (String folioPath : folioPaths) {
				File inputFile = new File(folioPath);
				if (!inputFile.exists())
					inputFile.mkdir();// �����ļ���
			}

			for (Map.Entry<String, String> entry : filePaths.entrySet()) {
				File oldFile = new File(entry.getKey()); // �����ļ�·�������ļ�
				File newFile = new File(temporaryPath + File.separator + nodeName + File.separator + entry.getValue());
				newFile.createNewFile();
				this.fileOption.fileChannelCopy(oldFile, newFile);
			}

		}
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
}
