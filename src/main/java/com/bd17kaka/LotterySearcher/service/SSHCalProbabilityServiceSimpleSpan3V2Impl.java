package com.bd17kaka.LotterySearcher.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.bd17kaka.LotterySearcher.constat.SSH;
import com.bd17kaka.LotterySearcher.dao.RedisDao;
import com.bd17kaka.LotterySearcher.po.SSHNewCombination;

/**
 * @author bd17kaka
 * 详细注释见 {@code SSHCalProbabilityService}注释
 * 这个实现只考虑长度为2-3的组合
 */
@Service(value = "sshCalProbabilityServiceSimpleSpan3V2Impl")
public class SSHCalProbabilityServiceSimpleSpan3V2Impl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleSpan3V2Impl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<SSHNewCombination> calRedMostProbability(int size) {
		
		/**
		 * 首先遍历出所有组合
		 * 计算所有组合的概率，将概率值放大10000倍，也就是万级的概率
		 * 
		 * 计算公式：
		 * 	分子 - (#ABC * #BCD * #CDE * #DEF) * 10000
		 * 	分母 - (#A * #B * #C * #D * #E * #F) 
		 */
		
		// 结果集
		Map<String, Double> rsMap = new HashMap<String, Double>();

		// 代表六个球号
		int i = 1, j = 1, k = 1, m = 1, n = 1, l = 1;
		int max = SSH.RED.getMAX();
		for (; i <= max; i++) {
			
			for (j = i + 1; j <= max; j++) {
				
				for (k = j + 1; k <= max; k++) {
					
					for (m = k + 1; m <= max; m++) {
						
						for (n = m + 1; n <= max; n++) {
							
							for (l = n + 1; l <= max; l++) {
								
								// 分子 分母
								long molecular = 10000; 
								long denominator = 1;
								String field = "";
								
								// 计算分子
								field =  String.format("%02d", i) 
										+ ":" + String.format("%02d", j) 
										+ ":" + String.format("%02d", k);
								molecular *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", j) 
										+ ":" + String.format("%02d", k) 
										+ ":" + String.format("%02d", m);
								molecular *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", k) 
										+ ":" + String.format("%02d", m) 
										+ ":" + String.format("%02d", n);
								molecular *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", m) 
										+ ":" + String.format("%02d", n) 
										+ ":" + String.format("%02d", l);
								molecular *= redisDao.hget(SSH.RED.getRedisKey(), field);
								
								// 计算分母
								field =  String.format("%02d", i);
								denominator *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", j);
								denominator *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", k);
								denominator *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", m);
								denominator *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", n);
								denominator *= redisDao.hget(SSH.RED.getRedisKey(), field);
								field =  String.format("%02d", l);
								denominator *= redisDao.hget(SSH.RED.getRedisKey(), field);
								
								// 保存到结果集
								field = String.format("%02d", i) 
										+ ":" + String.format("%02d", j) 
										+ ":" + String.format("%02d", k) 
										+ ":" + String.format("%02d", m)
										+ ":" + String.format("%02d", n)
										+ ":" + String.format("%02d", l);
								rsMap.put(field, (double)molecular / (double)denominator);
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		// 排序获取概率最大的size个组合
		ArrayList<Entry<String, Double>> tmp = new ArrayList<Entry<String, Double>>(rsMap.entrySet());
		Collections.sort(tmp, new SSHRedProbabilityComparator());
		int t = 0;
		for(Entry<String, Double> e : tmp) {  
            log.info(e.getKey() + "的概率是" + e.getValue());
            if (t++ >= 10) {
            	break;
            }
        }  
		
		return null;
		
	}

}
