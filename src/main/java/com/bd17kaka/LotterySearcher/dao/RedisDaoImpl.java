package com.bd17kaka.LotterySearcher.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedis;

/**
 * 将数据存储在Redis中
 * @author bd17kaka
 */
@Repository(value="redisDao")
public class RedisDaoImpl extends RedisUtils implements RedisDao {

	private static final Log log = LogFactory.getLog(RedisDaoImpl.class);

	public int hget(String key, String field) {
		
		ShardedJedis redis = getConnection();

		int value = 0;
		String strValue = redis.hget(key, field);
		try {
			value = Integer.parseInt(strValue);
		} catch (Exception e) {
			value = 0;
		}
		
		returnConnection(redis);
		return value;
	}

	public void hset(String redisKey, String field, String value) {

		ShardedJedis redis = getConnection();

		redis.hset(redisKey, field, value);
		
		returnConnection(redis);
		
	}
}
