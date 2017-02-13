package org.hammer.zookeeper.curator.impl;
 

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong; 
import org.apache.log4j.Logger;
import org.hammer.zookeeper.curator.ICuratorClient;
import org.hammer.zookeeper.curator.impl.CuratorLock.CuratorLockSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service("CuratorClient")
public class CuratorClient implements ICuratorClient{
	
	private static final Logger logger =  Logger.getLogger(CuratorClient.class);
	
	@Autowired
	@Qualifier("singleCaseClient")
	protected CuratorFramework singleCaseClient;
	
	 
	@Autowired
	@Qualifier("getRetryPolicy")
	protected RetryPolicy retrypolicy ;
	
	@Autowired
	private CuratorLock lockService ; 

 
	private  DistributedAtomicLong getAtomicLong(String path) throws Exception {
		return new DistributedAtomicLong(singleCaseClient, path, retrypolicy );
	}

	@Override
	public Long getUniqueCode(String path) throws Exception {
		Long retValue = -1L ;	 
		try{
			DistributedAtomicLong atomic = getAtomicLong(path) ;
			AtomicValue<Long> value = atomic.increment() ;			
			if(value.succeeded()){
				retValue = value.postValue();				
			}
		}
		catch(Exception ex){
			logger.error(ex);
			throw ex ;
		}
		return retValue;
	}

	@Override
	public void lock(String path, CuratorLockSupplier suppliper) throws Exception {
		lockService.lock(path, suppliper);		
	}

	@Override
	public void lock(String path, Integer lockSec, CuratorLockSupplier suppliper) throws Exception {
		lockService.lock(path, lockSec, suppliper);		
	}





	
	
}
