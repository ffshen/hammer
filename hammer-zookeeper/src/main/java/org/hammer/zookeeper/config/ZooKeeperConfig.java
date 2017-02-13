package org.hammer.zookeeper.config;


import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory; 
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.hammer.zookeeper.listener.IConnectionStateListener;
import org.hammer.zookeeper.properties.ZkCuratorProperties; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ZkCuratorProperties.class)
public class ZooKeeperConfig {
 
	
    @Autowired
    private ZkCuratorProperties properties ;
    
	@Autowired
	@Qualifier("ReconnectHandler")
	IConnectionStateListener errorhandle;
		
	@Bean
	public RetryPolicy getRetryPolicy() {
		RetryPolicy retryPolicy = 
				new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(),
					properties.getMaxRetries(),
					properties.getMaxSleepMs());
		return retryPolicy;
	}	
	
	@Bean
	public CuratorFramework singleCaseClient(){
		String connectString = properties.getConnectString() ;
		int sessionTimeoutMs = properties.getSessionTimeoutMs() ;
		int connectionTimeoutMs = properties.getConnectionTimeoutMs() ;
		String namespace = properties.getNamespace() ;
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString(connectString)
				.connectionTimeoutMs(connectionTimeoutMs)
				.sessionTimeoutMs(sessionTimeoutMs)
				.retryPolicy(getRetryPolicy()).namespace(namespace).build();
		client.start();
		if(errorhandle()!=null){
			client.getConnectionStateListenable().addListener(errorhandle());
		}
		return client;
	}
	
	@Bean
	public IConnectionStateListener errorhandle(){
		return errorhandle ;
	}
	

}
