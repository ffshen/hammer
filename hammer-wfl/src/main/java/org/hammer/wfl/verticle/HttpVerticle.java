package org.hammer.wfl.verticle;
  
import org.hammer.exception.RetException;
import org.hammer.mvc.DefaultWebApiResult;
import org.hammer.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

@Component
public class HttpVerticle extends AbstractVerticle {

	  Logger logger = LoggerFactory.getLogger(this.getClass());  	   
	  
	  @Override
	  public void start() throws Exception {
	    Router router = Router.router(vertx);

	    router.route(HttpMethod.POST,"/rest/wfl/createWfl").handler(context->{	
	    	HttpServerRequest request = context.request() ;	
	    	HttpServerResponse response = context.response() ; 
	                  	  	  
	    	request.bodyHandler(buffer->{	
	    		 
	    		JsonObject params = buffer.toJsonObject() ;	    		
	    		params.fieldNames().forEach(fieldname->{
	    			logger.info("params fieldname:{},value:{}",fieldname, params.getString(fieldname));
	    		}); 	  
	    		vertx.eventBus().<String>send(IOrderVerticle.CREATE_WFL, buffer.toString("utf8"), reply->{	    			
	    	          	if (reply.succeeded()) { 
	    	        	  	response.end(JsonUtil.toJson(DefaultWebApiResult.success(reply.result().body())));
	    	            } else {
	    	            	if(reply.cause() instanceof RetException){
	    	            		RetException exception = (RetException)reply.cause() ;
	    	            		response
	    	            			.setStatusCode(500)
	    	            			.end(JsonUtil.toJson(DefaultWebApiResult.failure(exception.getRetCode(), exception.getResMsg())));
	    	            	}
	    	            	else{
	    	            		response
	    	            			.setStatusCode(500)
	    	            			.end(JsonUtil.toJson(DefaultWebApiResult.failure("500", reply.cause().getMessage())));
	    	            	}
	    	            }	    			
	    		});
	    	}) ;	    	 
	    }); 
	    
	    
	    vertx.createHttpServer()
	    	.requestHandler(router::accept)
	    	.listen(9001,res->{
	    		if (res.succeeded()) {
	    			logger.info("Server is now listening!");
	    		} else {
	    			logger.info("Failed to bind!");
	    		}
	    	});
	  }
	   
}
