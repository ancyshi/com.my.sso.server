package com.my.util;


public class TokenUtil {

	// 存储临时令牌到redis中，存活期60秒
	public static void setToken(String tokenId, TokenInfo tokenInfo) {
	}

	// 根据token键取TokenInfo
	public static TokenInfo getToken(String token) {
		return null;
	}

	// 删除某个 token键值
	public static void delToken(String tokenId) {
	}

}
