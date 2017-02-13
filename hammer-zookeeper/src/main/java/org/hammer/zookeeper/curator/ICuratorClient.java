package org.hammer.zookeeper.curator;

import org.hammer.zookeeper.curator.impl.CuratorLock.CuratorLockSupplier;

public interface ICuratorClient {
	  
	
	
	/**
	 * 获取流水号
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public Long getUniqueCode(String path) throws Exception ;
	 
	/**
	 * 分布式锁
	 * @param path
	 * @param suppliper
	 * @throws Exception
	 */
	public void lock(String path,  CuratorLockSupplier suppliper) throws Exception ; 
	
	/**
	 * 分布式锁
	 * @param path
	 * @param lockSec
	 * @param suppliper
	 * @throws Exception
	 */
	public void lock(String path, Integer lockSec ,CuratorLockSupplier suppliper) throws Exception ;
}
