package org.hammer.config;
  
import org.hammer.constants.HessianClientConstants;
import org.hammer.hessian.server.CustomHessianServiceExporter;
import org.hammer.service.AppHessianClientAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

/**
 * 
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
@ComponentScan(basePackages={"org.hammer"})
@Configuration
public class HessianServerConfig {
    @Autowired
    private AppHessianClientAPI api;

    @Bean(name = HessianClientConstants.SERVICE_MAPPING)
    public HessianServiceExporter hessianAPI() {
        return CustomHessianServiceExporter.create(api, AppHessianClientAPI.class);
    }
}
