package com.my.factory;

import javax.servlet.http.HttpSession;

/**
 * 具体工厂
 * 
 * @author by_ww
 *
 */
public class SessionFactory implements AbstractFactory {

	@Override
	public AbstractSession generateAbstractSession(String sesseionId, HttpSession session) {
		return new GlobalSession(sesseionId, session);
	}

}
