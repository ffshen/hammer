package org.hammer.wfl.instance; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hammer.cache.ICache;
import org.hammer.exception.RetException; 
import org.hammer.wfl.enums.IStepStatus; 
import org.hammer.wfl.rule.WflRule.WflStep;
import org.hammer.zookeeper.curator.ICuratorClient;
import org.hammer.zookeeper.curator.impl.CuratorLock;
import org.hammer.zookeeper.curator.impl.CuratorLock.CuratorLockSupplier; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IStep extends WflStep{

	private static Logger logger = LoggerFactory.getLogger(IStep.class); 

	private static final String StepInstIdUniqueCodePath = "/wfl/step/instanceid" ;	

	private static final String StepInstIDLockPath = "/wfl/step/lock/" ;

	private static final String COLON = "::" ;
	
	private static final String StepInstIdCacheKey = "I_STEP" + COLON ;
	
    @Autowired
    private CuratorLock lock ;
    
    @Autowired
    private ICuratorClient curatorClient ;
    
	@Autowired
	private ICache cache ;
	
	/**
	 * return code 
	 */
	private static final String ISTEP_CREATE_WFL_ERR_CODE = "200000" ;
	private static final String ISTEP_CREATE_WFL_ERR_MSG  = "创建环节实例出错，" ;
	
	
	/**
	 * action
	 */
	
	public IStep createIStep(IStep iStep) throws RetException { 
		try{
			Long instId = curatorClient.getUniqueCode(StepInstIdUniqueCodePath) ;  
			
		    CuratorLockSupplier<IStep> locksupplier  = ()->{
		    	iStep.setStepInstid(instId);
		    	iStep.setStepStatus(IStepStatus.INIT.getValue()); 
		    	return iStep ;
		    } ;		    
	    	return lock.lock(StepInstIDLockPath + instId ,  locksupplier );
		}
		catch(Exception ex){
			logger.error(ISTEP_CREATE_WFL_ERR_MSG + ex.getMessage(),ex);
			throw new RetException(ISTEP_CREATE_WFL_ERR_CODE,ISTEP_CREATE_WFL_ERR_MSG + ex.getMessage()) ;
		}
	}	
	
	/**
	 *properties 
	 */
	
    private Long stepInstid;    

    private Long wflInstanceid;
    
    private String stepStatus;

    private Integer recId;

    private Integer tryTimes ;

	public Long getStepInstid() {
		return stepInstid;
	}

	public void setStepInstid(Long stepInstid) {
		this.stepInstid = stepInstid;
	}

	public Long getWflInstanceid() {
		return wflInstanceid;
	}

	public void setWflInstanceid(Long wflInstanceid) {
		this.wflInstanceid = wflInstanceid;
	}

	public String getStepStatus() {
		return stepStatus;
	}

	public void setStepStatus(String stepStatus) {
		this.stepStatus = stepStatus;
	}

	public Integer getRecId() {
		return recId;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

	public Integer getTryTimes() {
		return tryTimes;
	}

	public void setTryTimes(Integer tryTimes) {
		this.tryTimes = tryTimes;
	}
    
    

}
