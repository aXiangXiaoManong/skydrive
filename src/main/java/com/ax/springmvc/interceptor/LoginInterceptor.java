package com.ax.springmvc.interceptor;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ax.springmvc.pojo.User;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!new File("E:\\studyInfo\\skyUpLoadFile").exists()) {
			new File("E:\\studyInfo\\skyUpLoadFile").mkdir();
		}
		/*if (request.getSession().getAttribute("user") == null) {
			//request.getSession().setAttribute("user",
					//new User(1, "zxl", "headImg/9b9580c3-dfee-4af1-b7ad-0dbcaf06b0a1.png", "15675101674", "123456"));
			request.getRequestDispatcher("redirectIndex").forward(request, response);
			return false;
		}*/
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
}
