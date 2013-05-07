package com.bd17kaka.LotterySearcher.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bd17kaka.LotterySearcher.po.SSHNewCombination;

/**
 * @author bd17kaka
 * 计算球序列出现可能性的Service
 */
public interface SSHCalProbabilityService {
	
	/**
	 * 检查输入的红球号的可能性
	 * 球号长度为1-6
	 * @param input 每个球号以","分隔
	 * @return
	 */
	double calRedProbability(String input);
	
	/**
	 * 获取size个最后可能出现的球号序列
	 * @param size 获取最可能出现的序列的个数
	 * @param strings 只有{@link SSHCalProbabilityServiceSimpleSpan3V5Impl}使用该参数
	 * 			strings[0]: 分布类型
	 * @return
	 */
	List<SSHNewCombination> calRedMostProbability(int size, String...strings);
	
	/**
	 * @author bd17kaka
	 * 对所有的可能性进行排序
	 */
	public class SSHRedProbabilityComparator implements Comparator<Map.Entry<String, Double>> {

		public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
			
			return (o1.getValue() < o2.getValue()) ? 1 : -1;
		}
	}
	
}
