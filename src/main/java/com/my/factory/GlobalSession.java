package com.my.factory;


/**
 * 具体产品
 * 
 * @author by_ww
 *
 */
public class GlobalSession implements AbstractSession {
	private String sessionIdStr;

	private String userName;

	private String passWord;
	
	private Long userId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public void setSessionIdStr(String sessionIdStr) {
		this.sessionIdStr = sessionIdStr;
	}

	public String getSessionIdStr() {
		return sessionIdStr;
	}

	public GlobalSession() {
		super();
	}
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	public GlobalSession(String sessionIdStr, String userName, String passWord,
			Long userId) {
		super();
		this.sessionIdStr = sessionIdStr;
		this.userName = userName;
		this.passWord = passWord;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "{\"sessionIdStr\":\"" + sessionIdStr + "\",\"userName\":\""
				+ userName + "\",\"passWord\":\"" + passWord
				+ "\",\"userId\":\"" + userId + "\"} ";
	}
}
