package org.hammer.controller;
 
import org.hammer.context.AppContext;
import org.hammer.mvc.controller.BaseController;
import org.hammer.mvc.controller.DefaultWebApiResult;
import org.hammer.service.SampleInterface;
import org.hammer.vo.SampleVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
 
 

/**
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */

@RestController
@RequestMapping("/sample")
public class SampleController extends BaseController{

    private static Logger log = LoggerFactory.getLogger(SampleController.class);  
     
    @Autowired
    private SampleInterface i ;
    
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public DefaultWebApiResult sampleTest(@Validated @RequestBody SampleVo reqVo) {
        log.info("SampleController sampleTest : ",AppContext.getTraceId());
        return of(()->i.selectByPk(reqVo.getOrderId())) ;
    }
}
