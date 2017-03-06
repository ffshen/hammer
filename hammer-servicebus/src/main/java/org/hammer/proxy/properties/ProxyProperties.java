package org.hammer.proxy.properties;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        locations = "classpath:application.yml",
        prefix = ProxyProperties.PROXY_PREFIX)
public class ProxyProperties {

	public  static final String  PROXY_PREFIX = "proxy" ;
	
	private Integer serverPort ;
	
	private Integer proxyPort ;
	
	private String proxyHost ;	
	

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	
	
}
