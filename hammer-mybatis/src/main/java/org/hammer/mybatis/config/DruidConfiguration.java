package org.hammer.mybatis.config;
 

import java.util.Map;

import org.hammer.mybatis.properties.DruidProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Maps; 

/**
 * @author shenx
 * @date 2017年1月29日
 * @see
 * @version
 */

@Configuration
@Import(DruidProperties.class)
public class DruidConfiguration {
    
    @Autowired
    private DruidProperties properties ;
    

    @Bean
    public ServletRegistrationBean druidServlet() {
        StatViewServlet servlet = new StatViewServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername", properties.getLoginUsername());
        servletRegistrationBean.addInitParameter("loginPassword", properties.getLoginPassword());
        return servletRegistrationBean;
    }
    
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        Map<String, String> initParameters = Maps.newHashMap();
        initParameters.put("sessionStatMaxCount", properties.getSessionStatMaxCount());
        initParameters.put("profileEnable", properties.getProfileEnable());
        initParameters.put("sessionStatEnable", properties.getSessionStatEnable());
        //可添加hessian的Http过滤器.TODO.
        WebStatFilter filter = new WebStatFilter();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setInitParameters(initParameters);
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
