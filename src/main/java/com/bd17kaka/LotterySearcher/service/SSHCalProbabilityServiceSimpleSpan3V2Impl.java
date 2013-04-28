package com.bd17kaka.LotterySearcher.service;

import java.util.ArrayList;
import java.util.Collections;
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

	/* (non-Javadoc)
	 * @see com.bd17kaka.LotterySearcher.service.SSHCalProbabilityService#calRedMostProbability(int)
	 */
	public List<SSHNewCombination> calRedMostProbability(int size) {
		
		/**
		 * 首先遍历出所有组合
		 * 计算所有组合的概率，将概率值放大10000倍，也就是万级的概率
		 * 
		 * 计算公式：
		 * 	分子 - (#ABC * #BCD * #CDE * #DEF * #EF) * 10000
		 * 	分母 - (#B * #C * #D * #E) 
		 */
		
		// 结果集
		Map<String, Double> rsMap = new HashMap<String, Double>();
		
		// 保存长度为1-3的所有组合的出现个数
		Map<String, Integer> combinationMap = new HashMap<String, Integer>();
		int a = 1, b = 1, c = 1;
		int max = SSH.RED.getMAX();
		for (; a <=max; a++) {
			
			for (b = a + 1; b <=max; b++) {
				
				String field = "";
				int value = 0;

				field =  String.format("%02d", a) 
						+ ":" + String.format("%02d", b);
				value = redisDao.hget(SSH.RED.getRedisKey(), field);
				combinationMap.put(field, value);

				for (c = b + 1; c <=max; c++) {
					
					field =  String.format("%02d", a) 
							+ ":" + String.format("%02d", b) 
							+ ":" + String.format("%02d", c);
					value = redisDao.hget(SSH.RED.getRedisKey(), field);
					combinationMap.put(field, value);
				}
				
			}
			
		}

		// 代表六个球号
		int i = 1, j = 1, k = 1, m = 1, n = 1, l = 1;
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
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", j) 
										+ ":" + String.format("%02d", k) 
										+ ":" + String.format("%02d", m);
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", k) 
										+ ":" + String.format("%02d", m) 
										+ ":" + String.format("%02d", n);
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", m) 
										+ ":" + String.format("%02d", n) 
										+ ":" + String.format("%02d", l);
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", n) 
										+ ":" + String.format("%02d", l);
								molecular *= combinationMap.get(field);
								
								// 计算分母
								field =  String.format("%02d", j);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", k);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", m);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", n);
								denominator *= combinationMap.get(field);
								
								// 保存到结果集
								field = String.format("%02d", i) 
										+ ":" + String.format("%02d", j) 
										+ ":" + String.format("%02d", k) 
										+ ":" + String.format("%02d", m)
										+ ":" + String.format("%02d", n)
										+ ":" + String.format("%02d", l);
								double rs = (double)molecular / (double)denominator;
								rsMap.put(field, rs);
								log.info("组合[" + field + "]: " + rs);
								
								// 如果结果集超过了size个，进行排序，剔除最后的一个
								if (rsMap.size() > size) {
									
									ArrayList<Entry<String, Double>> tmp = new ArrayList<Entry<String, Double>>(rsMap.entrySet());
									Collections.sort(tmp, new SSHRedProbabilityComparator());
									Entry<String, Double> which2Del = tmp.get(tmp.size() - 1);
									rsMap.remove(which2Del.getKey());
									log.info("组合[" + which2Del.getKey() + "]被删除, 其概率为" + which2Del.getValue());
									
								}
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
		// 打印概率最大的10个组合
		log.info("-----------------------TOP 10-----------------------------");
		Set<String> set = rsMap.keySet();
		for (String item : set) {
			log.info("组合[" + item + "]: " + rsMap.get(item));
		}
		
		return null;
		
	}

}
