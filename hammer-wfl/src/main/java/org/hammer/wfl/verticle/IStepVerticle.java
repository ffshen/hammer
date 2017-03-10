package org.hammer.wfl.verticle;

import org.apache.log4j.Logger;
import org.hammer.exception.RetException;
import org.hammer.utils.JsonUtil; 
import org.hammer.wfl.instance.IStep; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

@Service
public class IStepVerticle extends AbstractVerticle {

	private static final Logger logger =  Logger.getLogger(IStepVerticle.class);

	  public  static final String CREATE_STEP = "IStepVerticle.createStep" ;
	  
	  @Autowired
	  private IStep iStep;
	  
	  @Override
	  public void start(Future<Void> startFuture) throws Exception { 
		  vertx.eventBus().<String>consumer(CREATE_STEP, createStepFunction).completionHandler(startFuture.completer());
	  }
	  

	    private  Handler<Message<String>> createStepFunction = (  Message<String> msg )->{
	    	logger.info("createStepFunction");
	    	try{
	    		IStep step = JsonUtil.toObject(msg.body(), IStep.class) ;
	    		msg.reply(JsonUtil.toJson(iStep.createIStep(step)));
	    	}
	    	catch(RetException retEx){
	    		logger.error("createStepFunction RetException:"+retEx.getResMsg(),retEx);
	    		msg.fail(Integer.parseInt(retEx.getRetCode()), retEx.getResMsg());
	    	}
	    	catch(Exception ex){
	    		logger.error("createStepFunction Exception:"+ex.getMessage(),ex);
	    		msg.fail(-1, ex.getMessage());
	    	}
	    };	
	    

}
