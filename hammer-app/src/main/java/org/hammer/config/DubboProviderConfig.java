package org.hammer.config;

import org.hammer.dubbo.config.DubboConfig;
import org.hammer.service.DubboService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.alibaba.dubbo.config.spring.ServiceBean;

@Configuration
@Import(DubboConfig.class)
public class DubboProviderConfig {
	
	  @Bean
	  public ServiceBean<DubboService> personServiceExport(DubboService service) {
	      ServiceBean<DubboService> serviceBean = new ServiceBean<DubboService>();
	      serviceBean.setProxy("javassist");
	      serviceBean.setVersion("myversion");
	      serviceBean.setInterface(DubboService.class.getName());
	      serviceBean.setRef(service);
	      serviceBean.setTimeout(5000);
	      serviceBean.setRetries(3);
	      serviceBean.setFilter("ContextProviderFilter");
	      return serviceBean;
	  }
}
