package org.hammer.proxy.verticle;
 
import org.hammer.proxy.properties.ProxyProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

@Component
public class ProxyVerticle extends AbstractVerticle {

	  Logger logger = LoggerFactory.getLogger(this.getClass());  
	  
	  @Autowired
	  private ProxyProperties properties ;
	  
	  @Override
	  public void start() throws Exception {
	    Router router = Router.router(vertx);

	    router.route(HttpMethod.POST,"/hammer/:proxyDomain/:proxyPath/").handler(context->{	
	    	HttpServerRequest request = context.request() ;	
	    	HttpServerResponse response = context.response() ;
	    	MultiMap headers = request.headers() ; 
	        	
	        if (logger.isInfoEnabled()) {
	            logger.info(" request -> url: {},  method: {} "
	            		, request.uri()  
	            		, request.method() );
	        	headers.forEach(header->{    		
	        		logger.info(" request -> header key:{},value:{}" ,header.getKey(), header.getValue());    		
	        	});      	            
	        }	           	  	 
	    	
	    	String proxyDomain = request.getParam("proxyDomain");
	    	String proxyPath = request.getParam("proxyPath");	    	

        	HttpClientOptions options = new HttpClientOptions().setKeepAlive(true);
        	HttpClient client = vertx.createHttpClient(options);
	    	HttpClientRequest proxyRequest 
	        	= client.request(request.method(), getPort(proxyDomain) ,getHost(proxyDomain),getUri(proxyPath) , proxyResponse -> {
	        	  response
				  	.setChunked(true)
	    		  	.headers()
	    		  	.setAll(proxyResponse.headers()) ;
	        	  
	    		  proxyResponse.handler(proxyRespBuffer->{
	    			  try{
	    				  response
		        		  	.setStatusCode(proxyResponse.statusCode())
	    				  	.write(proxyRespBuffer) ;
	    			  }
	    			  catch(Exception e){
	    				  logger.error("proxy write msg error .",e);
	    				  throw e ;
	    			  }
	    		  });
	    		  proxyResponse.endHandler((v) -> response.end());
	    	}) ;	 
	        proxyRequest
	        	.exceptionHandler(ex->{
				  	logger.error("proxyRequest error .",ex);	            	
	        	})
	        	.setChunked(true)
    			.headers().setAll(headers)
			  	; 
		    	
		    //取body
		    request.handler(bodybuffer->{ 
			        if (logger.isInfoEnabled()) {
			            logger.info(" request -> body :{}" ,	bodybuffer);   	
			        }
		        	proxyRequest.write(bodybuffer);
		    }) ;
		    request.endHandler((v) -> proxyRequest.end());
	    	 
	    }); 
	    
	    
	    vertx.createHttpServer()
	    	.requestHandler(router::accept)
	    	.listen(properties.getServerPort(),"playhost",res->{
	    		if (res.succeeded()) {
	    			logger.info("Server is now listening!");
	    		} else {
	    			logger.info("Failed to bind!");
	    		}
	    	});
	  }
	  
	  private Integer getPort(String proxyDomain){
		  return properties.getProxyPort() ;
	  }
	
	  private String getUri(String proxyPath){
		  return "/"+proxyPath ;
	  }
	  private String getHost(String proxyDomain){
		  return properties.getProxyHost() ;
	  }
}
