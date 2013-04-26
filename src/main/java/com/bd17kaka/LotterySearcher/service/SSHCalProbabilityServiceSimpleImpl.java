package com.bd17kaka.LotterySearcher.service;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.bd17kaka.LotterySearcher.constat.SSH;
import com.bd17kaka.LotterySearcher.dao.RedisDao;

@Service(value = "sshCalProbabilityServiceSimpleImpl")
public class SSHCalProbabilityServiceSimpleImpl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleImpl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {

		double fm = 0.0;
		
		// 输入参数检查
		String[] tokens = input.split(",");
		if (tokens.length != SSH.RED.getTOTAL()) {
			log.info(input + "不是合法输入");
			return fm;
		}
		
		// 首先获取第一个元素出现的概率
		int value = redisDao.hget(SSH.RED.getRedisKey(), tokens[0]);
		int totalValue = 0;
		for (int i = 0; i <  SSH.RED.getMAX(); i++) {
			totalValue += redisDao.hget(SSH.RED.getRedisKey(), String.format("%02d", i));
		}
		fm = (double)value / (double)totalValue;
		
		// 循环计算后面的概率
		for (int i = 0; i < tokens.length - 2; i++) {
			value = redisDao.hget(SSH.RED.getRedisKey(), tokens[i] + ":" + tokens[i + 1]);
			totalValue = redisDao.hget(SSH.RED.getRedisKey(), tokens[i]);
			fm *= (double)value / (double)totalValue; 
		}
		
		// 结果
		log.info(input + " : " + fm);
		return fm;
		
	}
}
