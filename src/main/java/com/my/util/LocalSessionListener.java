package com.my.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class LocalSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		GlobalSessions.addSession(se.getSession().getId(), se.getSession());

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		GlobalSessions.delSession(se.getSession().getId());

	}

}
