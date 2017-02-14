package org.hammer.kafka.config;
import kafka.consumer.ConsumerConfig;
 
import org.hammer.kafka.properties.KafkaConsumerProperties; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import; 
 
import java.util.Properties;

@Configuration
@Import(KafkaConsumerProperties.class)
public class KafkaConsumerConfig {
		
	private  static final String LINUX = "Linux" ;
	
	private  static final String WINDOWS = "Windows" ;
	
	@Autowired
	private KafkaConsumerProperties properties ;
	 
	public enum SysOs{
		L(LINUX) ,  W(WINDOWS) ;

        private String type;

        private SysOs(String type) {
            this.type = type;
        }
        
        public String value() {
            return type;
        }
	}
	
	
    public ConsumerConfig getKafkaConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", properties.getZookeeperConnect());
        props.put("group.id", properties.getGroupId());
        props.put("zookeeper.session.timeout.ms", properties.getZookeeperSessionTimeoutMs());
        props.put("zookeeper.sync.time.ms", properties.getZookeeperSyncTimeMs());
        props.put("auto.commit.interval.ms", properties.getAutoCommitIntervalMs()); 
        return new ConsumerConfig(props);
    }
	
	public String getOs() {
    	String os = System.getProperty("os.name"); 
    	if(os.toLowerCase().startsWith("win")){  
    		 return  SysOs.W.value() ;
    	} 
    	return SysOs.L.value() ;
	}	
	
}
