package com.bd17kaka.LotterySearcher.service;

import java.util.List;

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
	 * 
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
	 * 根据不同的实现，考虑的长度不一样：
	 * {@code SSHCalProbabilityServiceSimpleSpan2Impl}: 只考虑长度为2的组合
	 * {@code SSHCalProbabilityServiceSimpleSpan3Impl}: 考虑长度范围是2-3的组合
	 * 上面两者都不使用"古德-图灵估计"算法
	 * 
	 * @param size 获取最可能出现的序列的个数
	 * @return
	 */
	List<SSHNewCombination> calRedMostProbability(int size);
	
}
