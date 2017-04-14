package org.hammer.kafka.runable; 

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
/**
 * 可以在这里调用具体的业务逻辑
 * @author 祥
 *
 */
public class SampleRunable implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(SampleRunable.class);
	
	 private KafkaStream<byte[], byte[]> stream;//消息通道，可以取他的编号的 
	 
	 public SampleRunable(KafkaStream<byte[], byte[]> param) { 
		 stream = param;
	 }
	 @Override
	 public void run() {
	 	ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()){ 
        	System.out.println(".....");
        	MessageAndMetadata<byte[], byte[]> msg = it.next();
        	logger.info(" partition : {} , key : {} ,message : {} ",msg.partition(),new String(msg.key()),new String(msg.message()) );       
       	}
	}

}
