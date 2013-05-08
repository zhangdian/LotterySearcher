package com.bd17kaka.LotterySearcher.service;

import java.util.Map;

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
	 * 数据从Redis获取
	 * @param size 获取最可能出现的序列的个数 TODO:最大数现在写死了，都是10个
	 * @param strings 只有{@link SSHCalProbabilityServiceSimpleSpan3V5Impl}使用该参数
	 * 			strings[0]: 分布类型
	 * @return
	 */
	Map<String, Double> calRedMostProbability(int size, String...strings);
	
}
