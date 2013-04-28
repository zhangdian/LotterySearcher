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
 * 详细注释见 {@code SSHCalProbabilityService}注释
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

	public List<SSHNewCombination> calRedMostProbability(int size) {
		
		/**
		 * 每个维度的所有球号的出现概率存在一个Map中，所有维度组成一个List，结构如下：
		 * 	List<Map<String, double>>
		 * 每单个球出现的个数存储在一个Map中：
		 * 	Map<String, Integer>
		 */
		List<Map<String, Double>> list = new ArrayList<Map<String, Double>>();
		Map<String, Integer> map = new HashMap<String, Integer>();

		// 计算每单个球出现的概率，同时算出总数
		int totalValue = 0;
		for (int j = 1; j <= SSH.RED.getMAX(); j++) {
			int curValue = 0;
			String field =  String.format("%02d", j);
			curValue = redisDao.hget(SSH.RED.getRedisKey(), field);
			map.put(field, curValue);
			totalValue += curValue;
		}
		
		for (int i = 0; i < SSH.RED.getTOTAL(); i++) {
			
			// 存储该维度每个组合出现的概率
			Map<String, Double> curMap = new HashMap<String, Double>();
			
			if (i == 0) {
			
				/**
				 *	第一维
				 *  算出每单个球的概率
				 */
				int curValue = 0;
				for (int j = 1; j <= SSH.RED.getMAX(); j++) {

					// 求出每个球的概率
					String field =  String.format("%02d", j);
					curValue = map.get(field);
					curMap.put(field, ((double)curValue / (double)totalValue));
					
				}
				
			} else {

				/**
				 *	遍历前一维的每个组合
				 *	取出组合中最后的那个球
				 *	和y维的每个球进行组合，算出概率
				 */
				Map<String, Double> preMap = list.get(i - 1);
				if (null == preMap) {
					continue;
				}
				Set<String> set = preMap.keySet();
				for (String item : set) {
					
					// 当前组合的概率
					double curPM = preMap.get(item);
					
					// 找到最后的那个球
					String[] tokens = item.split(":");
					if (tokens == null || tokens.length == 0) {
						continue;
					}
					String lastBall = tokens[tokens.length - 1];
					
					// 算出以这个球开始的每个组合的概率，和pre组合的概率组合，形成完整组合的概率
					totalValue = map.get(lastBall);
					int curValue = 0;
					for (int j = (1 + Integer.parseInt(lastBall)); j <= SSH.RED.getMAX(); j++) {

						// 求出每个组合的概率
						String key = lastBall + ":" + String.format("%02d", j);
						curValue = redisDao.hget(SSH.RED.getRedisKey(), key);
						String field = item + ":" + String.format("%02d", j); 
						curMap.put(field, curPM * ((double)(curValue) / (double)totalValue));
						
					}
					
				}
				
			}
			
			// debug info
			log.info("------------------------------------------------------");
			log.info("第" + (i + 1) + "维的数据:");
			Set<String> set = curMap.keySet();
			for (String item : set) {
				log.info("\t" + item + ":" + curMap.get(item)); 
			}
			list.add(curMap);
			
		}
		
		// 排序获取概率最大的size个组合
		if (list.size() == 0) {
			return null;
		} else {
			Map<String, Double> preMap = list.get(SSH.RED.getTOTAL() - 1);
			ArrayList<Entry<String, Double>> tmp = new ArrayList<Entry<String, Double>>(preMap.entrySet());
			Collections.sort(tmp, new SSHRedProbabilityComparator());
			int n = 0;
			for(Entry<String, Double> e : tmp) {  
	            System.out.println(e.getKey() + "的概率是" + e.getValue());
	            if (n++ >= 10) {
	            	break;
	            }
	        }  
		}
		
		return null;
	}

	
	/**
	 * @author bd17kaka
	 * 对所有的可能性进行排序
	 */
	private class SSHRedProbabilityComparator implements Comparator<Map.Entry<String, Double>> {

		public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
			
			return (o1.getValue() < o2.getValue()) ? 1 : -1;
		}
	}

}
