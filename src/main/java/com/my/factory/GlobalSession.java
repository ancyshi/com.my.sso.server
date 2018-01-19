package com.my.factory;

import javax.servlet.http.HttpSession;

import com.my.model.Users;

/**
 * 具体产品
 * @author by_ww
 *
 */
public class GlobalSession extends MySession implements AbstractSession {

	public GlobalSession(Users user,HttpSession session) {
		session.setAttribute("username", user.getUserName());
		session.setAttribute("password", user.getPassWord());
		
	}

	
	public GlobalSession() {
		// TODO Auto-generated constructor stub
	}


	@Override
	public GlobalSession generateSession(String sessionId, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
