package com.my.factory;

import javax.servlet.http.HttpSession;

import com.my.model.Users;

/**
 * 具体工厂
 * @author by_ww
 *
 */
public class SessionFactory implements AbstractFactory {

	@Override
	public AbstractSession generateAbstractSession(Users user,HttpSession session) {
		return new GlobalSession(user,session);
	}

}
