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

	// 普通java文件下载方法，适用于所有框架
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public void downloadFilesTest(@RequestParam Integer[] id, @RequestParam Integer[] typeId,
			HttpServletRequest request, HttpServletResponse res) throws IOException {
		List<ResponseEntity<byte[]>> fileList = new ArrayList<ResponseEntity<byte[]>>();
		String zipName = "";
		boolean flog = true;
		// 创建需要下载的文件路径的集合
		List<String> filePaths = new ArrayList<String>();
		for (int i = 0; i < typeId.length; i++) {
			if (typeId[i] == 1) {

			} else {
				FileInfo fileInfo = this.fileInfoService.selectOne(id[i]);
				if (flog) {
					zipName = fileInfo.getShowName().substring(0, fileInfo.getShowName().lastIndexOf("."));
					if (id.length > 1)
						zipName += "等多个文件";
					zipName += ".zip";
					flog = false;
				}
				filePaths.add(fileInfo.getFileSrc());
			}
		}

		// 将附件中多个文件进行压缩，批量打包下载文件
		// 创建压缩文件需要的空的zip包
		String zipBasePath = "E:\\studyInfo\\skyUpLoadFile\\dowonLoad";
		String zipFilePath = zipBasePath + File.separator + zipName;

		// 压缩文件
		File zip = new File(zipFilePath);
		if (!zip.exists()) {
			zip.createNewFile();
		}
		// 创建zip文件输出流
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
		this.zipFile(zipBasePath, zipName, zipFilePath, filePaths, zos);
		zos.close();

		byte[] body = null;
		InputStream is = new FileInputStream(zip);
		body = new byte[is.available()];
		is.read(body);
		is.close();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-disposition", "attachment;filename=" + zipName);// 设置下载的压缩文件名称
		// res.setHeader("Content-disposition", "attachment;filename=" + zipName);//
		// 设置下载的压缩文件名称

		// 将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
		byte[] buff = new byte[bis.available()];
		bis.read(buff);
		bis.close();

		ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, HttpStatus.OK);
		fileList.add(entity);
		// return null;
	}

	/**
	 * 压缩文件
	 * 
	 * @param zipBasePath 临时压缩文件基础路径
	 * @param zipName     临时压缩文件名称
	 * @param zipFilePath 临时压缩文件完整路径
	 * @param filePaths   需要压缩的文件路径集合
	 * @throws IOException
	 */
	private String zipFile(String zipBasePath, String zipName, String zipFilePath, List<String> filePaths,
			ZipOutputStream zos) throws IOException {
		// 循环读取文件路径集合，获取每一个文件的路径
		for (String filePath : filePaths) {
			File inputFile = new File(filePath); // 根据文件路径创建文件
			if (inputFile.exists()) { // 判断文件是否存在
				if (inputFile.isFile()) { // 判断是否属于文件，还是文件夹
					// 创建输入流读取文件
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));

					// 将文件写入zip内，即将文件进行打包
					zos.putNextEntry(new ZipEntry(inputFile.getName()));

					// 写入文件的方法，同上
					int size = 0;
					byte[] buffer = new byte[1024]; // 设置读取数据缓存大小
					while ((size = bis.read(buffer)) > 0) {
						zos.write(buffer, 0, size);
					}
					// 关闭输入输出流
					zos.closeEntry();
					bis.close();

				} else { // 如果是文件夹，则使用穷举的方法获取文件，写入zip
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
		String temporaryPath = "E:\\studyInfo\\skyUpLoadFile\\dowonLoad";// 临时路径
		String nodeName = "file";// 临时文件名称

		File file = new File(temporaryPath + File.separator + nodeName);
		if (!file.exists()) {
			file.mkdir();
		}

		// 打包文件的存放路径
		ZipCompressorByAnt zc = new ZipCompressorByAnt(temporaryPath + "/file.zip");
		// 需要打包的文件路径
		zc.compress(temporaryPath + File.separator + nodeName);
		// String contentType = "application/octet-stream";
		try {
			// 导出压缩包
			// download(request, res, "upload/file.zip",
			// contentType,encodeChineseDownloadFileName(request, "file.zip"));
			this.downLoad(temporaryPath + "/file.zip", "file.zip", request, res);
		} catch (Exception e) {
			// request.getSession().setAttribute("msg", "暂无内容");
			e.printStackTrace();
		}
		// 如果原压缩包存在,则删除
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

			// 创建需要下载的文件路径的集合
			Map<String, String> filePaths = new HashMap<String, String>();
			List<String> folioPaths = new ArrayList<String>();
			for (int i = 0; i < typeId.length; i++) {
				if (typeId[i] == 1) {// 文件夹
					Folio folio = this.folioService.selectOne(id[i]);
					// 当前文件夹名称
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
				} else {// 文件
					FileInfo fileInfo = this.fileInfoService.selectOne(id[i]);
					filePaths.put(fileInfo.getFileSrc(), fileInfo.getShowName());
				}
			}

			for (String folioPath : folioPaths) {
				File inputFile = new File(folioPath);
				if (!inputFile.exists())
					inputFile.mkdir();// 创建文件夹
			}

			for (Map.Entry<String, String> entry : filePaths.entrySet()) {
				File oldFile = new File(entry.getKey()); // 根据文件路径创建文件
				File newFile = new File(temporaryPath + File.separator + nodeName + File.separator + entry.getValue());
				newFile.createNewFile();
				this.fileOption.fileChannelCopy(oldFile, newFile);
			}

		}
	}

	private void downLoad(String path, String realname, HttpServletRequest request, HttpServletResponse response) {
		// 得到要下载的文件
		File file = new File(path);
		// 如果文件不存在
		if (!file.exists()) {
			request.setAttribute("message", "您要下载的资源已被删除！！");
		} else {
			try {
				response.setContentType("application/octet-stream");
				response.setHeader("content-disposition",
						"attachment;filename=" + URLEncoder.encode(realname, "UTF-8"));
				response.setHeader("Content-Length", String.valueOf(10));
				FileInputStream in = new FileInputStream(path);// 读取要下载的文件,保存到文件输入流
				OutputStream out = response.getOutputStream();// 创建输出流
				byte[] buffer = new byte[1024];// 创建缓冲区
				int len = 0;
				// 循环将输入流中的内容读取到缓冲区当中
				while ((len = in.read(buffer)) > 0)
					out.write(buffer, 0, len);// 输出缓冲区的内容到浏览器,实现文件下载

				in.close();// 关闭文件输入流
				out.close();// 关闭输出流
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
