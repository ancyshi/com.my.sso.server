package com.my.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.my.model.User;
import com.my.util.TokenInfo;
import com.my.util.TokenUtil;

@RestController
@RequestMapping(value = "/server")
public class SSOController {
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
	public String pageLogin(JSONObject request) throws Exception {
		// 1.判定是否有GlobalSessionId并且合法
		if (!checkSessionId(request)) {
			// 显示登录界面
		}
		// 1.2 如果已经登录，则产生临时令牌token
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalId("feaef");
		tokenInfo.setUserId(2);
		tokenInfo.setUsername("张三");
		tokenInfo.setSsoClient("ef");
		TokenUtil.setToken(token, tokenInfo);

		// 1.3 判定是否有returnURL,有的话就重定向回应用系统，没有就显示主页面
		if (request.getString("returnURL") != null) {
			// 发送请求到/client/auth/check
		}
		return "/login";
	}

	private boolean checkSessionId(JSONObject request) {
		String sessionId = request.getString("globalSession");
		if (sessionId != null) {
			// 校验sessionid是否合法
		}
		return false;
	}

	/*
	 * 说明： 处理浏览器用户登录认证请求。如带有returnURL参数，认证通过后，将产生临时认证令牌token，并携带此token重定向回系统。
	 * 如没有带returnURL参数
	 * ，说明用户是直接从认证中心发起的登录请求，认证通过后，返回认证中心首页提示用户已登录。上面登录时序交互图中的3和此接口有关。
	 */
	// 这个接口就是登陆界面发送请求的接口，判定是否携带returnURL，如果携带则重定向回去，否则直接登陆主界面
	@RequestMapping(value = "/auth/login")
	public void authLogin(JSONObject reqObj) throws Exception {
		// 1、认证用户
		User user = new User();
		user.setName("张三");

		// 2、认证不通过就返回，并报错

		// 3、认证通过，就产生临时的token
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalId("feaef");
		tokenInfo.setUserId(2);
		tokenInfo.setUsername("张三");
		tokenInfo.setSsoClient("ef");
		TokenUtil.setToken(token, tokenInfo);

		// 4、如果携带了returnURL,那么就重定向，否则返回主页面
		redictToApp();
	}

	private void redictToApp() {
		// TODO Auto-generated method stub

	}

	/*
	 * 说明：认证应用系统来的token是否有效，如有效，应用系统向认证中心注册，同时认证中心会返回该应用系统登录用户的相关信息，如ID,username等
	 * 。上面登录时序交互图中的4和此接口有关。
	 */
	@RequestMapping(value = "/auth/verify")
	public String authVerify(JSONObject reqObj) throws Exception {
		// 1、获取到token
		JSONObject token = reqObj.getJSONObject("token");

		// 2、认证token是否有效
		token.getString("userName");

		// 3、有效，则返回用户的一些信息，并且生成全局的session
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
