package com.my.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.my.util.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.my.cache.CookieCache;
import com.my.cache.GlobalSessionCache;
import com.my.dao.UsersJPA;
import com.my.factory.AbstractFactory;
import com.my.factory.GlobalSession;
import com.my.factory.SessionFactory;
import com.my.model.TokenInfo;
import com.my.model.Users;
import com.my.util.TokenUtil;
import com.my.util.ToolsUtil;

@Controller
@RequestMapping(value = "/server")
public class SSOController {

	@Resource
	private UsersJPA usersJPA;

	@Resource
	private TokenUtil tokenUtil;

	@Resource
	private CookieCache cookieCache;

	@Resource
	private GlobalSessionCache globalSessionCache;

	String token = UUID.randomUUID().toString();

	private AbstractFactory abstractFactory = new SessionFactory();

	@RequestMapping(value = "/page/login", method = RequestMethod.GET)
	public String pageLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String globalSessionId = ToolsUtil.getCookieValueByName(request, "globalSessionId");
		String cookieIdStr = "";
		if (globalSessionId != null) {
			cookieIdStr = cookieCache.getCookie(globalSessionId);
		}

		// 如果没有全局会话，或者已经登出了，就显示登录页面
		if (null == globalSessionId || !cookieIdStr.equals("true")) {
			// 重定向之后会执行下面的语句，因此加个return
			model.addAttribute("returnURL", request.getParameter("returnURL"));
			return "/login";
		}
		GlobalSession globalSession = globalSessionCache.cacheable(globalSessionId);
		// 1.2 如果已经登录，则产生临时令牌token
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalSessionId(globalSession.getSessionIdStr());
		tokenInfo.setUserId(globalSession.getUserId());
		tokenInfo.setUserName(globalSession.getUserName());
		tokenInfo.setSsoClient("ef");
		tokenUtil.setToken(token, tokenInfo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("returnURL", request.getParameter("returnURL"));
		map.put("token", token);
		String redirectURL = ToolsUtil.addressAppend("localhost", "8078", "/client/auth/check", map);
		response.sendRedirect(redirectURL);

		return null;

	}

	@RequestMapping(value = "/auth/login", method = RequestMethod.POST)
	public String authLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1、把用户的信息解码后，认证用户
		Users user = usersJPA.findByUserNameAndPassWord(SecurityUtils.getBase64(request.getParameter("userName")),
				SecurityUtils.getBase64((String) request.getParameter("passWord")));
		if (user == null) {
			// 没有注册过的用户，显示注册界面
			return "/register";
		}

		// 2、认证通过，产生全局的sessionId
		HttpSession session = request.getSession(true);
		session.setAttribute("username", user.getUserName());
		session.setAttribute("password", user.getPassWord());

		GlobalSession globalSession = (GlobalSession) abstractFactory.generateAbstractSession();
		globalSession.setSessionIdStr(session.getId());
		globalSession.setUserName(user.getUserName());
		globalSession.setUserId(user.getId());
		globalSession.setPassWord(user.getPassWord());
		globalSessionCache.cachePut(session.getId(), globalSession);

		// 产生临时的token
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalSessionId(globalSession.getSessionIdStr());
		tokenInfo.setUserId(user.getId());
		tokenInfo.setUserName(user.getUserName());
		tokenInfo.setSsoClient("ef");
		tokenUtil.setToken(token, tokenInfo);

		// 3、如果携带了returnURL,那么就重定向，否则返回主页面
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("returnURL", request.getParameter("returnURL"));
		map.put("token", token);
		String redirectURL = ToolsUtil.addressAppend("localhost", "8078", "/client/auth/check", map);
		response.sendRedirect(redirectURL);
		return null;
	}

	@RequestMapping(value = "/auth/logout")
	public Long authLogout(JSONObject reqObj) throws Exception {
		// 1.判定用户是否登录

		// 1.1 如果没有登录，向浏览器显示登录页面

		// 1.2 如果已经登录，则产生临时令牌token,并重定向回应用系统

		return null;
	}

	@RequestMapping(value = "/register",method = RequestMethod.POST)
	public String register(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Users user = new Users();
		user.setUserName(SecurityUtils.getBase64(user.getUserName()));
		user.setPassWord(SecurityUtils.getBase64( user.getPassWord()));
		user.setId(System.currentTimeMillis());
		usersJPA.save(user);
		return "/login";
	}
}
