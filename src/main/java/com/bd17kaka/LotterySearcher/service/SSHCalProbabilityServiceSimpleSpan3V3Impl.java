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
@Service(value = "sshCalProbabilityServiceSimpleSpan3V3Impl")
public class SSHCalProbabilityServiceSimpleSpan3V3Impl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleSpan3V3Impl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<SSHNewCombination> calRedMostProbability(int size, String...strings) {
		
		/**
		 * 首先遍历出所有组合
		 * 计算所有组合的概率，将概率值放大10000倍，也就是万级的概率
		 * 
		 * 只考虑长度为3的组合
		 * 
		 * 计算公式：
		 * 	分子 - (#ABC * #BCD * #CDE * #DEF)^2 * 10000
		 * 	分母 - (#A * #B * #C * #D) * (#BC * #CD * #DE) 
		 */
		
		// 结果集
		Map<String, Double> rsMap = new HashMap<String, Double>();
		
		// 保存长度为1-3的所有组合的出现个数
		Map<String, Integer> combinationMap = new HashMap<String, Integer>();
		int q = 1, w = 1, r = 1;
		int max = SSH.RED.getMAX();
		for (; q <=max; q++) {

			String field = "";
			int value = 0;

			field =  String.format("%02d", q); 
			value = redisDao.hget(SSH.RED.getRedisKey(), field);
			combinationMap.put(field, value);
			
			for (w = q + 1; w <=max; w++) {

				field =  String.format("%02d", q) 
						+ ":" + String.format("%02d", w);
				value = redisDao.hget(SSH.RED.getRedisKey(), field);
				combinationMap.put(field, value);

				for (r = w + 1; r <=max; r++) {
					
					field =  String.format("%02d", q) 
							+ ":" + String.format("%02d", w) 
							+ ":" + String.format("%02d", r);
					value = redisDao.hget(SSH.RED.getRedisKey(), field);
					combinationMap.put(field, value);
				}
				
			}
			
		}

		// 代表六个球号
		int a = 1, 
			b = 1, 
			c = 1, 
			d = 1, 
			e = 1, 
			f = 1;
		for (; a <= max; a++) {
			
			for (b = a + 1; b <= max; b++) {
				
				for (c = b + 1; c <= max; c++) {
					
					for (d = c + 1; d <= max; d++) {
						
						for (e = d + 1; e <= max; e++) {
							
							for (f = e + 1; f <= max; f++) {
								
								// 分子 分母
								long molecular = 10000; 
								long denominator = 1;
								String field = "";
								
								// 计算分子
								field =  String.format("%02d", a) 
										+ ":" + String.format("%02d", b) 
										+ ":" + String.format("%02d", c);
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", b) 
										+ ":" + String.format("%02d", c) 
										+ ":" + String.format("%02d", d);
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", c) 
										+ ":" + String.format("%02d", d) 
										+ ":" + String.format("%02d", e);
								molecular *= combinationMap.get(field);
								field =  String.format("%02d", d) 
										+ ":" + String.format("%02d", e) 
										+ ":" + String.format("%02d", f);
								molecular *= combinationMap.get(field);
								molecular *= molecular;
								
								// 计算分母
								field =  String.format("%02d", a);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", b);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", c);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", d);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", b) 
										+ ":" + String.format("%02d", c);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", c) 
										+ ":" + String.format("%02d", d);
								denominator *= combinationMap.get(field);
								field =  String.format("%02d", d) 
										+ ":" + String.format("%02d", e);
								denominator *= combinationMap.get(field);
								
								// 保存到结果集
								field = String.format("%02d", a) 
										+ ":" + String.format("%02d", b) 
										+ ":" + String.format("%02d", c) 
										+ ":" + String.format("%02d", d)
										+ ":" + String.format("%02d", e)
										+ ":" + String.format("%02d", f);
								double rs = 0.0;
								if (denominator != 0) {
									rs = (double)molecular / (double)denominator;
								}
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
