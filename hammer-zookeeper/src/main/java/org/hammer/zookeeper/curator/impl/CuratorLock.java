package org.hammer.zookeeper.curator.impl;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hammer.exception.RetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
 

/**
 * 
 * 返回ZooKeeper Curator Client.Service可以通过getZkClient方法对Znode进行操作。
 * 
 * 具体Znode的Crud,start,close不另外封装
 * 
 * 继承虚拟类ZkClient.另外实现getNameSpace方法设置NameSpace.本例.NameSpace通过配置文件配置.
 * 
 * 注入ConnectionStateListener，完成连接异常的处理.
 * 
 * 		本例注入DemoConnectionStateListener. 
 * 		DemoConnectionStateListener的异常处理是重连ReconnectHandle。如果需要，可以编写其他实现IErrorHandle的类完成异常处理。
 * 
 * @author xiang
 *
 */
@Service
public class CuratorLock	{

    Logger logger = LoggerFactory.getLogger(this.getClass());   
	
	private static final Integer LOCK_SECONDS = 1000 ;
	
	
	@Autowired
	@Qualifier("singleCaseClient")
	protected CuratorFramework singleCaseClient;
	
	
	public CuratorLock(){
		super();
	}
	 
	public InterProcessMutex getInterProcessMutexLock(String path) throws Exception {
		try{
			return new InterProcessMutex(singleCaseClient, path);
		}
		catch(Exception ex){
			logger.error("CuratorLockImpl getInterProcessMutexLock error :",ex);
			throw ex ;
		}
	}
	
	public void acquirelock(InterProcessMutex lock,Integer lockSec ) throws Exception {		
		logger.info("acquirelock");
        if ( !lock.acquire(lockSec, TimeUnit.SECONDS) )
        {
            throw new IllegalStateException(" could not acquire the lock");
        }
	}	

	public void acquirelock(InterProcessMutex lock) throws Exception {	
		acquirelock(lock , LOCK_SECONDS ) ;
	}
	
	public void releaselock(InterProcessMutex lock) throws Exception {
		logger.info("releaselock");		
		if(BooleanUtils.isFalse(Objects.isNull(lock))){	
			if(lock.isAcquiredInThisProcess()){
				lock.release();
				lock = null ;
			}
		}
	}	

	public void lock(String path,  CuratorLockSupplier suppliper) throws Exception {
		lock(path, LOCK_SECONDS, suppliper ) ;
	}
	
	public void lock(String path, Integer lockSec ,CuratorLockSupplier suppliper) throws Exception {
		InterProcessMutex lock = null ;
		try{			
			lock = new InterProcessMutex(singleCaseClient, path);	
			acquirelock(lock) ; 
			suppliper.get() ;
		}
		catch(RetException ret){
			logger.error(ret.getResMsg(),ret);
			throw ret ;			
		}
		catch(Exception ex){
			logger.error("- CuratorLock Lock Error . ",ex);
			throw new Exception(ex) ;
		}
		finally{
			releaselock(lock) ;
		}
	}
	
    @FunctionalInterface
    public interface CuratorLockSupplier {

        Object get() throws RetException, Exception;
    }

}
