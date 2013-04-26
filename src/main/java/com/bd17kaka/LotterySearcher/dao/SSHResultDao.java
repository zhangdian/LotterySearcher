package com.bd17kaka.LotterySearcher.dao;

import com.bd17kaka.LotterySearcher.po.SSHResult;


/**
 * 双色球开奖结果DAO
 * @author bd17kaka
 */
public interface SSHResultDao {
	/**
	 * 插入开奖结果信息
	 * @param user
	 * @return
	 */
	boolean insert(SSHResult sshResult);

}
