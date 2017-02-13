package org.hammer.mvc.http;

import org.hammer.http.RestClient;
import org.hammer.mvc.controller.DefaultWebApiResult;
import org.hammer.mvc.interceptor.ClientHttpRequestInterceptorImpl;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class SimpleHttpClient {
	 
	private static ClientHttpRequestInterceptor clientHttpRequestInterceptor = new ClientHttpRequestInterceptorImpl();

	public static DefaultWebApiResult post(String url ,Object obj){
		RestClient.getRestTemplate().setInterceptors(Lists.newArrayList(clientHttpRequestInterceptor));
    	return RestClient.getRestTemplate().postForObject(url, obj , DefaultWebApiResult.class) ;
	}
}
