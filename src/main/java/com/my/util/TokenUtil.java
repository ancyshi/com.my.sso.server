package com.my.util;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import com.alibaba.fastjson.JSON;
import com.my.dao.TokenInfoJPA;
import com.my.model.TokenInfo;

@Service
public class TokenUtil {
	
	@Resource
	private TokenInfoJPA tokenInfoJPA;
	
//	@Resource
//	private Jedis jedis; 


	// 存储临时令牌到redis中，存活期60秒
	@CachePut(key = "#tokenId", value = "tokenInfo")
	public String setToken(String tokenId, TokenInfo tokenInfo) {
		TokenInfo token = tokenInfoJPA.save(tokenInfo);
		// 这边存入redis,设置存活期为60秒
		return token.toString();
	}

	// 根据token键取TokenInfo
	@Cacheable(key = "#tokenId", value = "tokenInfo")
	public String getToken(String tokenId, TokenInfo tokenInfo) {
		
		if (null == tokenInfo) {
			return null;
		}
		TokenInfo token = tokenInfoJPA.findOne(tokenInfo.getUserId());
		// 根据token去redis中查询tokenInfo
		return JSON.toJSONString(token);
	}

	// 删除某个 token键值
	@CacheEvict(key = "#tokenId", value = "tokenInfo")
	public boolean delToken(String tokenId) {
		return true;
	}
	
//	// 存储临时令牌到redis中，存活期60秒
//	public void setTokenInfo(String tokenId, TokenInfo tokenInfo) {
//		jedis.set(tokenId, tokenInfo.toString());
//	}
//
//	// 根据token键取TokenInfo
//	public TokenInfo getTokenInfo(String tokenId) {
//		String tokenInfoStr = jedis.get(tokenId);
//		return JSONObject.parseObject(tokenInfoStr, TokenInfo.class);
//	}
//
//	// 删除某个 token键值
//	public void delTokenInfo(String tokenId) {
//	}

}
