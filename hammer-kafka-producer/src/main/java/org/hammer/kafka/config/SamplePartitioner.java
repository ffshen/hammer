package org.hammer.kafka.config;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SamplePartitioner implements Partitioner{	

    Logger logger = LoggerFactory.getLogger(this.getClass());  
    
	public SamplePartitioner (VerifiableProperties props) {
		super() ;
	}
	
	@Override
	public int partition(Object key, int num) {
		try{
	    	Random rnd = new Random();
	    	int p = rnd.nextInt(num) ;
	    	logger.info("partition:{}",p);
			return  p ;
		}
		catch(Exception ex){
			ex.printStackTrace();
			return 1 ;
		} 
	}

}
