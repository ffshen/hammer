package org.hammer.kafka.pool;
 

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig; 
 
public class KafkaProducerFactory extends BasePooledObjectFactory<Producer<String, String>>{

    Logger logger = LoggerFactory.getLogger(this.getClass());   
	 
	ProducerConfig producerconfig; 
	
	public KafkaProducerFactory(ProducerConfig config) {
		super();
		this.producerconfig = config ;
	}
	
	@Override
	public Producer<String, String> create() throws Exception {
		Producer<String, String> producer = new Producer<String, String>(this.producerconfig);
		return producer;
	}

	@Override
	public PooledObject<Producer<String, String>> wrap(Producer<String, String> arg0) {
		return new DefaultPooledObject<Producer<String, String>>(arg0) ;
	}

    @Override 
    public void destroyObject(PooledObject<Producer<String, String>> p) throws Exception {
    	logger.info("Producer close !");
    	Producer<String, String> object = p.getObject(); 
    	object.close();
    }
 
}
