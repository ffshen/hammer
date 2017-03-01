package org.hammer.mvc.http;
 
 
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
 
import org.hammer.concurrency.pool.ExecutorServiceFactory;
import org.hammer.context.AppContext;
import org.hammer.http.AsyncRestClient;
import org.hammer.http.RestClient; 
import org.hammer.mvc.controller.DefaultWebApiResult;
import org.hammer.mvc.interceptor.ClientHttpRequestInterceptorImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture; 
 

@Service
public class SimpleHttpClient {
	 
	private static ClientHttpRequestInterceptor clientHttpRequestInterceptor = new ClientHttpRequestInterceptorImpl();
	
	public static class HttpBuilder<T>  {		

		private static RestTemplate restTemplate = RestClient.getRestTemplate() ;

		private static AsyncRestTemplate asynRestTemplate = AsyncRestClient.getAsyncRestTemplate() ;
		
		private String url ;
		
		private Object body ;
		
		private HashMap<String, Object> urlVariables ;
		
		private MultiValueMap<String, String> headers ;
		
		private List<ClientHttpRequestInterceptor> interceptors ;
		
		private Class<?> responseType ;
		
		private FutureCallback<ResponseEntity<T>> callback ;
		
		private HttpBuilder(){
			url = null ;
			body = null ;
			headers = new LinkedMultiValueMap<String, String>(); 
			interceptors = Lists.newArrayList() ; 
		}
		
		public HttpBuilder<T>   withUrl(String u){
			this.url = u ;
			return this ;
		}
		
		public HttpBuilder<T>   withHeader(String name ,String value){
			this.headers.add(name,value) ;
			return this ;
		}
		
		public HttpBuilder<T>   withBody( Object b){
			this.body = b ;
			return this ;
		}
		
		public HttpBuilder<T>   withResponseType( Class<?> r){
			this.responseType = r ;
			return this ;
		}
		
		public HttpBuilder<T> withUrlVariables(HashMap<String, Object> v){
			this.urlVariables = v ;
			return this ;
		}
		
		public HttpBuilder<T> withUrlVariable(String key ,Object value){
			this.urlVariables.put(key, value) ;
			return this ;
		}
		
		public HttpBuilder<T>   withInterceptor(ClientHttpRequestInterceptor i){
			this.interceptors.add(i) ;
			return this ;
		}

        @SuppressWarnings({ "unchecked", "rawtypes" })
		public HttpBuilder  withCallback(FutureCallback  c){	
	        InvocationHandler handler = new FutureCallbackProxy(c);	        
			FutureCallback<ResponseEntity<T>> proxyinstance =
	        		(FutureCallback<ResponseEntity<T>>)Proxy
	        				.newProxyInstance(FutureCallback.class.getClassLoader(),
	        										new Class[]{FutureCallback.class}, 
	        											handler);  
			
			this.callback = proxyinstance ;
			return this ;
		}

		@SuppressWarnings({"unchecked"})
		public ResponseEntity<T> get(){   
			ResponseEntity<T> output  = (ResponseEntity<T>) restTemplate.getForEntity(url, responseType, urlVariables)		;
			return  output ;
		}        

		@SuppressWarnings({"unchecked", "rawtypes"})
		public ResponseEntity<T> post(){   
			HttpEntity entity = new HttpEntity(body ,headers);
			restTemplate.setInterceptors(this.interceptors);
			ResponseEntity<T> output  = (ResponseEntity<T>) restTemplate.postForEntity(url, entity, responseType ) ;			
			return  output ;
		}

		@SuppressWarnings({"unchecked", "rawtypes"})
		public void postAsync(){ 
			headers.add(AppContext.TRACE_ID, AppContext.getTraceId());
			HttpEntity entity = new HttpEntity(body ,headers);	 
			
			org.springframework.util.concurrent.ListenableFuture  
					response = (org.springframework.util.concurrent.ListenableFuture ) asynRestTemplate.postForEntity(url , entity, responseType);
			
			ListenableFuture  future = JdkFutureAdapters.listenInPoolThread(response,  ExecutorServiceFactory.getExecutorService()) ;
			
			Futures.addCallback(future, callback ,ExecutorServiceFactory.getExecutorService());
		}	
		
		private class  FutureCallbackProxy implements InvocationHandler{		 	   
			   private AppContext parentContext; 			   
			   private FutureCallback<ResponseEntity<T>> callback ;
			   
			   public FutureCallbackProxy(FutureCallback<ResponseEntity<T>> c){
				   parentContext = AppContext.current();
				   callback = c ;
			   }			   
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		        	AppContext.current(parentContext);
					return method.invoke(callback, args);
				}
		   }
	}
	 
	public static HttpBuilder<DefaultWebApiResult>  prepareDefaultWebApiResult(){
		return new HttpBuilder<DefaultWebApiResult>()
				.withInterceptor(clientHttpRequestInterceptor) 
			    .withResponseType(DefaultWebApiResult.class) ;
	}
	 
	public static HttpBuilder<?>  prepare(Class<?> responseType){
		return new HttpBuilder<>()
				.withInterceptor(clientHttpRequestInterceptor) 
			    .withResponseType(responseType) ;
	}
	
}
