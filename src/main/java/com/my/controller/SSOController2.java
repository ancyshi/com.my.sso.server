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
public class SSOController2 {

	@Resource
	private UsersJPA usersJPA;

	@Resource
	private TokenUtil tokenUtil;

	String token = UUID.randomUUID().toString();

	
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

	
}
