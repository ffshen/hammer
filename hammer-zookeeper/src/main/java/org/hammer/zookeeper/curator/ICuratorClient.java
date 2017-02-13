package org.hammer.zookeeper.curator;
 

public interface ICuratorClient {
	  
	
	
	/**
	 * 获取流水号
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public Long getUniqueCode(String path) throws Exception ;
	 
	
}
