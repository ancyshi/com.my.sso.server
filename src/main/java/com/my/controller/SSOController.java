package com.my.controller;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.my.dao.UsersJPA;
import com.my.model.TokenInfo;
import com.my.model.Users;
import com.my.util.GlobalSessions;
import com.my.util.TokenUtil;
import com.my.util.ToolsUtil;

@RestController
@RequestMapping(value = "/server")
public class SSOController {

	@Resource
	private UsersJPA usersJPA;

	@Resource
	private TokenUtil tokenUtil;

	String token = UUID.randomUUID().toString();

	/*
	 * 此接口主要接受来自应用系统的认证请求，此时，returnURL参数需加上，用以向认证中心标识是哪个应用系统，以及返回该应用的URL。
	 * 如用户没有登录，应用中心向浏览器用户显示登录页面。 如已登录，则产生临时令牌token，并重定向回该系统。上面登录时序交互图中的2和此接口有关。
	 * 
	 * 当然，该接口也同时接受用户直接向认证中心登录，此时没有returnURL参数，认证中心直接返回登录页面
	 * 
	 * 这个接口是应用系统与认证中心之间的通信，作用 1、
	 */
	@RequestMapping(value = "/page/login", method = RequestMethod.GET)
	public void pageLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1.判定是否有GlobalSessionId并且合法
		JSONObject resultObj = new JSONObject();
		
		String globalSessionId = ToolsUtil.getCookieValueByName(request, "globalSessionId");

		if (null == globalSessionId) {
			// 重定向之后会执行下面的语句，因此加个return
			response.sendRedirect("http://localhost:8078/client/auth/check");
			return;
		}
		// 1.2 如果已经登录，则产生临时令牌token
		HttpSession globalSession = GlobalSessions.getSession(globalSessionId);

		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalSessionId("feaef");
		tokenInfo.setUserId(Long.parseLong(globalSession.getId()));
		tokenInfo.setUserName((String) globalSession.getAttribute("name"));
		tokenInfo.setSsoClient("ef");
		tokenUtil.setToken(token, tokenInfo);

		resultObj.put("tokenInfo", tokenInfo);
		resultObj.put("returnURL", request.getParameter("returnURL"));

		response.sendRedirect("http://localhost:8078/client/auth/check?tokenInfo=" + tokenInfo);
		return;

	}

	/*
	 * 说明： 处理浏览器用户登录认证请求。如带有returnURL参数，认证通过后，将产生临时认证令牌token，并携带此token重定向回系统。
	 * 如没有带returnURL参数
	 * ，说明用户是直接从认证中心发起的登录请求，认证通过后，返回认证中心首页提示用户已登录。上面登录时序交互图中的3和此接口有关。
	 * 
	 * 
	 * 这里假设每个页面都带有returnURL,如果是登陆页面发送的请求那么携带主页面的url，否则携带app页面的url
	 */
	// 用户输入用户名和密码后，点击取人，发送请求到这个接口
	// 这个接口就是登陆界面发送请求的接口，判定是否携带returnURL，如果携带则重定向回去，否则直接登陆主界面
	@RequestMapping(value = "/auth/login", method = RequestMethod.POST)
	public void authLogin(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 1、认证用户
		 Users user =
		 usersJPA.findByUserNameAndPassWord(request.getParameter("userName"),
		 (String) request.getParameter("passWord"));
//		Users user = new Users();
//		user.setId(12l);
//		user.setPassWord("234");
//		user.setUserName("zhangsna");

		if (user == null) {
			// 没有注册过的用户，显示注册界面
		}

		// 2、认证通过，产生全局的sessionId
		HttpSession session = request.getSession(true);
		session.setAttribute("username", user.getUserName());
		session.setAttribute("password", user.getPassWord());

		GlobalSessions.addSession(session.getId(), session);

		// 产生临时的token
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalSessionId(session.getId());
		tokenInfo.setUserId(user.getId());
		tokenInfo.setUserName(user.getUserName());
		tokenInfo.setSsoClient("ef");
		tokenUtil.setToken(token, tokenInfo);

//		String aString = tokenUtil.getToken("6d813fc4-ae76-4d1c-9f59-7e64e83a400c", tokenInfo);

		// 3、如果携带了returnURL,那么就重定向，否则返回主页面
		 response.sendRedirect("http://localhost:8078/client/auth/check?token="
		 + token + "&returnURL"
		 + request.getParameter("returnURL"));
		return;
	}

	/*
	 * 说明：认证应用系统来的token是否有效，如有效，应用系统向认证中心注册，同时认证中心会返回该应用系统登录用户的相关信息，如ID,username等
	 * 。上面登录时序交互图中的4和此接口有关。
	 */
	@RequestMapping(value = "/auth/verify")
	public Object authVerify(@RequestBody JSONObject reqObj) throws Exception {
		// 1、获取到token
		String token = reqObj.getString("token");

		if (token == null) {
			throw new Exception("没有收到token字段信息");
		}

		// 2、认证token是否有效
		
		String tokenInfo = tokenUtil.getToken(token, null);
		
		return tokenInfo;
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
