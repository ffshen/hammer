package org.hammer.mvc.controller;


import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.hammer.concurrency.SimpleAsyncUtils;
import org.hammer.exception.RetException;
import org.hammer.mvc.DefaultWebApiResult;
import org.hammer.mvc.context.LoginContextHolder;
import org.hammer.utils.JsonUtil;
import org.hammer.vo.OperatorVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.async.DeferredResult;
 
 

/**
 * 公共基础Controller
 *
 * @author Administrator
 * @date 2016年7月8日
 * @see
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
 

    protected DefaultWebApiResult success() {
        return DefaultWebApiResult.success();
    }

    protected DefaultWebApiResult success(Object data) {
        return DefaultWebApiResult.success(data);
    }
    
    /**
     * @param errCode   错误码
     * @param errMsg    错误信息
     * @param ex        异常, 输出到日志
     * @param reqParams 请求参数, 转换成JSON格式, 并输出到日志
     * @return
     */
    protected DefaultWebApiResult failure(String errCode, String errMsg, Throwable ex, Object... reqParams) {
        logger.error("[errCode: {}, errMsg: {}, reqParams: {}]", errCode, errMsg, JsonUtil.toJson(reqParams), ex);
        return new DefaultWebApiResult(errMsg, errCode);
    }
    
    protected void needLog() {        
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0) {
            throw new RetException(DefaultWebApiResult.LOSE_LOGININFO_FAIL_CODE,DefaultWebApiResult.LOSE_LOGININFO_FAIL_MSG);
        }
        OperatorVo vo = new OperatorVo();
        Stream.of(cookies).forEach(cookie -> {
            if(logger.isDebugEnabled()){
                logger.debug("Current Cookie:{}:{}" ,cookie.getName() , cookie.getValue());
            }            
            if("userId".equals(cookie.getName())) {
                vo.setOperator(Integer.parseInt(cookie.getValue()));
            } else if ("userName".equals(cookie.getName())) {
                vo.setOperaterName(cookie.getValue());
            }
        });
        LoginContextHolder.setOperator(vo);
    }

    /**
     * factory method, use Supplier lambda to create a result
     *
     * @return DefaultWebResult
     */
    protected DefaultWebApiResult of(Supplier supplier) {
        try {
            return Optional.ofNullable(supplier.get())
                    .map(r -> success(r))
                    .orElse(DefaultWebApiResult.failure(DefaultWebApiResult.BUSINESS_BUSY_FAIL_CODE, DefaultWebApiResult.BUSINESS_BUSY_FAIL_MSG));

        } catch (RetException e) {
            logger.error("Supplier service execute throw RetException error - ", e);
            return DefaultWebApiResult.failure(e.getRetCode(), e.getResMsg());
        } catch (Exception e) {
            logger.error("Supplier service execute throw Exception error - ", e);
            return DefaultWebApiResult.failure(DefaultWebApiResult.BUSINESS_BUSY_FAIL_CODE, DefaultWebApiResult.BUSINESS_BUSY_FAIL_MSG);
        }
    }

    /**
     * factory method, use Consumer lambda to create a result
     *
     * @return DefaultWebResult
     */
    public   DefaultWebApiResult of(NothingConsumer consumer) {
        try {
            consumer.accept();
            return success();
        } catch (RetException e) {
            logger.error("NothingConsumer service execute throw RetException error - ", e);
            return DefaultWebApiResult.failure(e.getRetCode(), e.getResMsg());
        } catch (Exception e) {
            logger.error("NothingConsumer service execute throw RetException error - ", e);
            return DefaultWebApiResult.failure(DefaultWebApiResult.BUSINESS_BUSY_FAIL_CODE, DefaultWebApiResult.BUSINESS_BUSY_FAIL_MSG);
        }
    }
    
    protected DeferredResult<DefaultWebApiResult> asyncOf(Supplier supplier){
    	DeferredResult<DefaultWebApiResult> deferredResult = new DeferredResult<DefaultWebApiResult>();
    	CompletableFuture<DefaultWebApiResult> future
    		= SimpleAsyncUtils
    			.supplyAsyncWithPool(()->{
    				return  of( supplier) ;
    			}) ; 
    	try {
			deferredResult.setResult(future.get()) ;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
    	return deferredResult ;
    }

    @FunctionalInterface
    public interface Supplier {

        /**
         * Gets a result.
         *
         * @return a result
         */
        Object get() throws RetException, Exception;
    }

    @FunctionalInterface
    public interface NothingConsumer {
        /**
         * Performs this operation on the nothing argument.
         */
        void accept() throws RetException, Exception;
    }


}
