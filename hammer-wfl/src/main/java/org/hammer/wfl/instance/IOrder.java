package org.hammer.wfl.instance;

import java.util.Date;
import java.util.List; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hammer.cache.ICache; 
import org.hammer.exception.RetException;
import org.hammer.utils.JsonUtil;
import org.hammer.wfl.enums.IOrderStatus;
import org.hammer.wfl.rule.WflRule.WflOrder;
import org.hammer.zookeeper.curator.ICuratorClient; 
import org.hammer.zookeeper.curator.impl.CuratorLock;
import org.hammer.zookeeper.curator.impl.CuratorLock.CuratorLockSupplier;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class IOrder {	

		private static Logger logger = LoggerFactory.getLogger(IOrder.class);   
	
		private static final String OrderInstIdUniqueCodePath = "/wfl/order/instanceid" ;
		
		private static final String OrderInstIDLockPath = "/wfl/order/lock/" ;

		private static final String COLON = "::" ;
		
		private static final String OrderInstIdCacheKey = "I_ORDER" + COLON ;
				
		/**
		 * return code 
		 */
		private static final String IORDER_CREATE_WFL_ERR_CODE = "100000" ;
		private static final String IORDER_CREATE_WFL_ERR_MSG  = "创建流程实例出错，" ;
	
	    @Autowired
	    private CuratorLock lock ;
	    
	    @Autowired
	    private ICuratorClient curatorClient ;
	    
		@Autowired
		private ICache cache ;
	
		/**
		 * action
		 */
		
		public IOrder addIStep(IOrder order ,IStep step){
			try{
			    CuratorLockSupplier<IOrder> locksupplier  = ()->{  
			    	order.getIsteps().add(step);
					//cache
					logger.info("addIStep:{}",JsonUtil.toJson(order));
					cache.set(getIOrderKey(order.getWflInstanceid()), JsonUtil.toJson(order));
			    	return order ;	
			    } ; 
		    	return lock.lock(OrderInstIDLockPath + order.getWflInstanceid() ,  locksupplier );
			}
			catch(Exception ex){
				logger.error(IORDER_CREATE_WFL_ERR_MSG + ex.getMessage(),ex);
				throw new RetException(IORDER_CREATE_WFL_ERR_CODE,IORDER_CREATE_WFL_ERR_MSG + ex.getMessage()) ;
			}
		}
		
		public IOrder createIOrder(WflOrder rOrder,String wflInfo) throws RetException {
			try{
				Long instId = curatorClient.getUniqueCode(OrderInstIdUniqueCodePath) ;  
				
			    CuratorLockSupplier<IOrder> locksupplier  = ()->{
						IOrder iOrder = new IOrder() ;
						BeanUtils.copyProperties(rOrder, iOrder);
						iOrder.setWflInstanceid(instId);
						iOrder.setCreateDate(new Date());
						iOrder.setWflInfo(wflInfo);
						iOrder.setWflInfo(wflInfo);
						iOrder.setWflStatus(IOrderStatus.EXE.getValue());
						//cache
						cache.set(getIOrderKey(instId), JsonUtil.toJson(iOrder));
						return iOrder ;
	
			    } ;
			    
		    	return lock.lock(OrderInstIDLockPath + instId ,  locksupplier );
			}
			catch(Exception ex){
				logger.error(IORDER_CREATE_WFL_ERR_MSG + ex.getMessage(),ex);
				throw new RetException(IORDER_CREATE_WFL_ERR_CODE,IORDER_CREATE_WFL_ERR_MSG + ex.getMessage()) ;
			}
		}
		
		/**
		 * get cache key
		 */
		
		public static String getIOrderKey(Long instId){
			return OrderInstIdCacheKey  + instId;
		}
		
		/**
		 *properties 
		 */
	
	    private Long wflInstanceid;

	    private String wflStatus;

	    private Integer wflId;

	    private String wflName;

	    private Integer orderId;

	    private String wflCode;

	    private Date createDate;

	    private Date finishDate;

	    private String wflInfo;
	    
	    private List<IStep> isteps = Lists.newArrayList();
	    
	    

		public List<IStep> getIsteps() {
			return isteps;
		}

		public void setIsteps(List<IStep> isteps) {
			this.isteps = isteps;
		}

		public Long getWflInstanceid() {
			return wflInstanceid;
		}

		public void setWflInstanceid(Long long1) {
			this.wflInstanceid = long1;
		}

		public String getWflStatus() {
			return wflStatus;
		}

		public void setWflStatus(String wflStatus) {
			this.wflStatus = wflStatus;
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

		public Integer getOrderId() {
			return orderId;
		}

		public void setOrderId(Integer orderId) {
			this.orderId = orderId;
		}

		public String getWflCode() {
			return wflCode;
		}

		public void setWflCode(String wflCode) {
			this.wflCode = wflCode;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public Date getFinishDate() {
			return finishDate;
		}

		public void setFinishDate(Date finishDate) {
			this.finishDate = finishDate;
		}

		public String getWflInfo() {
			return wflInfo;
		}

		public void setWflInfo(String wflInfo) {
			this.wflInfo = wflInfo;
		}

	    
}
