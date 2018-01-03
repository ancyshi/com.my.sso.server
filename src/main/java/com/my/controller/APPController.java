package com.my.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.util.GlobalSessions;

@RestController
@RequestMapping(value = "/app")
public class APPController {

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

}
