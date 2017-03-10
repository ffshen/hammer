package org.hammer.wfl.rule;
  
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hammer.cache.CacheException;
import org.hammer.cache.ICache;
import org.hammer.concurrency.SimpleAsyncUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;  

@Component
@Configuration
@ConfigurationProperties(
        locations = "classpath:wfl.yml")
public class WflRule {	

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String ORDER = "WFL_ORDER" ;
	
	private static final String CONTROL = "WFL_CONTROL" ;	

	private static final String STEP = "WFL_STEP" ;
	
	private static final String COLON = "::" ;
	
	private static final String OREDERCODEKEY = ORDER + COLON ;

	private static final String CONTROLKEY = CONTROL + COLON ;
	
	private static final String STEPORDERIDKEY = STEP + COLON ;
	
	private List<WflOrder> wflOrders ;
	
	@Autowired
	private ICache cache ;
	
	public static String getWflOrderKey(String orderCode){
		return OREDERCODEKEY + orderCode;
	}
	
	public static String getWflControlKey(Integer wflId ){
		return CONTROLKEY + wflId   ;
	}
	
	public static String getWflStepKey(Integer wflId  ,Integer stepId){
		return STEPORDERIDKEY + wflId + COLON + stepId   ;
	}
	
	public static String getWflStepByWflIdKey(Integer wflId){
		return STEPORDERIDKEY + wflId    ;
	}
	
	/**
	 * 根据流程编号和环节编码定位环节
	 * @param wflId
	 * @param stepCode
	 * @return
	 * @throws CacheException 
	 */
	public List<WflStep> getWflStepByWflAndCode(Integer wflId ,String stepCode) throws CacheException,NullPointerException{
		  		
		@SuppressWarnings("unchecked")
		List<WflStep> steps = 
				Optional
					.of((List<WflStep>) cache.get(getWflStepByWflIdKey(wflId), WflStep.class))
					.get()
					.stream()
					.filter(step->{
							return  StringUtils.equals(stepCode, step.getStepCode()) ;
					})
					.collect(Collectors.toList()) ;
		
		return Optional.of(steps).get() ;
	}	
	
	/**
	 * 根据流程编号和环节编号，查找流程调度
	 * @param wflId
	 * @param step
	 * @return
	 */
	public WflControl getWflControlByWflAndStep(Integer wflId ,Integer step){
		return null ;
	}
	
	/**
	 * 获取流程规则
	 * @param code
	 * @return
	 * @throws CacheException
	 */
	public  WflOrder getWflOrderByCode(String code ) throws CacheException{
		return (WflOrder) cache.get(getWflOrderKey(code), WflOrder.class) ;
	}
	
	//缓存调度规则
	private void cacheControls(Integer wflId , List<WflControl> wflControls){
		Optional
		.ofNullable(wflControls)
		.ifPresent(controls->{
			try {
				cache.set(getWflControlKey(wflId) , controls );
			} catch (Exception e) {
				e.printStackTrace();
			}										
		}); 
	}
	
	//缓存环节规则
	private void cacheSteps(Integer wflId , List<WflStep> wflSteps) {
		Optional
		.ofNullable(wflSteps)
		.ifPresent(steps->{
				try {
					cache.set(getWflStepByWflIdKey(wflId) , steps );
				} catch (Exception e) {
					e.printStackTrace();
				}
				steps
					.stream()
					.forEach(step->{
						try {
							cache.set(getWflStepKey(wflId,step.getStepId()) , step );
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
			}); 		
	}
	
	/**
	 * 缓存规则
	 * 缓存流程，调度，环节
	 */
	@PostConstruct
	public void cache(){	
			CompletableFuture<?>[] 
                    cfs = wflOrders.stream()
					.map(order->SimpleAsyncUtils.supplyAsyncWithPool(()->{
							try {
								//cache order
								cache.set(getWflOrderKey(order.getWflCode()) , order );
								//cache controls
								cacheControls(order.getWflId(),order.getWflControls()) ;
								//cache steps
								cacheSteps(order.getWflId(),order.getWflSteps()) ;
							} catch (Exception e) {
								e.printStackTrace();
							}
							return order ;
						})).toArray(CompletableFuture[]::new);
            CompletableFuture.allOf(cfs).join(); 
	}
	 
	public List<WflOrder> getWflOrders() {
		return wflOrders;
	}

	public void setWflOrders(List<WflOrder> wflOrders) {
		this.wflOrders = wflOrders;
	}

	public static class WflStep{
		
		Integer stepId ;
		
		String stepCode ;
		
		String stepType ;
		
		String stepName ;
		
		Boolean isAuto ;

		public Integer getStepId() {
			return stepId;
		}

		public void setStepId(Integer stepId) {
			this.stepId = stepId;
		}

		public String getStepCode() {
			return stepCode;
		}

		public void setStepCode(String stepCode) {
			this.stepCode = stepCode;
		}

		public String getStepType() {
			return stepType;
		}

		public void setStepType(String stepType) {
			this.stepType = stepType;
		}

		public String getStepName() {
			return stepName;
		}

		public void setStepName(String stepName) {
			this.stepName = stepName;
		}

		public Boolean getIsAuto() {
			return isAuto;
		}

		public void setIsAuto(Boolean isAuto) {
			this.isAuto = isAuto;
		}	
	}

	public static class WflControl{
		private Integer controlId ;
		
		private Integer stepId ;
		
		private Integer nextStepId ;

		public Integer getControlId() {
			return controlId;
		}

		public void setControlId(Integer controlId) {
			this.controlId = controlId;
		}

		public Integer getStepId() {
			return stepId;
		}

		public void setStepId(Integer stepId) {
			this.stepId = stepId;
		}

		public Integer getNextStepId() {
			return nextStepId;
		}

		public void setNextStepId(Integer nextStepId) {
			this.nextStepId = nextStepId;
		}	
	}

	public static class WflOrder {
		
		private Integer wflId ;
		
		private String wflName ;
		
		private String wflCode ;		

		private List<WflControl> wflControls ;

		private List<WflStep> wflSteps ;		
	 
		public List<WflStep> getWflSteps() {
			return wflSteps;
		}

		public void setWflSteps(List<WflStep> wflSteps) {
			this.wflSteps = wflSteps;
		}

		public List<WflControl> getWflControls() {
			return wflControls;
		}

		public void setWflControls(List<WflControl> wflControls) {
			this.wflControls = wflControls;
		}

		public Integer getWflId() {
			return wflId;
		}

		public void setWflId(Integer wflId) {
			this.wflId = wflId;
		}

		public String getWflName() {
			return wflName;
		}

		public void setWflName(String wflName) {
			this.wflName = wflName;
		}

		public String getWflCode() {
			return wflCode;
		}

		public void setWflCode(String wflCode) {
			this.wflCode = wflCode;
		}		
		
	}
}
