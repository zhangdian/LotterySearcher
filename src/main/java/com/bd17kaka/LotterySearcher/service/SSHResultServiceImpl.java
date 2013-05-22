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
import com.bd17kaka.LotterySearcher.dao.SSHResultDao;
import com.bd17kaka.LotterySearcher.po.SSHNewCombination;

/**
 * @author bd17kaka
 */
@Service(value = "sshResultService")
public class SSHResultServiceImpl implements SSHResultService {

	private static final Log log = LogFactory.getLog(SSHResultServiceImpl.class);
	
	@Resource(name = "sshResultDao")
	private SSHResultDao sshResultDao;

	public long countByDistributeV3(String distribute) {

		return sshResultDao.countByDistributeV3(distribute);
	}

}
