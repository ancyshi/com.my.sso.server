package com.my.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.my.cache.CookieCache;
import com.my.dao.UsersJPA;
import com.my.model.CookieId;
import com.my.model.Users;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.util.GlobalSessions;

@RestController
@RequestMapping(value = "/oneToMany")
public class APPController {
	@Resource
	private UsersJPA usersJPA;

	@Resource
	private CookieCache cookieCache;

	@RequestMapping(value = "/query")
	public String pageLogin(@RequestBody JSONObject reqObject) throws Exception {

		Users user = usersJPA.findByUserNameAndPassWord(reqObject.getString("userName"),reqObject.getString("passWord"));
		return user.toString();

	}
	@RequestMapping(value = "/redis")
	public Object redis(@RequestBody JSONObject record) throws Exception {

		// 产生临时的token
		CookieId localCookieId = new CookieId();
		localCookieId.setCookiesId("afefefe1");
		cookieCache.jedisSAdd("fe", localCookieId.getCookiesId());

		// cookieCache.delete("fe");

		return localCookieId.getCookiesId();

	}

}
