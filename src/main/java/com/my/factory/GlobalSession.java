package com.my.factory;

import javax.servlet.http.HttpSession;

/**
 * 具体产品
 * 
 * @author by_ww
 *
 */
public class GlobalSession extends MySession implements AbstractSession {

	public GlobalSession(String sesseionId, HttpSession session) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public GlobalSession generateSession(String sessionId, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

}
