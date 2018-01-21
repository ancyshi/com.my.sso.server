package com.my.factory;

import javax.servlet.http.HttpSession;

/**
 * 具体产品
 * 
 * @author by_ww
 *
 */
public class GlobalSession  implements AbstractSession {
	public String sessionIdStr;

	public HttpSession httpSession;

	public String getSessionIdStr() {
		return sessionIdStr;
	}

	public void setSessionIdStr(String sessionIdStr) {
		this.sessionIdStr = sessionIdStr;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}


	public GlobalSession() {
		super();
	}

	public GlobalSession(String sessionIdStr, HttpSession httpSession) {
		super();
		this.sessionIdStr = sessionIdStr;
		this.httpSession = httpSession;
	}


	@Override
	public GlobalSession generateSession(String sessionId, HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

}
