package org.hammer.wfl.verticle;
 
import org.hammer.exception.RetException; 
import org.hammer.wfl.rule.WflRule.WflControl; 

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

public class IControlVerticle extends AbstractVerticle {

	  public  static final String CREATE_CONTROL = "IControlVerticle.createControl" ;
	  
	  
	  @Override
	  public void start(Future<Void> startFuture) throws Exception { 
		  vertx.eventBus().<String>consumer(CREATE_CONTROL, createControlFunction).completionHandler(startFuture.completer());
	  }
	  
	    private  Handler<Message<String>> createControlFunction = (  Message<String> msg )->{
	    	try{
	    		
	    		
//	    		msg.reply(JsonUtil.toJson(order));
	    	}
	    	catch(RetException retEx){
	    		msg.fail(Integer.parseInt(retEx.getRetCode()), retEx.getResMsg());
	    	}
	    	catch(Exception ex){
	    		msg.fail(-1, ex.getMessage());
	    	}
	    };	  
	    
	    
	    public static class CreateControlReq extends WflControl{
	    	
	    	private Long wflInstanceId ;

			public Long getWflInstanceId() {
				return wflInstanceId;
			}

			public void setWflInstanceId(Long wflInstanceId) {
				this.wflInstanceId = wflInstanceId;
			}
	    	
	    }
}
