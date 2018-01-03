package com.my.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.my.util.GlobalSessions;

@RestController
@RequestMapping(value = "/sso")
public class SSOController {
	/*
	 * 此接口主要接受来自应用系统的认证请求，此时，returnURL参数需加上，用以向认证中心标识是哪个应用系统，以及返回该应用的URL。
	 * 如用户没有登录，应用中心向浏览器用户显示登录页面。 如已登录，则产生临时令牌token，并重定向回该系统。上面登录时序交互图中的2和此接口有关。
	 * 
	 * 当然，该接口也同时接受用户直接向认证中心登录，此时没有returnURL参数，认证中心直接返回登录页面
	 * 
	 * 这个接口是应用系统与认证中心之间的通信，作用 1、
	 */
	@RequestMapping(value = "/page/login")
	public Long pageLogin(HttpServletRequest request) throws Exception {
		// 1.判定用户是否登录
		HttpSession session = request.getSession();
		if (session == null) {
			// 直接重定向到登录界面
		}
		// 如果有session，判断是否合法
		if (GlobalSessions.getSession(session.getId()) == null) {
			// 不合法的话重定向到登录界面
		}
		// 1.1 如果没有登录，向浏览器显示登录页面

		// 1.2 如果已经登录，则产生临时令牌token,并重定向回应用系统

		return null;
	}

	/*
	 * 说明： 处理浏览器用户登录认证请求。如带有returnURL参数，认证通过后，将产生临时认证令牌token，并携带此token重定向回系统。
	 * 如没有带returnURL参数
	 * ，说明用户是直接从认证中心发起的登录请求，认证通过后，返回认证中心首页提示用户已登录。上面登录时序交互图中的3和此接口有关。
	 */

	@RequestMapping(value = "/auth/login")
	public Long authLogin(JSONObject reqObj) throws Exception {
		// 1.判定用户是否登录

		// 1.1 如果没有登录，向浏览器显示登录页面

		// 1.2 如果已经登录，则产生临时令牌token,并重定向回应用系统

		return null;
	}

	/*
	 * 说明：认证应用系统来的token是否有效，如有效，应用系统向认证中心注册，同时认证中心会返回该应用系统登录用户的相关信息，如ID,username等
	 * 。上面登录时序交互图中的4和此接口有关。
	 */
	@RequestMapping(value = "/auth/verify")
	public Long authVerify(JSONObject reqObj) throws Exception {
		// 1.判定用户是否登录

		// 1.1 如果没有登录，向浏览器显示登录页面

		// 1.2 如果已经登录，则产生临时令牌token,并重定向回应用系统

		return null;
	}

	/*
	 * 说明：登出接口处理两种情况，一是直接从认证中心登出，一是来自应用重定向的登出请求。这个根据gId来区分，无gId参数说明直接从认证中心注销，有，
	 * 说明从应用中来。接口首先取消当前全局登录会话，其次根据注册的已登录应用，通知它们进行登出操作。上面登出时序交互图中的2和4与此接口有关。
	 */
	@RequestMapping(value = "/auth/logout")
	public Long authLogout(JSONObject reqObj) throws Exception {
		// 1.判定用户是否登录

		// 1.1 如果没有登录，向浏览器显示登录页面

		// 1.2 如果已经登录，则产生临时令牌token,并重定向回应用系统

		return null;
	}
}
