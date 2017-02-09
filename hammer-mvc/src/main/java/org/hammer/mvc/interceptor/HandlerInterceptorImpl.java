package org.hammer.mvc.interceptor;
 
import org.apache.commons.lang3.StringUtils;
import org.hammer.context.AppContext;
import org.hammer.context.generator.TraceIdGenerator;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


/**
 *  
 * @author shenx
 * @date 2017年2月6日
 * @see
 * @version
 */
public class HandlerInterceptorImpl implements HandlerInterceptor {

    @SuppressWarnings({ "rawtypes", "static-access" })
	@Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        try {
            HttpServletRequest request = httpServletRequest;
            if (!AppContext.isEmpty() &&
                    (StringUtils.equals(AppContext.getTraceId(), request.getHeader(AppContext.TRACE_ID))
                            || StringUtils.equals(AppContext.getTraceId(), request.getHeader(AppContext.TRACE_ID.toLowerCase())))) {
                return true;
            }
            AppContext.clear();
            AppContext.setNeedClear(false);
            Enumeration enumeration = request.getHeaderNames(); 
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement().toString();
                String value = request.getHeader(name);
                String key = AppContext.KEY_MAP.get(name.toLowerCase());                  
                if (key != null) {
                    AppContext.current().put(key, value);
                }
            }
        } 
        catch (Exception e) {
            System.out.println("Create HessianConext Error:" + e.getMessage());
        }
        //set traceId
        if (AppContext.isTraceIdEmpty()) {
            AppContext.put(AppContext.TRACE_ID, TraceIdGenerator.create());
        } 
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        AppContext.clear();
    }
}
