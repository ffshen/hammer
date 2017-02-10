package org.hammer.mvc.interceptor;

import java.io.IOException;

import org.hammer.context.AppContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Service;

@Service
public class ClientHttpRequestInterceptorImpl implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution )
			throws IOException {
	    HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);  
	    requestWrapper.getHeaders().set(AppContext.TRACE_ID, AppContext.getTraceId());
	    return execution.execute(requestWrapper, body); 
	}

}
