package org.hammer.dubbo.config;
 
import org.hammer.dubbo.properties.DubboProperties;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import; 

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig; 

@Configuration
@Import(DubboProperties.class)
public class DubboConfig {

    @Autowired
    private DubboProperties properties ; 
    
    @Bean
    public RegistryConfig registry() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(properties.getRegistryAddress());
        registryConfig.setProtocol(properties.getRegistryProtocol());
        return registryConfig;
    }
    
    @Bean
    public ApplicationConfig application() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(properties.getApplicationName());
        return applicationConfig;
    }
    
    @Bean
    public MonitorConfig monitorConfig() {
        MonitorConfig mc = new MonitorConfig();
        mc.setProtocol(properties.getMonitorProtocol());
        return mc;
    }
    
    @SuppressWarnings("rawtypes")
	@Bean
    public ReferenceConfig referenceConfig() {
        ReferenceConfig rc = new ReferenceConfig();
        rc.setMonitor(monitorConfig());
        return rc;
    }
    
    @Bean
    public ProtocolConfig protocol() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setPort(properties.getProtocolPort());
        return protocolConfig;
    }
    
    @Bean
    public ProviderConfig provider() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setMonitor(monitorConfig());
        providerConfig.setThreads(properties.getProviderThreads());
        return providerConfig;
    }
    

}
