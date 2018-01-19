package com.my.factory;

import javax.servlet.http.HttpSession;

/**
 * 抽象工厂
 * 
 * @author by_ww
 *
 */
public interface AbstractFactory {

	public AbstractSession generateAbstractSession(String sesseionId, HttpSession session);
}
