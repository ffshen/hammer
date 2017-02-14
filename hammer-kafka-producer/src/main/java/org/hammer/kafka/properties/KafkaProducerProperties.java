package org.hammer.kafka.properties;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        locations = "classpath:kafka.yml",
        prefix = KafkaProducerProperties.KAFKA_PRODUCER_PREFIX)
public class KafkaProducerProperties {

    public  static final String KAFKA_PRODUCER_PREFIX = "kafka.producer" ;
    
    private String brokerList ;
    
    private String serializerClass ;
    
    private String partitionerClass ;
    
    private String requestRequiredAcks ;

	public String getBrokerList() {
		return brokerList;
	}

	public void setBrokerList(String brokerList) {
		this.brokerList = brokerList;
	}

	public String getSerializerClass() {
		return serializerClass;
	}

	public void setSerializerClass(String serializerClass) {
		this.serializerClass = serializerClass;
	}

	public String getPartitionerClass() {
		return partitionerClass;
	}

	public void setPartitionerClass(String partitionerClass) {
		this.partitionerClass = partitionerClass;
	}

	public String getRequestRequiredAcks() {
		return requestRequiredAcks;
	}

	public void setRequestRequiredAcks(String requestRequiredAcks) {
		this.requestRequiredAcks = requestRequiredAcks;
	}
    
    
}
