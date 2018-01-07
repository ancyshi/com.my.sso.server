package com.my.controller;

import java.util.UUID;

import javax.annotation.Resource;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.dao.UsersJPA;
import com.my.model.Users;
import com.my.util.TokenInfo;
import com.my.util.TokenUtil;

@RestController
@RequestMapping(value = "/sso")
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
	public void pageLogin(JSONObject request) throws Exception {
		// 1.判定用户是否登录
		if (request.getJSONObject("globalsessionid") == null) {
			// 显示登录界面，重定向到登陆界面，输入用户名和密码
		}
//		User user = JSON.toJavaObject(request.getJSONObject("user"), User.class);
		
		// 1.2 如果已经登录，则产生临时令牌token
		Users user = usersJPA.findByUserNameAndPassWord(request.getString("userName"),request.getString("passWord"));
		
		if (user == null) {
			// 没有注册过的用户，显示注册界面
		}
		
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalId("feaef");
		tokenInfo.setUserId(user.getId());
		tokenInfo.setUsername(user.getUserName());
		tokenInfo.setSsoClient("ef");
		TokenUtil.setToken(token, tokenInfo);

		// 1.3 判定是否有returnURL,有的话就重定向回应用系统，没有就显示主页面
		if (!StringUtils.isEmpty(request.getString("returnURL"))) {
			// 重定向到应用系统
		} else {
			// 重定向到登陆页面
		}

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
	public void authLogin(JSONObject request) throws Exception {
		// 1、认证用户
		Users user = usersJPA.findByUserNameAndPassWord(request.getString("userName"),request.getString("passWord"));
		
		if (user == null) {
			// 没有注册过的用户，显示注册界面
		}

		// 2、认证通过，就产生临时的token
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setGlobalId("feaef");
		tokenInfo.setUserId(user.getId());
		tokenInfo.setUsername(user.getUserName());
		tokenInfo.setSsoClient("ef");
		TokenUtil.setToken(token, tokenInfo);

		// 3、如果携带了returnURL,那么就重定向，否则返回主页面
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
	public Long authVerify(JSONObject reqObj) throws Exception {
		// 1、获取到token
		

		// 2、认证token是否有效

		// 3、有效，则返回用户的一些信息，并且声称全局的session
		return null;
	}

	private void redirectMethod() {
		// 向认证中心发送验证token请求
		String verifyURL = "http://" + server + PropertiesConfigUtil.getProperty("sso.server.verify");
		HttpClient httpClient = new DefaultHttpClient();
		// serverName作为本应用标识
		HttpGet httpGet = new HttpGet(verifyURL + "?token=" + token + "&localId=" + request.getSession().getId());
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
				// 解析json数据
				ObjectMapper objectMapper = new ObjectMapper();
				VerifyBean verifyResult = objectMapper.readValue(result, VerifyBean.class);
				// 验证通过,应用返回浏览器需要验证的页面
				if (verifyResult.getRet().equals("0")) {
					Auth auth = new Auth();
					auth.setUserId(verifyResult.getUserId());
					auth.setUsername(verifyResult.getUsername());
					auth.setGlobalId(verifyResult.getGlobalId());
					request.getSession().setAttribute("auth", auth);
					// 建立本地会话
					return "redirect:http://" + returnURL;
				}
			}
		} catch (Exception e) {
			return "redirect:" + loginURL;
		}

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
