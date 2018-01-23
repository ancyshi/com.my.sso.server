package com.my.cache;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.my.factory.GlobalSession;

@Service
public class GlobalSessionCache {

	private JedisPool jedisPool = new JedisPool();

	public String cachePut(String sessionId, GlobalSession globalSession) throws Exception {
		Jedis jedis = jedisPool.getResource();
		jedis.set(sessionId, globalSession.toString());
		return "true";
	}

	public GlobalSession cacheable(String sessionId) throws Exception {
		Jedis jedis = jedisPool.getResource();
		String globalSession = jedis.get(sessionId);
		if (globalSession == null) {
			return null;
		}
		JSONObject globalObject = JSON.parseObject(globalSession);
		return JSON.toJavaObject(globalObject, GlobalSession.class);
	}
}
