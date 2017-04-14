package org.hammer.kafka.config;

import java.util.Properties;
import org.hammer.utils.JsonUtil; 

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.hammer.kafka.pool.KafkaProducerFactory;
import org.hammer.kafka.properties.KafkaProducerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import(KafkaProducerProperties.class)
@Scope("singleton")
public class KafkaProducerConfig {
	

    Logger logger = LoggerFactory.getLogger(this.getClass()); 
	
    @Autowired
    private KafkaProducerProperties properties ; 
	
	@Bean
	public ProducerConfig ProducerConfig() throws Exception {
		Properties props = new Properties();		 
		if(StringUtils.isEmpty(properties.getBrokerList())){
			throw new NullPointerException("please check your kafka producer config , broker list is null .") ; 
		}
		props.put("metadata.broker.list", properties.getBrokerList());
		props.put("serializer.class", properties.getSerializerClass());//这里设置partitioner
		props.put("partitioner.class", properties.getPartitionerClass());
		props.put("request.required.acks", properties.getRequestRequiredAcks());	
		props.put("zk.connect", properties.getZkConnect());	
		ProducerConfig config = new ProducerConfig(props);	
		logger.info("KafKa producer config : {}",JsonUtil.toJson(props));
		return config ;
	}

	@Bean
	@Scope("singleton")
	public static ObjectPool<Producer<String, String>> getProducerPool(ProducerConfig producerconfig) {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(10);
		config.setMinIdle(5);
		config.setMaxIdle(10);
		GenericObjectPool<Producer<String, String>> pool = new GenericObjectPool<Producer<String, String>>(new KafkaProducerFactory(producerconfig), config);
		return pool;
	}

}
