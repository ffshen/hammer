package org.hammer.wfl.verticle;

import java.util.Objects;
import java.util.Optional;

import org.hammer.utils.JsonUtil;
import org.hammer.wfl.enums.RStepCode;
import org.hammer.wfl.instance.IOrder;
import org.hammer.wfl.instance.IStep;
import org.hammer.wfl.rule.WflRule;
import org.hammer.wfl.rule.WflRule.WflOrder;
import org.hammer.wfl.rule.WflRule.WflStep; 
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.BooleanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hammer.exception.RetException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

@Component
public class IOrderVerticle extends AbstractVerticle {	

	private static Logger logger = LoggerFactory.getLogger(IOrderVerticle.class); 
	
	  public  static final String CREATE_WFL = "IOrderVerticle.createWfl" ;
	  
		/**
		 * return code 
		 */
		private static final String IORDERVERTICLE_WFL_ORDER_NOTFOUND_ERR_CODE = "200000" ;
		private static final String IORDERVERTICLE_WFL_ORDER_NOTFOUND_ERR_MSG  = "无法根据WFL_CODE定位流程规则，" ;
	  
	  @Autowired
	  WflRule rule ;
	  
	  @Autowired
	  IOrder iOrder ;

	  @Override
	  public void start(Future<Void> startFuture) throws Exception { 
		  vertx.eventBus().<String>consumer(CREATE_WFL, createWflFunction).completionHandler(startFuture.completer());
	  }
	  
	  
	  	//创建订单逻辑
	    private  Handler<Message<String>> createWflFunction = (  Message<String> msg )->{
	    	try{
	    		String body = msg.body() ;
	        	//将消息转换成对象
	    		CreateWflReq req = JsonUtil.toObject(body, CreateWflReq.class) ;
	    		//根据code查找流程规格
	    		WflOrder rOrder = rule.getWflOrderByCode(req.getCode()) ;	    		
	    		if(BooleanUtils.isTrue(Objects.isNull(rOrder))){
	    			throw new RetException(IORDERVERTICLE_WFL_ORDER_NOTFOUND_ERR_CODE
	    						,IORDERVERTICLE_WFL_ORDER_NOTFOUND_ERR_MSG + req.getCode()) ;
	    		}
	    		//创建订单实例
	    		IOrder order = iOrder.createIOrder(rOrder, req.getMemo()) ;
	    		//发送创建开始环节	    		
	    		WflStep startstep = Optional
	    								.of(rule.getWflStepByWflAndCode(rOrder.getWflId(),RStepCode.START.getValue()))
	    								.get().get(0) ;
	    		IStep iStep = new IStep() ;
	    		BeanUtils.copyProperties(startstep, iStep);
	    		iStep.setWflInstanceid(order.getWflInstanceid());
	    		vertx.eventBus().<String>send(IStepVerticle.CREATE_STEP, JsonUtil.toJson(iStep), reply->{	
    	          	if (reply.succeeded()) { 
    	          		IStep isteprepay = JsonUtil.toObject(reply.result().body(), IStep.class)  ;
    	          		iOrder.addIStep(order,isteprepay) ;
    		    		//返回订单实例完成消息    		    		
    		    		msg.reply(JsonUtil.toJson(order));
    	            }

    	          	if (reply.failed()) {  
    	          		logger.error("IStepVerticle replay CREATE_STEP error ");
    	          		msg.reply(reply.cause());
    	          	}
	    		});
	    		
	    		//发送创建第一条调度消息
//	    		vertx.eventBus().<String>send(IControlVerticle.CREATE_CONTROL, buffer.toString("utf8"), reply->{	  
//	    			
//	    		});
	    		//发送记录日志消息
	    		

	    	}
	    	catch(RetException retEx){
	    		msg.fail(Integer.parseInt(retEx.getRetCode()), retEx.getResMsg());
	    	}
	    	catch(Exception ex){
	    		msg.fail(-1, ex.getMessage());
	    	}
	    };
	    
	    
	    public static class CreateWflReq {
	    		
	    		private String code  ;
	    		
	    		private String memo ;

				public String getCode() {
					return code;
				}

				public void setCode(String code) {
					this.code = code;
				}

				public String getMemo() {
					return memo;
				}

				public void setMemo(String memo) {
					this.memo = memo;
				}
	    	
	    }
}
