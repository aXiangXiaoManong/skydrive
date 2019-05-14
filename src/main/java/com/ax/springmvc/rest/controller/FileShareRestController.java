package com.ax.springmvc.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ax.springmvc.pojo.FileShare;
import com.ax.springmvc.pojo.User;
import com.ax.springmvc.service.FileShareService;
import com.ax.springmvc.service.FolioService;
import com.ax.springmvc.vo.FileShareSession;

@Controller
@ResponseBody
@RequestMapping("rest/fileShare")
public class FileShareRestController {
	@Autowired
	private FileShareService fileShareService;

	@Autowired
	private FolioService folioService;

	@RequestMapping(method = RequestMethod.POST)
	public Object shareFiles(HttpSession session, Integer shareDay, Integer[] userId, Integer[] id, Integer[] typeId) {
		int uId = ((User) session.getAttribute("user")).getId();
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.fileShareService.addShareFile(new FileShare(uId, 1, 0), shareDay, userId, id, typeId) > 0) {
			map.put("status", 200);
			map.put("msg", "分享成功");
		} else {
			map.put("status", 201);
			map.put("msg", "分享失败！");
		}
		return map;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Object sessionInfo(HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		List<FileShareSession> fileShares = this.fileShareService.queryAll(userId);
		return fileShares;
	}

	@RequestMapping(value = "/{beUserId}", method = RequestMethod.GET)
	public Object sessionRecord(@PathVariable Integer beUserId, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		List<FileShareSession> fileShares = this.fileShareService.queryByUserIdAndBeUserId(userId, beUserId);
		return fileShares;
	}

	@RequestMapping(value = "/fileShareDetail", method = RequestMethod.GET)
	public Object fileShareDetail(HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		return this.fileShareService.fileShareDetail(userId);
	}

	@RequestMapping(value = "/showShareFileInfo/{fileShareId}", method = RequestMethod.GET)
	public Object showShareFileInfo(@PathVariable Integer fileShareId, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		return this.fileShareService.showShareFileInfo(userId, fileShareId);
	}

	@RequestMapping(value = "/{folioId}/{fileShareId}", method = RequestMethod.POST)
	public Object saveToDisk(@PathVariable Integer folioId, @PathVariable Integer fileShareId, HttpSession session) {
		int userId = ((User) session.getAttribute("user")).getId();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.fileShareService.saveToDisk(folioId, fileShareId,userId);
			map.put("status", 200);
			map.put("msg", "保存成功");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "保存失败！");
			e.printStackTrace();
		}
		return map;
	}

	/**
	 ** 取消分享
	 * 
	 * @param fileShareId
	 * @return
	 */
	@RequestMapping(value = "/{fileShareId}", method = RequestMethod.DELETE)
	public Object delShareFile(@PathVariable int fileShareId) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.fileShareService.delShareFile(fileShareId);
			map.put("status", 200);
			map.put("msg", "分享链接已删除成功!");
		} catch (Exception e) {
			map.put("status", 201);
			map.put("msg", "分享链接删除失败!");
			e.printStackTrace();
		}
		return map;
	}
}
