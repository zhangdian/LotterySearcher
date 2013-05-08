package com.bd17kaka.LotterySearcher.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.bd17kaka.LotterySearcher.constat.SSH;
import com.bd17kaka.LotterySearcher.constat.SSH.RedDistributed;
import com.bd17kaka.LotterySearcher.constat.SSH.SSHRedAlgorithm;
import com.bd17kaka.LotterySearcher.dao.RedisDao;

/**
 * @author bd17kaka
 * 详细注释见 {@code SSHCalProbabilityService}注释
 * 这个实现只考虑长度为2-3的组合
 * 
 * 引入球号分布的概念
 */
@Service(value = "sshCalProbabilityServiceSimpleSpan3V5Impl")
public class SSHCalProbabilityServiceSimpleSpan3V5Impl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleSpan3V5Impl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {
		
		List<String> list = SSH.RED.getNumsFromInuput(input);
		if (list == null) {
			return 0;
		}
		
		// 六个球
		int 	a = Integer.parseInt(list.get(0)),
				b = Integer.parseInt(list.get(1)),
				c = Integer.parseInt(list.get(2)),
				d = Integer.parseInt(list.get(3)),
				e = Integer.parseInt(list.get(4)),
				f = Integer.parseInt(list.get(5));
		
		// 分子 分母
		long molecular 		= 10000; 
		long denominator 	= 1;
		String field 		= "";
		String redisKey 	= SSH.RED.getRedisKey();
		
		// 计算分子
		field =  String.format("%02d", a) 
				+ ":" + String.format("%02d", b) 
				+ ":" + String.format("%02d", c);
		molecular *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", b) 
				+ ":" + String.format("%02d", c) 
				+ ":" + String.format("%02d", d);
		molecular *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", c) 
				+ ":" + String.format("%02d", d) 
				+ ":" + String.format("%02d", e);
		molecular *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", d) 
				+ ":" + String.format("%02d", e) 
				+ ":" + String.format("%02d", f);
		molecular *= redisDao.hget(redisKey, field);
		molecular *= molecular;
		
		// 计算分母
		field =  String.format("%02d", a);
		denominator *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", b);
		denominator *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", c);
		denominator *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", d);
		denominator *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", b) 
				+ ":" + String.format("%02d", c);
		denominator *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", c) 
				+ ":" + String.format("%02d", d);
		denominator *= redisDao.hget(redisKey, field);
		field =  String.format("%02d", d) 
				+ ":" + String.format("%02d", e);
		denominator *= redisDao.hget(redisKey, field);

		// 结果
		double rs = 0.0;
		if (denominator != 0) {
			rs = (double)molecular / (double)denominator;
		}
		log.info(input + " : " + rs);
		
		return rs;
	}
	public Map<String, Double> calRedMostProbability(int size, String...strings) {

		// 参数检查，找到输入的红球分布
		if (null == strings) {
			return null;
		}
		
		int sshRedDistribute = 0;
		String strSSHRedDistribute = strings[0];
		try {
			sshRedDistribute = Integer.parseInt(strSSHRedDistribute);
		} catch (Exception e) {
			return null;
		}
		RedDistributed redDistributed = RedDistributed.getRedDistributed(sshRedDistribute);
		if (null == redDistributed) {
			return null;
		}
		
		// 从Redis中获取所有的值
		Map<String, String> map = redisDao.hgetAll(SSHRedAlgorithm.getRedisKeyOfTopNCombinationByDistribution(sshRedDistribute));
		if (map == null) {
			return null;
		}
		Set<String> keys = map.keySet();
		Map<String, Double> rsMap = new HashMap<String, Double>();
		for (String key : keys) {
			
			double value = 0;
			try {
				value = Double.parseDouble(map.get(key));
			} catch (Exception e) {
				continue;
			}
			rsMap.put(key, value);
		}
		
		return rsMap;
	}
}
