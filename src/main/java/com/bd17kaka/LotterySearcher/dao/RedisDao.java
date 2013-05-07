package com.bd17kaka.LotterySearcher.dao;

import java.util.List;
import java.util.Map;


/**
 * 将数据存储在Redis中
 * @author bd17kaka
 */
public interface RedisDao {
	
	/**
	 * 获取值
	 * @param key
	 * @return
	 */
	int hget(String redisKey, String field);
	
	/**
	 * 设置值
	 * @param redisKey
	 * @param field
	 * @param value
	 */
	void hset(String redisKey, String field, String value);
	
	/**
	 * @param redisKey
	 * @return
	 */
	Map<String, String> hgetAll(String redisKey);
	
	/**
	 * 删除一个Key
	 * @param redisKey
	 */
	void del(String redisKey);
	
}
