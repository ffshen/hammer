package org.hammer.kafka.config;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SamplePartitioner implements Partitioner{	

    Logger logger = LoggerFactory.getLogger(this.getClass());  
    
	public SamplePartitioner (VerifiableProperties props) {
		super() ;
	}
	
	@Override
	public int partition(Object key, int num) {
		int partition = 0;
        String stringKey = (String) key;
        int offset = stringKey.lastIndexOf('.');
        if (offset > 0) {
           partition = Integer.parseInt( stringKey.substring(offset+1)) % num;
        }        
        logger.info(" SimplePartitioner partitions : {}, stringKey ： {} ,partition : {} " ,num,stringKey,partition);
        
        return partition;
	}

}
