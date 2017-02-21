package org.hammer.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate; 

public class AsyncRestClient {

    Logger logger = LoggerFactory.getLogger(this.getClass());  

    private static class LazyHolder{   
    	
    	 private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 100;

    	 private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;

    	 private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (60 * 1000);
    	 
    	 private static AsyncRestTemplate  restTemplate  ;
    	
    	static {
    		  try {
    			   PoolingNHttpClientConnectionManager
    			   		connectionManager
    			   			= new PoolingNHttpClientConnectionManager( new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
    			   connectionManager.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
    			   connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
    			    
    			   RequestConfig config 
    			   		= RequestConfig
    			   			.custom()
    			   			.setConnectTimeout(DEFAULT_READ_TIMEOUT_MILLISECONDS)
    			   			.build();

    			    CloseableHttpAsyncClient httpclient 
    			    	= HttpAsyncClientBuilder
    			    		.create()
    			    		.setConnectionManager(connectionManager)
    			    		.setDefaultRequestConfig(config).build();
    			    
    			    restTemplate = new AsyncRestTemplate(new HttpComponentsAsyncClientHttpRequestFactory( httpclient), RestClient.getRestTemplate());
    		  }
    		  catch(Exception ex){
    			  ex.printStackTrace();
    		  } 		
    	}    	
    }

	public AsyncRestClient(){  }
	
    public static AsyncRestTemplate getAsyncRestTemplate() {
        return LazyHolder.restTemplate;
    }
	
}
