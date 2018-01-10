package com.my.controller;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.my.dao.UsersJPA;
import com.my.model.Users;
import com.my.util.GlobalSessions;
import com.my.util.TokenInfo;
import com.my.util.TokenUtil;
import com.my.util.ToolsUtil;

@Controller
@RequestMapping(value = "/server")
public class SSOController {

	@Resource
	private UsersJPA usersJPA;

	String token = UUID.randomUUID().toString();

	/*
	 * 此接口主要接受来自应用系统的认证请求，此时，returnURL参数需加上，用以向认证中心标识是哪个应用系统，以及返回该应用的URL。
	 * 如用户没有登录，应用中心向浏览器用户显示登录页面。 如已登录，则产生临时令牌token，并重定向回该系统。上面登录时序交互图中的2和此接口有关。
	 * 
	 * 当然，该接口也同时接受用户直接向认证中心登录，此时没有returnURL参数，认证中心直接返回登录页面
	 * 
	 * 这个接口是应用系统与认证中心之间的通信，作用 1、
	 */
	@RequestMapping(value = "/page/login")
	public void pageLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 1.判定是否有GlobalSessionId并且合法
		JSONObject resultObj = new JSONObject();
		String globalSessionId = ToolsUtil.getCookieValueByName(request, "globalSessionId");

		if (null == globalSessionId) {
//			resultObj.put("returnUR", "/login");
//			return "/login";
			response.sendRedirect("http://localhost:8077/server/auth/login?returnURL=app1");
		}
		// 1.2 如果已经登录，则产生临时令牌token
		HttpSession globalSession = GlobalSessions.getSession(globalSessionId);

		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalSessionId("feaef");
		tokenInfo.setUserId(Long.parseLong(globalSession.getId()));
		tokenInfo.setUsername((String) globalSession.getAttribute("name"));
		tokenInfo.setSsoClient("ef");
		TokenUtil.setToken(token, tokenInfo);

		resultObj.put("tokenInfo", tokenInfo);
		resultObj.put("returnURL", request.getAttribute("returnURL"));

		response.sendRedirect("http://localhost:8077/client/auth/check?tokenInfo=" + tokenInfo);

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
	@RequestMapping(value = "/auth/login")
	public String authLogin(HttpServletRequest request) throws Exception {
		// 1、认证用户
		if (request.getAttribute("userName") == null) {
			return "/login";
		}
		Users user = usersJPA.findByUserNameAndPassWord((String) request.getAttribute("userName"),
				(String) request.getAttribute("passWord"));

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
		tokenInfo.setGlobalSessionId("feaef");
		tokenInfo.setUserId(user.getId());
		tokenInfo.setUsername(user.getUserName());
		tokenInfo.setSsoClient("ef");
		TokenUtil.setToken(token, tokenInfo);

		// 3、如果携带了returnURL,那么就重定向，否则返回主页面
		return (String) request.getAttribute("returnURL");
	}

	/*
	 * 说明：认证应用系统来的token是否有效，如有效，应用系统向认证中心注册，同时认证中心会返回该应用系统登录用户的相关信息，如ID,username等
	 * 。上面登录时序交互图中的4和此接口有关。
	 */
	@RequestMapping(value = "/auth/verify")
	public Object authVerify(JSONObject reqObj) throws Exception {
		// 1、获取到token
		JSONObject token = reqObj.getJSONObject("token");

		if (token == null) {
			return false;
		}

		// 2、认证token是否有效
		String userName = token.getString("userName");
		String passWord = token.getString("");

		// 验证token是否有效

		// 3、有效，则返回用户的一些信息，并且声称全局的session
		return true;
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
