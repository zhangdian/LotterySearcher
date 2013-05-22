package com.bd17kaka.LotterySearcher.service;

import java.util.Map;

/**
 * @author bd17kaka
 * SSH历史结果Service
 */
public interface SSHResultService {
	
	/**
	 * 获取某个分布的出现次数，V3
	 * @param distribute
	 * @return
	 */
	long countByDistributeV3(String distribute);
	
}
