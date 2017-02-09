package org.hammer.hessian.client;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;

import org.apache.commons.lang3.BooleanUtils;
import org.hammer.context.AppContext;
import org.hammer.context.generator.TraceIdGenerator;

import java.lang.reflect.Method;
import java.net.URL;

/**
 * Created by ansion on 16/7/9.
 */
public class CustomHessianProxy extends HessianProxy {
 
    private static final long serialVersionUID = -7664193408254752520L;

    protected CustomHessianProxy(URL url, HessianProxyFactory factory) {
        super(url, factory);
    }

    protected CustomHessianProxy(URL url, HessianProxyFactory factory, Class<?> type) {
        super(url, factory, type);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        setContext(method, args);
        return super.invoke(proxy, method, args);
    }

    @Override
    protected void addRequestHeaders(HessianConnection conn)
    {
        //add traceId
        super.addRequestHeaders(conn);
        try {
            conn.addHeader(AppContext.TRACE_ID, AppContext.getTraceId());
        } catch (Exception e) {
            System.out.println("Hessian_add_header_error:" + e.getMessage());
        }
    }

    protected void setContext(Method method, Object[] args) {
        
        if(AppContext.isNeedClear()) {
            AppContext.clear();
        }
        //set traceId        
        if(BooleanUtils.or(new boolean[]{AppContext.isTraceIdEmpty(),AppContext.getTraceId().equals(TraceIdGenerator.DEFAULT_TRACE_ID_PREFIX)})){
            System.out.println("CustomHessianProxy setContext generate a new traceId");
            AppContext.put(AppContext.TRACE_ID, TraceIdGenerator.create());
        }
    }


}