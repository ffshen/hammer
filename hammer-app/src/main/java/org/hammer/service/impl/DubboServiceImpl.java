package org.hammer.service.impl;
  
import org.hammer.service.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
 

@Service
public class DubboServiceImpl implements DubboService {

    private static Logger log = LoggerFactory.getLogger(DubboServiceImpl.class);  
	@Override
	public void say(String word) {
		log.info(" DubboService say : {} ",word);
		
	}

}
