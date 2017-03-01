package org.hammer.dubbo;
  
import org.hammer.service.DubboService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ReferenceBean;

@Configuration
public class DubboConsumerConfig {
	
    @Bean
    public ReferenceBean<DubboService> service() {
        ReferenceBean<DubboService> ref = new ReferenceBean<>();
//        ref.setProxy(proxy);
        ref.setVersion("myversion");
        ref.setInterface(DubboService.class);
        ref.setTimeout(5000);
        ref.setRetries(3);
        ref.setCheck(false);
        ref.setFilter("ContextConsumerFilter");
        return ref;
    }
}
