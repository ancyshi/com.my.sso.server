package com.my.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.util.GlobalSessions;

@RestController
@RequestMapping(value = "/app")
public class APPController {

	@RequestMapping(value = "/login")
	public String pageLogin(HttpServletRequest request) throws Exception {
		// 1.判定用户是否登录,这里解决了图中1过程，
		// 这里应该是局部session
		HttpSession session = request.getSession();
		if (session == null) {
			// 直接重定向到登录界面/page/login，图中的2
			redictToServer(request);
		}
		// 如果有session，判断是否合法
		if (GlobalSessions.getSession(session.getId()) == null) {
			// 不合法的话重定向到登录界面
			redictToServer(request);
		}

		// 如果有session，那么要判断session是否合法,/auth/verify

		// 合法的话直接跳到page1
		
		return "/LoginHtml";


	}

	private void redictToServer(HttpServletRequest request) {

	}

}
