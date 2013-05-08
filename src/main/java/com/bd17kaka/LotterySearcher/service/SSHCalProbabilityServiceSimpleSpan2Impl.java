package com.bd17kaka.LotterySearcher.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.bd17kaka.LotterySearcher.constat.SSH;
import com.bd17kaka.LotterySearcher.dao.RedisDao;
import com.bd17kaka.LotterySearcher.po.SSHNewCombination;

/**
 * @author bd17kaka
 * 
 * 这个实现只考虑长度为2的组合
 */
@Service(value = "sshCalProbabilityServiceSimpleSpan2Impl")
public class SSHCalProbabilityServiceSimpleSpan2Impl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleSpan2Impl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {

		double fm = 0.0;
		
		// 输入参数检查
		List<String> list = SSH.RED.getNumsFromInuput(input);
		if (null == list) {
			log.info(input + "不是合法输入");
			return fm;
		}
		int size = list.size();
			
		// 首先获取第一个元素出现的概率
		int value = redisDao.hget(SSH.RED.getRedisKey(), list.get(0));
		int totalValue = 0;
		for (int i = 1; i <= SSH.RED.getMAX(); i++) {
			totalValue += redisDao.hget(SSH.RED.getRedisKey(), String.format("%02d", i));
		}
		fm = (double)value / (double)totalValue;
		
		// 循环计算后面的概率
		for (int i = 0; i < size - 1; i++) {
			value = redisDao.hget(SSH.RED.getRedisKey(), list.get(i) + ":" + list.get(i + 1));
			totalValue = redisDao.hget(SSH.RED.getRedisKey(), list.get(i));
			fm *= (double)value / (double)totalValue; 
		}
			
		// 结果
		log.info(input + " : " + fm);
		return fm;
		
	}

	public Map<String, Double> calRedMostProbability(int size, String...strings) {
		return null;
	}

}
