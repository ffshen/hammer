package org.hammer.kafka.consumer;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService; 
import java.util.concurrent.TimeUnit;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.consumer.ConsumerConfig; 
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector; 
 
 
public abstract class AbstractKafkaConsumer {		

    private static Logger logger = LoggerFactory.getLogger(AbstractKafkaConsumer.class);
	
	private ConsumerConnector consumerConnector ;
	
	protected  abstract String getTopic() ;
	
	protected  abstract Integer getPartition() ;
	
	protected  abstract ConsumerConfig getConsumerConfig() ; 	

	protected  abstract ExecutorService getExecutor();
	
	public void doStart(KafkaConsumer consumer) throws Exception {	
		
		consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(getConsumerConfig()) ;

		HashMap<String, Integer> topicCountMap = new HashMap<String, Integer>();		
		
		//4个进程去消费消息
		topicCountMap.put(getTopic(), getPartition() );
		
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap);
		
		List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(getTopic());
		
		consumer.accept(streams);
	}
	
	public void doDestory() throws Exception {
		logger.info(" AbstractKafkaConsumer  doDestory Objects .");
		if (consumerConnector != null) consumerConnector.shutdown();
		
		if (getExecutor() != null) getExecutor().shutdown();
		
		try {
			if (getExecutor() != null){
	            if (!getExecutor().awaitTermination(5000, TimeUnit.MILLISECONDS)) {
	            	logger.info("Timed out waiting for consumer threads to shut down, exiting uncleanly");
	            }
			}
	     } catch (InterruptedException e) {
	    	 logger.info("Interrupted during shutdown, exiting uncleanly");
	     }
	}

    @FunctionalInterface
    public interface KafkaConsumer {     	
        void accept(List<KafkaStream<byte[], byte[]>> streams) throws  Exception;
    }
}
