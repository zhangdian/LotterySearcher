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
import com.bd17kaka.LotterySearcher.constat.SSH.RedDistributed;
import com.bd17kaka.LotterySearcher.dao.RedisDao;
import com.bd17kaka.LotterySearcher.po.SSHNewCombination;

/**
 * @author bd17kaka
 * 详细注释见 {@code SSHCalProbabilityService}注释
 * 这个实现只考虑长度为2-3的组合
 * 
 * 引入球号分布的概念
 */
@Service(value = "sshCalProbabilityServiceSimpleSpan3V5Impl")
public class SSHCalProbabilityServiceSimpleSpan3V5Impl implements SSHCalProbabilityService {

	private static final Log log = LogFactory.getLog(SSHCalProbabilityServiceSimpleSpan3V5Impl.class);
	
	@Resource(name = "redisDao")
	private RedisDao redisDao;

	public double calRedProbability(String input) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<SSHNewCombination> calRedMostProbability(int size, String...strings) {

		// 参数检查，找到输入的红球分布
		if (null == strings) {
			return null;
		}
		
		int sshRedDistribute = 0;
		String strSSHRedDistribute = strings[0];
		try {
			sshRedDistribute = Integer.parseInt(strSSHRedDistribute);
		} catch (Exception e) {
			return null;
		}
		
		RedDistributed tmp = RedDistributed.getRedDistributed(sshRedDistribute);
		if (null == tmp) {
			return null;
		}
		
		// 从Redis中获取所有的值
		Map<String, String> map = redisDao.hgetAll(RedDistributed.getRedisKeyOfSimpleSpan3V5(sshRedDistribute));
		
		// TODO: 瓶装成要返回的对象。这里的返回值是不是要改成Map
		
		return null;
	}
}
