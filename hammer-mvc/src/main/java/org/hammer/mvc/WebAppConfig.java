package org.hammer.mvc;
 
import org.hammer.mvc.filter.QueryStringDecodeFilter; 
import org.hammer.mvc.interceptor.HandlerInterceptorImpl;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered; 
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
 

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
@Configuration
@EnableRedisHttpSession
public class WebAppConfig extends WebMvcConfigurerAdapter {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) { 
        registry.addInterceptor(new HandlerInterceptorImpl()); 
    }
     
    @Bean
    public FilterRegistrationBean queryStrDecodeFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.addUrlPatterns("/*");
        bean.setFilter(new QueryStringDecodeFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        bean.setName(QueryStringDecodeFilter.FilterName);
        return bean;
    }
     
}
