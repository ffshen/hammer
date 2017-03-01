package org.hammer;
 

import kafka.consumer.ConsumerConfig; 


import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.hammer.concurrency.pool.ExecutorServiceFactory;
import org.hammer.kafka.consumer.AbstractKafkaConsumer; 
import org.hammer.kafka.properties.KafkaConsumerProperties;
import org.hammer.kafka.runable.SampleRunable;
import org.hammer.kafka.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import; 

@SpringBootApplication
@Import(KafkaConsumerProperties.class)
public class ConsumerApp extends AbstractKafkaConsumer implements DisposableBean,CommandLineRunner{

    private static Logger logger = LoggerFactory.getLogger(ConsumerApp.class);
    
	@Autowired
	private KafkaConsumerProperties properties ;	    
    
	@Override
	public void run(String... arg0) throws Exception {
		try{
	        if (properties.getSecurityMode())
	        {
	            try
	            {	                
	                //!!注意，安全认证时，需要用户手动修改为自己申请的机机账号.
	            	//TODO.后续配置.
	                SecurityUtils.securityPrepare(null,null,null,null);
	            }
	            catch (IOException e)
	            {
	            	logger.error("Security prepare failure.",e);	                
	            }
	            logger.info("Security prepare success.");
	        }	        
			doStart((streams)->{
			    streams.stream().forEach((s)->{
			    	getExecutor().submit(new SampleRunable(s)) ;
		    	});
			});
		}
		catch(Exception ex){
			logger.error("error",ex);
			doDestory() ;
		}
		
	}
    public static void main( String[] args )
    {
    	SpringApplication.run(ConsumerApp.class);
    }
	@Override
	public String getTopic() {
		return "example-metric1";
	}
	
	@Override
	public Integer getPartition() {
		return 4 ;
	}
	
	@Override
	protected ConsumerConfig getConsumerConfig() {
        Properties props = new Properties();
        props.put("zookeeper.connect", properties.getZookeeperConnect());
        props.put("group.id", properties.getGroupId());
        props.put("zookeeper.session.timeout.ms", properties.getZookeeperSessionTimeoutMs());
        props.put("zookeeper.sync.time.ms", properties.getZookeeperSyncTimeMs());
        props.put("auto.commit.interval.ms", properties.getAutoCommitIntervalMs()); 
        return new ConsumerConfig(props);
	}
	@Override
	protected ExecutorService getExecutor() {
		return ExecutorServiceFactory.getExecutorService();
	}
	
	@Override
	public void destroy() throws Exception {
		doDestory() ;		
	}

}
