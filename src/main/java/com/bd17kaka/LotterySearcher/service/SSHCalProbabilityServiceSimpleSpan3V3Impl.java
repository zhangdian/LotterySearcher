package com.bd17kaka.LotterySearcher.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

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

	public Map<String, Double> calRedMostProbability(int size, String...strings) {
		
		return null;
	}

}
