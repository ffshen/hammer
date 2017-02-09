package org.hammer.mvc.http;

import org.hammer.http.RestClient;
import org.hammer.mvc.controller.DefaultWebApiResult;
import org.hammer.mvc.interceptor.ClientHttpRequestInterceptorImpl;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service
public class SimpleHttpClient {

	public static DefaultWebApiResult post(String url ,Object obj){
		RestClient.getRestTemplate().setInterceptors(Lists.newArrayList(new ClientHttpRequestInterceptorImpl()));
    	return RestClient.getRestTemplate().postForObject(url, obj , DefaultWebApiResult.class) ;
	}
}
