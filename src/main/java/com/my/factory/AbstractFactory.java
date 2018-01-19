package com.my.factory;

import javax.servlet.http.HttpSession;

import com.my.model.Users;

/**
 * 抽象工厂
 * @author by_ww
 *
 */
public interface AbstractFactory {

	public AbstractSession generateAbstractSession(Users user, HttpSession session);
}
