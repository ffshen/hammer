package org.hammer.kafka.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        locations = "classpath:kafka.yml",
        prefix = KafkaConsumerProperties.KAFKA_CONSUMER_PREFIX)
public class KafkaConsumerProperties {

    public  static final String KAFKA_CONSUMER_PREFIX = "kafka.consumer" ;
    
    private String zookeeperConnect ;
    
    private String groupId ;
    
    private String zookeeperSessionTimeoutMs ;
    
    private String zookeeperSyncTimeMs ;
    
    private String autoCommitIntervalMs ;
    
    private Boolean securityMode ;

	public String getZookeeperConnect() {
		return zookeeperConnect;
	}

	public void setZookeeperConnect(String zookeeperConnect) {
		this.zookeeperConnect = zookeeperConnect;
	}

	public String getGroupId() {
		return groupId;
	}
 

	public String getZookeeperSessionTimeoutMs() {
		return zookeeperSessionTimeoutMs;
	}

	public void setZookeeperSessionTimeoutMs(String zookeeperSessionTimeoutMs) {
		this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
	}

	public String getZookeeperSyncTimeMs() {
		return zookeeperSyncTimeMs;
	}

	public void setZookeeperSyncTimeMs(String zookeeperSyncTimeMs) {
		this.zookeeperSyncTimeMs = zookeeperSyncTimeMs;
	}

	public String getAutoCommitIntervalMs() {
		return autoCommitIntervalMs;
	}

	public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
		this.autoCommitIntervalMs = autoCommitIntervalMs;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Boolean getSecurityMode() {
		return securityMode;
	}

	public void setSecurityMode(Boolean securityMode) {
		this.securityMode = securityMode;
	}
    
    
}
