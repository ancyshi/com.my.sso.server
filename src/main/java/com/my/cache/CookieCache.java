package com.my.cache;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.my.dao.CookieIdJPA;
import com.my.model.CookieId;

@Service
public class CookieCache {

	@Resource
	private CookieIdJPA cookieIdJPA;

	// @Resource
	private JedisPool jedisPool = new JedisPool();

//	@CachePut(key = "#key", value = "cookie")
	public String add(String key, CookieId cookieId) {
		CookieId cookie = cookieIdJPA.save(cookieId);
		return cookie.getCookiesId();
	}

//	@Cacheable(value = "cookie", key = "#cookieId")
	public CookieId getCookie(String cookieId)  {
		CookieId cookie = cookieIdJPA.jpaFindOne(cookieId);
	   return	cookie;
	}

	// @CachePut(key = "#key", value = "cookie")
	public Boolean jedisAdd(String key, String value) {
		Jedis jedis = jedisPool.getResource();
		jedis.set(key, value);
		return true;
	}

	public String jedisGet(String key) {
		Jedis jedis = jedisPool.getResource();
		String value = jedis.get(key);
		if (value == null) {
			return "";
		}
		return value;
	}

//	@CacheEvict(value = "cookie", key = "#key")
	public void delete(String key) {
		cookieIdJPA.deleteAll();
		return;

	}

	// 存储集合
	public void jedisSAdd(String globalSessionId, String localSessionIdStr) {
		Jedis jedis = jedisPool.getResource();
		jedis.sadd(globalSessionId, localSessionIdStr);
		return;
	}

}
