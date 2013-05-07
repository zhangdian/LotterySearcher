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
@Service(value = "sshCalProbabilityServiceSimpleSpan3Impl")
public class SSHCalProbabilityServiceSimpleSpan3Impl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleSpan3Impl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 分六维进行计算
	 * 第一维计算单个球出现的概率
	 * 第二维以第一维的结果为x维，以所有红球为y维，计算所有二维组的概率，x维的概率从第一维的结果集中获取
	 * 同理，第3-5维都以上一维的结果为x维，以所有红球为y维，计算当前维度球组合的概率，x维的概率从上一维的结果集中获取
	 * 第6维和2-5维一样，只是要注意如果组合出现过，那么其概率就直接置为0
	 * 
	 * 在计算过程中要注意以下几点：
	 * 	球的排列顺序要严格递增的顺序
	 * 	如果顺序不对，那么该组合的概率为0
	 * 	如果在一个组合中出现有相同的球号，概率为0
	 * 
	 * 考虑长度范围是2-3的组合
	 */
	public List<SSHNewCombination> calRedMostProbability(int size, String...strings) {
		
		/**
		 * 每个维度的所有球号的出现概率存在一个Map中，所有维度组成一个List，结构如下：
		 * 	List<Map<String, double>>
		 * 
		 * 每单个球或者组合的出现次数直接去Redis中query
		 * 将来比较一下存储在Redis中和内存中的性能差别
		 */
		List<Map<String, Double>> list = new ArrayList<Map<String, Double>>();
		
		
		for (int i = 0; i < SSH.RED.getTOTAL(); i++) {
			
			// 存储该维度每个组合出现的概率
			Map<String, Double> curMap = new HashMap<String, Double>();
			
			if (i == 0) {
				
				// 单个球出现总数
				int totalValue = 0;
				int curValue = 0;
				for (int j = 1; j <= SSH.RED.getMAX(); j++) {

					String field =  String.format("%02d", j);
					curValue = redisDao.hget(SSH.RED.getRedisKey(), field);
					totalValue += curValue;
				}

				// 单个球每个球的概率
				for (int j = 1; j <= SSH.RED.getMAX(); j++) {

					// 求出每个球的概率
					String field =  String.format("%02d", j);
					curValue = redisDao.hget(SSH.RED.getRedisKey(), field);
					curMap.put(field, ((double)curValue / (double)totalValue));
					
				}
				
			} else {

				/**
				 *	遍历前一维的每个组合
				 *	取出组合中最后的那个球, 和y维的每个球进行组合, 得出一个概率
				 *	如果原组合的长度>=2的话，再取出最后的两个球，和y维当前球再计算一个概率
				 *	两个概率相乘
				 */
				Map<String, Double> preMap = list.get(i - 1);
				if (null == preMap) {
					continue;
				}
				Set<String> set = preMap.keySet();
				for (String item : set) {
					
					// 当前组合的概率
					double curPM = preMap.get(item);
					
					// 找到最后的那个球,以及最后两个球的组合
					String[] tokens = item.split(":");
					int length = tokens.length;
					if (tokens == null || length == 0) {
						continue;
					}
					String lastBall = tokens[length - 1];
					String lastTwoBall = null;
					if (length >= 2) {
						lastTwoBall = tokens[length - 2] + ":" + tokens[length - 1];
					}
					
					// 算出以这个球开始的每个组合的概率，和pre组合的概率组合，形成完整组合的概率
					// 如果pre组合的长度>=2，那么还要计算pre组合的后两个球和当前球组合起来的概率
					int totalValue = redisDao.hget(SSH.RED.getRedisKey(), lastBall);
					int curValue = 0;
					for (int j = (1 + Integer.parseInt(lastBall)); j <= SSH.RED.getMAX(); j++) {

						// 求出每个组合的概率
						String field = lastBall + ":" + String.format("%02d", j);
						curValue = redisDao.hget(SSH.RED.getRedisKey(), field);
						curPM = curPM * ((double)(curValue) / (double)totalValue);
						if (lastTwoBall != null) {
							field = lastTwoBall + ":" + String.format("%02d", j);
							curValue = redisDao.hget(SSH.RED.getRedisKey(), field);
							int tmpTotalBall = redisDao.hget(SSH.RED.getRedisKey(), lastTwoBall);
							curPM = curPM * ((double)(curValue) / (double)tmpTotalBall);
						}
						
						field = item + ":" + String.format("%02d", j); 
						curMap.put(field, curPM);
						
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

}
