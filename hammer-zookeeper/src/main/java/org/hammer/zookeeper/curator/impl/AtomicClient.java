package org.hammer.zookeeper.curator.impl;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service("AtomicClient")
public class AtomicClient {
	
	private static final Logger logger =  Logger.getLogger(AtomicClient.class);
	

	@Autowired
	@Qualifier("singleCaseClient")
	protected CuratorFramework singleCaseClient;
	
	 
	@Autowired
	@Qualifier("getRetryPolicy")
	protected RetryPolicy retrypolicy ;


	public Long getAtomic(String path)	throws Exception {
		DistributedAtomicLong atomic = null ;
		Long retValue = -1L ;
		try{			
			atomic = new DistributedAtomicLong(singleCaseClient, path, retrypolicy );
			AtomicValue<Long> value = atomic.increment() ;
			if(value.succeeded()){
				retValue = value.postValue();				
			}
		}
		catch(Exception ex){
			logger.error(ex);
			throw ex ;
		}
		return retValue ;
	}

}
