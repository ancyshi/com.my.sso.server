package com.my.cache;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.my.factory.GlobalSession;

@Service
public class GlobalSessionCache {

	private JedisPool jedisPool = new JedisPool();

	public String cachePut(String sessionId, GlobalSession globalSession) throws Exception {
		Jedis jedis = jedisPool.getResource();
		jedis.setex(sessionId, 60, globalSession.toString());
		return "true";
	}

}
