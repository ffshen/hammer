package org.hammer.dubbo.properties;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        locations = "classpath:dubbo.yml",
        prefix = DubboProperties.DUBBO_PREFIX)
public class DubboProperties {

    public  static final String DUBBO_PREFIX = "dubbo.common" ;
    
    private String registryAddress ;

    private String registryProtocol ;
    
    private String applicationName ;
    
    private String monitorProtocol ;
    
    private Integer protocolPort ;
    
    private Integer providerThreads ;

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getRegistryProtocol() {
		return registryProtocol;
	}

	public void setRegistryProtocol(String registryProtocol) {
		this.registryProtocol = registryProtocol;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getMonitorProtocol() {
		return monitorProtocol;
	}

	public void setMonitorProtocol(String monitorProtocol) {
		this.monitorProtocol = monitorProtocol;
	}

	public Integer getProtocolPort() {
		return protocolPort;
	}

	public void setProtocolPort(Integer protocolPort) {
		this.protocolPort = protocolPort;
	}

	public Integer getProviderThreads() {
		return providerThreads;
	}

	public void setProviderThreads(Integer providerThreads) {
		this.providerThreads = providerThreads;
	}
    
     
}
