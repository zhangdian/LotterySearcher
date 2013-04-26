package com.bd17kaka.LotterySearcher.service;

/**
 * @author bd17kaka
 * 计算球序列出现可能性的Service
 */
public interface SSHCalProbabilityService {
	
	/**
	 * 检查输入的红球号的可能性
	 * @param input
	 * @return
	 */
	double calRedProbability(String input);
	
}
