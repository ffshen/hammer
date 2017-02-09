package org.hammer.other;
  

import org.hammer.utils.Encodes; 
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenx
 * @date 2017年2月3日
 * @see
 * @version
 */
public class EncodeTest {

    private static final Logger log = LoggerFactory.getLogger(EncodeTest.class); 
    
    private static final String str = "?param=1&param=2" ;
    
    @Test
    public void encode() throws Exception{
        log.info("decode ： {} ",Encodes.urlDecode(str ));
        
        log.info("encode ： {} ",Encodes.urlEncode(str ));

        log.info("decode ： {} ",Encodes.urlDecode(str )); 
    }
}
